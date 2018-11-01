package main.model;

/**
 * Enum for MediaFileType
 *
 * - These are the only valid file types for this application
 */
public enum MediaFileType {

    MP3(".mp3"),
    MP4(".mp4"),
    AAC(".aac"),
    WAV(".wav");

    private String fileSuffix;

    MediaFileType(String fileSuffix) {
        this.fileSuffix = fileSuffix;
    }

    public String getFileSuffix() {
        return fileSuffix;
    }
}
