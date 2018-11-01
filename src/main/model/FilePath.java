package main.model;

import java.io.Serializable;

public class FilePath implements Serializable {

    private String path;

    /**
     * Get path
     *
     * @return String - path
     */
    public String getPath() {
        return path;
    }

    /**
     * Set path
     *
     * @param path String path
     */
    public void setPath(final String path) {
        this.path = path;
    }

    /**
     * Just return the path directly for display in the table view
     */
    @Override
    public String toString() {
        return path;
    }
}
