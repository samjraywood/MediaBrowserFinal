package main.model;

import java.io.Serializable;

public class FileName implements Serializable {

    private String fileName;

    /**
     * Get fileName
     *
     * @return String - fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Set fileName
     * @param fileName String - fileName
     */
    public void setFileName(final String fileName) {
        this.fileName = fileName;
    }

    /**
     * Just return the fileName directly for display in the table view
     */
    @Override
    public String toString() {
        return fileName;
    }
}
