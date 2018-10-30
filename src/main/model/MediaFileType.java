package main.model;

public enum MediaFileType {

    MP3(".mp3"),
    MP4(".mp4"),
    WAV(".wav");

    private String fileSuffix;

    MediaFileType(String fileSuffix) {
        this.fileSuffix = fileSuffix;
    }

    public String getFileSuffix() {
        return fileSuffix;
    }
}
