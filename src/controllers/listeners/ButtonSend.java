package controllers.listeners;

import models.Model;
import views.View;
import models.Database;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

/**
 * Class for button Send
 */
public class ButtonSend implements ActionListener {
    private final Model model;
    private final View view;
    private final Database database;
    public static Set<Character> guessedLettersSet = new HashSet<>();

    /**
     * Constructor
     * @param model Model
     * @param view View
     */
    public ButtonSend(Model model, View view) {
        this.model = model;
        this.view = view;
        this.database = new Database(model);
    }

    /**
     * Possible actions for button Send
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        //System.out.println("Guessing: " + model.getWordToGuess());

        view.getTxtChar().requestFocus();
        String enteredChars = view.getTxtChar().getText().toUpperCase();
        if (enteredChars.isEmpty()) {
            return;
        }
        char guessedLetter = enteredChars.charAt(0);
        int maxImageId = 11;
        if (guessedLettersSet.contains(guessedLetter)) {
            model.getMissedLetters().add(enteredChars);
            if (model.getImageId() < maxImageId) {
                model.setImageId(model.getImageId() + 1);
                view.setNewImage(model.getImageId());
            }
            view.getLblError();
        } else {
            guessedLettersSet.add(guessedLetter);
            String guessWord = model.getWordToGuess();
            char[] guessList = guessWord.toCharArray();
            boolean correct = true;
            for (int i = 0; i < guessList.length; i++){
                if (guessList[i] == guessedLetter){
                    model.getNewWord().setCharAt(i, guessedLetter);
                    view.getLblResult().setText(model.spaceBetweenWords(model.getNewWord().toString()));
                    correct = false;
                }
            }
            if (correct) {
                model.getMissedLetters().add(enteredChars);
                model.setImageId(model.getImageId() + 1); // Update the image ID
                view.setNewImage(model.getImageId()); // Set the new image
                view.getLblError();
            }
        }
        model.setCountMissedWords(model.getMissedLetters().size());
        view.getLblError().setText("Wrong: " + model.getCountMissedWords() + " letter(s) " + model.getMissedLetters());
        view.getTxtChar().setText("");
        if (!model.getNewWord().toString().contains("_")) {
            view.getGameTime().stopTimer();
            //System.out.println(view.getGameTime().getPlayedTimeInSeconds());
            model.askPlayerName();
            database.insertScoreToTable(view);
            guessedLettersSet.clear();
            view.getRealDateTime().start();
            view.showNewButton();
            view.getLblResult().setText("L E T ' S  P L A Y");
        }
        if (!(model.getCountMissedWords() < maxImageId)) {
            view.getGameTime().stopTimer();
            view.getGameTime().setRunning(false);
            JOptionPane.showMessageDialog(null, "Too many wrong letters", "Game Over", JOptionPane.PLAIN_MESSAGE);
            view.getRealDateTime().start();
            view.getLblResult().setText("L E T ' S  P L A Y");
            view.showNewButton();
        }
    }
}