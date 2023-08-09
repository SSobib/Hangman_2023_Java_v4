package models;

import models.datastructures.DataScores;
import models.datastructures.DataWords;
import views.View;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A class for interacting with and querying a database.
 */
public class Database {
    private Connection connection = null;
    private final String databaseUrl;
    private final Model model;

    /**
     * A database constructor that is always invoked when an object is created.     *
     * @param model Model
     */
    public Database(Model model) {
        this.model = model;
        this.databaseUrl = "jdbc:sqlite:" + model.getDatabaseFile();
    }

    /**
     * Database connection
     * @return Connection
     * @throws SQLException throws error on console.
     */
    private Connection dbConnection() throws SQLException {
        // https://stackoverflow.com/questions/13891006/
        if(connection != null) {
            connection.close();
        }
        connection = DriverManager.getConnection(databaseUrl);
        return connection;
    }

    /**
     * The method reads unique category names from the database and writes them to the cmbNames variable of the model.
     */
    public List<DataWords> getWords() throws SQLException {
        List<DataWords> dataWords = new ArrayList<>();
        String sql = "SELECT * FROM words ORDER BY category, word";
        List<String> categories = new ArrayList<>(); // NB! See on meetodi sisene muutuja categories!
        try {
            Connection conn = this.dbConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt("id");
                String word = rs.getString("word");
                String category = rs.getString("category");
                dataWords.add(new DataWords(id, word, category)); // Add words to dataWords list
                categories.add(category);
            }
            // https://howtodoinjava.com/java8/stream-find-remove-duplicates/
            List<String> unique = categories.stream().distinct().collect(Collectors.toList());
            model.setCorrectCmbNames(unique); // Unikaalsed nimed Listist String[] listi categories

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dataWords;
    }
    /**
     * This method reads the entire leaderboard content from the database and writes it to the model's dataScores
     * variable
     */
    public void selectScores() {
        String sql = "SELECT * FROM scores ORDER BY gametime, playertime DESC, playername";
        List<DataScores> data = new ArrayList<>();
        try {
            Connection conn = this.dbConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            model.getDataScores().clear();
            while (rs.next()) {
                String datetime = rs.getString("playertime");
                LocalDateTime playerTime = LocalDateTime.parse(datetime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                String playerName = rs.getString("playername");
                String guessWord = rs.getString("guessword");
                String wrongChar = rs.getString("wrongcharacters");
                int timeSeconds = rs.getInt("gametime");
                data.add(new DataScores(playerTime, playerName, guessWord, wrongChar, timeSeconds));
            }
            model.setDataScores(data); // Write dataScore in the model variable

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * For inserting data to database
     * TO-DO example is here https://alvinalexander.com/java/java-mysql-insert-example-preparedstatement/
     * TO-DO example to format dates https://stackoverflow.com/questions/64759668/what-is-the-correct-datetimeformatter-pattern-for-datetimeoffset-column#:~:text=You%20need%20to%20use%20the,SSSSSS%20xxx%20.
     */

    public void insertScoreToTable(View view) {

        String sql = "INSERT INTO scores (playertime, playername, guessword, wrongcharacters, gametime) VALUES (?, ?, ?, ?, ?)";
        String removeBrackets = model.getMissedLetters().toString().replace("[", "").replace("]", "");
        DataScores endTime = new DataScores(LocalDateTime.now(), model.getPlayerName(), model.getWordToGuess(), removeBrackets, view.getGameTime().getPlayedTimeInSeconds());

        try (Connection conn = this.dbConnection();
             PreparedStatement preparedStmt = conn.prepareStatement(sql)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String playerTime = endTime.gameTime().format(formatter);
            preparedStmt.setString(1, playerTime);
            preparedStmt.setString(2, endTime.playerName());
            preparedStmt.setString(3, endTime.guessWord());
            preparedStmt.setString(4, endTime.missingLetters());
            preparedStmt.setInt(5, endTime.timeSeconds());
            preparedStmt.executeUpdate();
            selectScores();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
