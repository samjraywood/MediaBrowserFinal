package sample;

import javafx.beans.property.SimpleStringProperty;

import java.util.List;

public class MediaFile {

    private SimpleStringProperty name = new SimpleStringProperty();
    private SimpleStringProperty filePath = new SimpleStringProperty();
    private SimpleStringProperty comment = new SimpleStringProperty();
    private MediaFileType mediaFileType;
    private List<Category> category;

    MediaFile() {
    }

    /**
     * Get name
     *
     * @return String
     */
    public String getName() {
        return name.get();
    }

    /**
     * Set name
     *
     * @param name name
     */
    public void setName(String name) {
        this.name.set(name);
    }

    /**
     * Get filePath
     *
     * @return String
     */
    public String getFilePath() {
        return filePath.get();
    }

    /**
     * Set filePath
     *
     * @param filePath filePath
     */
    public void setFilePath(String filePath) {
        this.filePath.set(filePath);
    }

    /**
     * Get mediaFileType
     *
     * @return MediaFileType
     */
    public MediaFileType getMediaFileType() {
        return mediaFileType;
    }

    /**
     * Set mediaFileType
     *
     * @param mediaFileType mediaFileType
     */
    public void setMediaFileType(MediaFileType mediaFileType) {
        this.mediaFileType = mediaFileType;
    }

    /**
     * Get comment
     *
     * @return String
     */
    public String getComment() {
        return comment.get();
    }

    /**
     * Set comment
     *
     * @param comment comment
     */
    public void setComment(String comment) {
        this.comment.set(comment);
    }

    /**
     * Get category
     *
     * @return List of Category
     */
    public List<Category> getCategory() {
        return category;
    }

    /**
     * Creates a string from a list of categories attached to the media file
     * @return String
     */
    public String getCategoryListDescription() {
        if (category != null && !category.isEmpty()) {
            final StringBuilder sb = new StringBuilder();
            category.forEach(category1 -> sb.append(category1.getDescription()).append(" "));
            return sb.toString();
        } else {
            return "";
        }
    }

    /**
     * Set category
     *
     * @param category List of Category
     */
    public void setCategory(List<Category> category) {
        this.category = category;
    }

    /**
     * nameProperty
     */
    public SimpleStringProperty nameProperty() {
        return name;
    }

    /**
     * filePathProperty
     */
    public SimpleStringProperty filePathProperty() {
        return filePath;
    }

    /**
     * commentProperty
     */
    public SimpleStringProperty commentProperty() {
        return comment;
    }

    @Override
    public String toString() {
        return "MediaFile{" +
                "name=" + name +
                ", filePath=" + filePath +
                ", comment=" + comment +
                ", mediaFileType=" + mediaFileType +
                ", category=" + category +
                '}';
    }
}
