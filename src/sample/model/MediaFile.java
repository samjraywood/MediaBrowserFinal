package sample.model;

import javafx.beans.property.SimpleStringProperty;

import java.util.List;

public class MediaFile {

    private Long id;
    private SimpleStringProperty name = new SimpleStringProperty();
    private SimpleStringProperty filePath = new SimpleStringProperty();
    private SimpleStringProperty comment = new SimpleStringProperty();
    private MediaFileType mediaFileType;
    private List<Category> categories;

    MediaFile() {
    }

    /**
     * Get id
     *
     * @return Long
     */
    public Long getId() {
        return id;
    }

    /**
     * Set id
     *
     * @param id id
     */
    public void setId(final Long id) {
        this.id = id;
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
    public void setName(final String name) {
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
    public void setFilePath(final String filePath) {
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
    public void setMediaFileType(final MediaFileType mediaFileType) {
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
    public void setComment(final String comment) {
        this.comment.set(comment);
    }

    /**
     * Get categories
     *
     * @return List of Category
     */
    public List<Category> getCategories() {
        return categories;
    }

    /**
     * Creates a string from a list of categories attached to the media file
     *
     * @return String
     */
    public String getCategoryListDescription() {
        if (categories != null && !categories.isEmpty()) {
            final StringBuilder sb = new StringBuilder();
            categories.forEach(category1 -> sb.append(category1.getDescription()).append(" "));
            return sb.toString();
        } else {
            return "";
        }
    }

    /**
     * Set categories
     *
     * @param categories List of Category
     */
    public void setCategories(final List<Category> categories) {
        this.categories = categories;
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
                "id=" + id +
                ", name=" + name +
                ", filePath=" + filePath +
                ", comment=" + comment +
                ", mediaFileType=" + mediaFileType +
                ", categories=" + categories +
                '}';
    }
}
