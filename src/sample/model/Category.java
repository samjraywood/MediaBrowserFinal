package sample.model;

public enum Category {

    CLASSICAL("Classical"),
    ROCK("Rock"),
    REGGAE("Reggae"),
    JAZZ("Jazz");

    private String description;

    /**
     * Constructor
     */
    Category(String description) {
        this.description = description;
    }

    /**
     * Get category description
     *
     * @return String
     */
    public String getDescription() {
        return description;
    }
}
