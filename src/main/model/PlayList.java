package main.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PlayList implements Serializable {

    private String name;
    private List<Long> mediaFileIdList = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public List<Long> getMediaFileIdList() {
        return mediaFileIdList;
    }

    public void setMediaFileIdList(final List<Long> mediaFileIdList) {
        this.mediaFileIdList = mediaFileIdList;
    }

    @Override
    public String toString() {
        return "PlayList{" +
                "name='" + name + '\'' +
                ", mediaFileIdList=" + mediaFileIdList +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final PlayList playList = (PlayList) o;

        return name != null ? name.equals(playList.name) : playList.name == null;
    }
}
