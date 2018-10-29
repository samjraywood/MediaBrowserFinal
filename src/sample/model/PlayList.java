package sample.model;

import javafx.beans.property.SimpleStringProperty;
import sample.model.MediaFile;

import java.util.List;

public class PlayList {

    private SimpleStringProperty name = new SimpleStringProperty();
    private List<MediaFile> mediaFileList;

    public PlayList() {
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public List<MediaFile> getMediaFileList() {
        return mediaFileList;
    }

    public void setMediaFileList(List<MediaFile> mediaFileList) {
        this.mediaFileList = mediaFileList;
    }
}
