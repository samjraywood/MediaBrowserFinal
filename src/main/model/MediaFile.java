package main.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

// We set this property in order to inform the ObjectMapper to ignore the method as it is only used from fxml via reflections
@JsonIgnoreProperties(value = {"categoryListDisplayString"})
public class MediaFile implements Serializable {

    private Long id;
    private SimpleObjectProperty<FileName> name = new SimpleObjectProperty<>();
    private SimpleObjectProperty<FilePath> filePath = new SimpleObjectProperty<>();
    private SimpleStringProperty comment = new SimpleStringProperty();
    private MediaFileType mediaFileType;
    private List<Category> categories;
    private Image image;

    public MediaFile() {
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
    public FileName getName() {
        return name.get();
    }

    /**
     * Set name
     *
     * @param name name
     */
    public void setName(final FileName name) {
        this.name.set(name);
    }

    /**
     * Get filePath
     *
     * @return String
     */
    public FilePath getFilePath() {
        return filePath.get();
    }

    /**
     * Set filePath
     *
     * @param filePath filePath
     */
    public void setFilePath(final FilePath filePath) {
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
     * Set categories
     *
     * @param categories List of Category
     */
    public void setCategories(final List<Category> categories) {
        this.categories = categories;
    }

    /**
     * Creates a string from a list of categories attached to the media file
     *
     * @return String
     *
     * - FXML uses this method via reflections - KEEP PUBLIC
     */
    public String getCategoryListDisplayString() {
        if (categories != null && !categories.isEmpty()) {
            final String sb = categories.stream()
                    .filter(Objects::nonNull)
                    .map(category1 -> category1.getCategoryName() + ", ")
                    .collect(Collectors.joining());
            final String categoryString = sb.trim();
            if (categoryString.endsWith(",")) {
                return categoryString.substring(0, categoryString.length() - 1);
            } else {
                return categoryString;
            }
        } else {
            return "";
        }
    }

    /**
     * Get image
     *
     * @return Image
     */
    public Image getImage() {
        return image;
    }

    /**
     * Set image
     *
     * @param image image
     */
    public void setImage(Image image) {
        this.image = image;
    }

    /**
     * nameProperty
     */
    public SimpleObjectProperty<FileName> nameProperty() {
        return name;
    }

    /**
     * filePathProperty
     */
    public SimpleObjectProperty<FilePath> filePathProperty() {
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
                ", image=" + image +
                '}';
    }
}
