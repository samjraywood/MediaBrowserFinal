package main.UI;

import main.model.MediaFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Holder for the MediaFiles. This is so it can be accessed from multiple places within the application.
 *
 * It is the main source of data for the application.
 */
public class MediaFileHolder {

    private static List<MediaFile> mediaFileList = new ArrayList<>();

    /**
     * Add multiple to the media file list
     * @param mediaFiles mediaFiles
     */
    public static void addAll(final List<MediaFile> mediaFiles) {
        mediaFileList.addAll(mediaFiles);
    }

    /**
     * Add to the media file list
     * @param mediaFile mediaFile
     */
    public static void add(final MediaFile mediaFile) {
        mediaFileList.add(mediaFile);
    }

    /**
     * Get the media file list
     */
    public static List<MediaFile> getMediaFileList() {
        return mediaFileList;
    }

    public static Long getNextId() {
        final List<MediaFile> mediaFilesSorted = new ArrayList<>(mediaFileList);
        mediaFilesSorted.sort(Comparator.comparing(MediaFile::getId));
        return mediaFilesSorted.get(mediaFilesSorted.size() - 1).getId() + 1;
    }

}
