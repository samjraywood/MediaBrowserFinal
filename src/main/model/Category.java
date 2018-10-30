package main.model;

import javafx.beans.property.SimpleStringProperty;

public class Category {

    private SimpleStringProperty name = new SimpleStringProperty();

    /**
     * Constructor
     */
    public Category (final String name) {
        this.name.set(name);
    }

    /**
     * Get category description
     *
     * @return String
     */
    public String getName() {
        return name.get();
    }

    /**
     * Set category name
     * @param name
     */
    public void setName(final String name) {
        this.name.set(name);
    }
}
