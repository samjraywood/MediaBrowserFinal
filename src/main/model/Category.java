package main.model;

import java.io.Serializable;

public class Category implements Serializable {

    private String name;

    /**
     * Get category description
     *
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * Set category name
     * @param name name
     */
    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
