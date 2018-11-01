package main.model;

import java.io.Serializable;

public class Category implements Serializable {

    private String categoryName;

    /**
     * Get category description
     *
     * @return String
     */
    public String getCategoryName() {
        return categoryName;
    }

    /**
     * Set category categoryName
     * @param categoryName categoryName
     */
    public void setCategoryName(final String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public String toString() {
        return categoryName;
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

        return categoryName != null ? categoryName.equals(category.categoryName) : category.categoryName == null;
    }
}
