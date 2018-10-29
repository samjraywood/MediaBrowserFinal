package sample;

import java.util.List;

class PlayList {

    private String name;
    private List<MediaFile> mediaFileList;

    PlayList() {
    }

    String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    List<MediaFile> getMediaFileList() {
        return mediaFileList;
    }

    void setMediaFileList(List<MediaFile> mediaFileList) {
        this.mediaFileList = mediaFileList;
    }
}
