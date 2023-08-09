package models.datastructures;

/**
 * Klass mis sisaldab tabeli words sisu
 */
public class DataWords {
    /**
     * Id
     */
    private final int id;
    /**
     * Word
     */
    private final String word;
    /**
     * Category
     */
    private final String category;

    /**
     * Constructor
     * @param id id
     * @param word word
     * @param category category
     */
    public DataWords(int id, String word, String category) {
        this.id = id;
        this.word = word;
        this.category = category;
    }
    /**
     * Returns word
     * @return String
     */
    public String getWord() {
        return word;
    }
    /**
     * Returns Category
     * @return String
     */
    public String getCategory() {
        return category;
    }
}
