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

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final Category category = (Category) o;

        return name != null ? name.equals(category.name) : category.name == null;
    }
}
