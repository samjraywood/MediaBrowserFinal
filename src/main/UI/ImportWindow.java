package main.UI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import main.model.MediaFile;
import main.model.MediaFileType;

import java.io.File;

import static main.model.MediaFileType.*;

public class ImportWindow {

    @FXML
    private Button importFileButton;
    @FXML
    private TextField filePathTextField;
    @FXML
    private Label importLabel;

    @FXML
    public void initialize() {
        importFileButton.setOnAction(event -> importFiles());
    }

    private void importFiles() {
        final File file = new File(filePathTextField.getText());
        // Check whether the file contains multiple files and handle all
        if (file.isDirectory()) {
            final File[] files = file.listFiles();
            if (files != null) {
                importFiles(files);
            } else {
                importLabel.setText("Unable to import any files");
            }
            // If it is a single file then handle
        } else if (file.exists()) {
            importFiles(new File[]{file});
        }
    }

    /**
     * This method imports all {@param files} into {@code selectedItem}
     *
     * @param files - {@link File[]}
     */
    private void importFiles(final File[] files) {
        Long nextId = MediaFileHolder.getNextId();
        int importedFiles = 0;
        for (final File file : files) {
            final String path = file.getPath();
            if (pathIsValidType(path)) {
                final MediaFile mediaFile = new MediaFile();
                mediaFile.setId(nextId);
                mediaFile.setName(file.getName());
                mediaFile.setFilePath(path);
                mediaFile.setMediaFileType(getFileTypeFromPath(path));
                MediaFileHolder.add(mediaFile);
                importedFiles++;
                nextId++;
            }
        }
        if (importedFiles > 0) {
            importLabel.setText("Successfully imported " + importedFiles + " files.");
        } else {
            importLabel.setText("Unable to import any files");
        }
    }

    /**
     * Checks the path is valid.
     *
     * Path must end in any of the MediaFileTypes to be imported.
     *
     * @param path path
     * @return true if valid
     */
    private boolean pathIsValidType(String path) {
        return getFileTypeFromPath(path) != null;
    }

    /**
     * Gets the MediaFileType from the file path
     * @param path path
     * @return MediaFilePath
     */
    private MediaFileType getFileTypeFromPath(final String path) {
        MediaFileType mediaFileType = null;
        if (path.contains(MP3.getFileSuffix())) {
            mediaFileType = MP3;
        } else if (path.contains(WAV.getFileSuffix())) {
            mediaFileType = WAV;
        } else if (path.contains(MP4.getFileSuffix())) {
            mediaFileType = MP4;
        } else if (path.contains(AAC.getFileSuffix())) {
            mediaFileType = AAC;
        }
        return mediaFileType;
    }
}
