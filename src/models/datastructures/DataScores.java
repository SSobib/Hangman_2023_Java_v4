package models.datastructures;

import java.time.LocalDateTime;

/**
 * Data structure for database ranking (table scores)
 */
public record DataScores(LocalDateTime gameTime, String playerName, String guessWord, String missingLetters,
                         int timeSeconds) {
}