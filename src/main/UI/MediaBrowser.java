package main.UI;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import main.model.Category;
import main.model.MediaFile;
import main.model.MediaFileType;
import main.model.PlayList;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class MediaBrowser {

    private static final String REMOVE_STRING = "Remove";
    private static final String PLAYLIST_FILE_JSON = "playlistFile.json";
    private static final String CATEGORY_FILE_JSON = "categoryFile.json";
    private static final String MEDIA_FILE_JSON = "mediaFile.json";

    @FXML
    private TableView<PlayList> playListTableView;
    @FXML
    private TableView<MediaFile> mediaFileTableView;
    @FXML
    private TableView<Category> categoryTableView;
    @FXML
    private Label playListTitle;
    @FXML
    private Button addPlayListButton;
    @FXML
    private Button editPlayListButton;
    @FXML
    private TextField editPlayListTextField;
    @FXML
    private Button addCategoryButton;
    @FXML
    private Button editCategoryButton;
    @FXML
    private TextField editCategoryTextField;
    @FXML
    private Button showAllButton;
    @FXML
    private Button saveButton;
    @FXML
    private TextField searchTextField;

    private final MenuItem removePlaylist = new MenuItem(REMOVE_STRING);
    private final ContextMenu playListContextMenu = new ContextMenu(removePlaylist);

    private final MenuItem removeCategory = new MenuItem(REMOVE_STRING);
    private final ContextMenu categoryContextMenu = new ContextMenu(removeCategory);

    private final MenuItem addToPlaylist = new MenuItem("Add to playlist");
    private final MenuItem setCategory = new MenuItem("Set selected categories");
    private final ContextMenu mediaFileContextMenu = new ContextMenu(addToPlaylist, setCategory);

    private ObservableList<MediaFile> observableMediaFileList = FXCollections.observableArrayList(); // To be set to the media file table view
    private ObservableList<PlayList> observableListPlayList = FXCollections.observableArrayList(); // To be set to the play list table view
    private ObservableList<Category> observableCategoryList = FXCollections.observableArrayList(); // To be set to the play list table view

    private List<MediaFile> mainMediaFileList = new ArrayList<>(); // store all media files, which can then be added to multiple play lists

    @FXML
    public void initialize() {
        loadAllFiles();
        initialiseButtons();
        initialiseCategories();
        initialisePlaylists();
        initialiseSearchField();
        initialiseMediaFileTableView();
    }

    /**
     * Initialise all buttons
     *
     * - set on action
     * - set tool tips
     */
    private void initialiseButtons() {
        removePlaylist.setOnAction(event -> removePlayList());
        removeCategory.setOnAction(event -> removeCategory());

        addPlayListButton.setOnAction(event -> addNewPlayList());
        addPlayListButton.setTooltip(new Tooltip("Add new Playlist"));

        editPlayListButton.setOnAction(event -> editPlayList());
        editPlayListButton.setTooltip(new Tooltip("Edit Playlist"));

        addCategoryButton.setOnAction(event -> addNewCategory());
        addCategoryButton.setTooltip(new Tooltip("Add new Category"));

        editCategoryButton.setOnAction(event -> editCategory());
        editCategoryButton.setTooltip(new Tooltip("Edit Category"));

        setCategory.setOnAction(event -> setSelectedCategory());

        showAllButton.setOnAction(event -> showAllMediaFiles());
        showAllButton.setTooltip(new Tooltip("Show all media files"));

        saveButton.setOnAction(event -> saveAndExportData());
    }

    /**
     * Create categories and add to list
     */
    private void initialiseCategories() {
//        final Category category1 = new Category();
//        category1.setName("Classical");
//        final Category category2 = new Category();
//        category2.setName("Rock");
//        final Category category3 = new Category();
//        category3.setName("Reggae");
//        final Category category4 = new Category();
//        category4.setName("Jazz");
//        observableCategoryList.addAll(category1, category2, category3, category4);
        categoryTableView.setItems(observableCategoryList);
        categoryTableView.setContextMenu(categoryContextMenu);
        categoryTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        categoryTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                editCategoryTextField.setText(newValue.getName());
            }
        });
    }

    /**
     * Sets the list of play lists available to the combo box
     */
    private void initialisePlaylists() {
//        final PlayList playList1 = new PlayList();
//        playList1.setName("First Playlist");
//        playList1.setMediaFileList(getMediaFileList());
//        observableListPlayList.add(playList1);
        playListTableView.setItems(observableListPlayList);
        playListTableView.setContextMenu(playListContextMenu);

        playListTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                playListTitle.setText(newValue.getName());
                editPlayListTextField.setText(newValue.getName());
                observableMediaFileList.setAll(newValue.getMediaFileList() != null ? newValue.getMediaFileList() : FXCollections.emptyObservableList());
                mediaFileTableView.refresh();
            }
        });

        playListTableView.getSelectionModel().selectFirst();

        if (!observableListPlayList.isEmpty()) {
            playListTitle.setText(observableListPlayList.get(0).getName());
        }

    }

    /**
     * Creates list of media files to display in the table view
     */
    private void initialiseMediaFileTableView() {
        mediaFileTableView.setContextMenu(mediaFileContextMenu);
//        observableMediaFileList.addAll(getMediaFileList());
        mediaFileTableView.setItems(observableMediaFileList);
    }

    /**
     * Loads all files from the text file.
     *
     * - Convert from json string to java object
     * - Build Categories
     * - Main list of media file
     * - Main list of play list
     * - Build play lists from main list
     */
    private void loadAllFiles() {
        loadCategoriesFromFile();
        loadPlaylistsFromFile();
//        loadMediaFilesTemporary();
        loadMediaFilesFromFile();
    }

    /**
     * Temp - add a list of all media files to a main list
     */
    private void loadMediaFilesTemporary() {
        mainMediaFileList.addAll(getMediaFileList());
    }

    /**
     * Loads all categories from a file stored as JSON
     */
    private void loadCategoriesFromFile() {
        final File file = new File(CATEGORY_FILE_JSON);
        if (file.exists()) {
            try (final BufferedReader reader = new BufferedReader(new FileReader(file))) {
                final StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                final List<Category> categories = new ObjectMapper().readValue(stringBuilder.toString(), new TypeReference<List<Category>>() {});
                observableCategoryList.setAll(categories);
            } catch (IOException e) {
                System.out.println("Error: Could not load categories from file" + e);
            }
        }
    }

    /**
     * Loads all categories from a file stored as JSON
     */
    private void loadMediaFilesFromFile() {
        final File file = new File(MEDIA_FILE_JSON);
        if (file.exists()) {
            try (final BufferedReader reader = new BufferedReader(new FileReader(file))) {
                final StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                final List<MediaFile> mediaFiles = new ObjectMapper().readValue(stringBuilder.toString(), new TypeReference<List<MediaFile>>() {});
                mainMediaFileList.addAll(mediaFiles);
            } catch (IOException e) {
                System.out.println("Error: Could not load categories from file" + e);
            }
        }
    }

    /**
     * Loads all playlists from a file stored as JSON
     */
    private void loadPlaylistsFromFile() {
        final File file = new File(PLAYLIST_FILE_JSON);
        if (file.exists()) {
            try (final BufferedReader reader = new BufferedReader(new FileReader(file))) {
                final StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                final List<PlayList> playLists = new ObjectMapper().readValue(stringBuilder.toString(), new TypeReference<List<PlayList>>() {});
                observableListPlayList.setAll(playLists);
            } catch (IOException e) {
                System.out.println("Error: Could not load play lists from file" + e);
            }
        }
    }

    /**
     * Saves all Categories, Playlists and Mediafile data in Json format to a file.
     */
    private void saveAndExportData() {
        final ObjectMapper mapper = new ObjectMapper();
        final ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());

        final List<PlayList> playLists = new ArrayList<>(observableListPlayList);
        final List<Category> categories = new ArrayList<>(observableCategoryList);
        final List<MediaFile> mediaFiles = new ArrayList<>(mainMediaFileList);

        try {
            writer.writeValue(new File(PLAYLIST_FILE_JSON), playLists);
            writer.writeValue(new File(CATEGORY_FILE_JSON), categories);
            writer.writeValue(new File(MEDIA_FILE_JSON), mediaFiles);
        } catch (IOException e) {
            System.out.println("Error: Could not write to file" + e);
        }
    }

    /**
     * Edit the selected category name
     * <p>
     * - Get text from the text field to set as the new category name
     * - If it is not null or empty, create a new Category and replace the selected one
     */
    private void editCategory() {
        final Category selectedPlayCategory = categoryTableView.getSelectionModel().getSelectedItem();
        final String newCategoryName = editCategoryTextField.getText();

        if (newCategoryName != null && !newCategoryName.trim().isEmpty()) { // Validate new category name is not null or empty
            final Category updatedCategory = new Category();
            updatedCategory.setName(newCategoryName.trim());
            final int selectedCategoryIndex = observableCategoryList.indexOf(selectedPlayCategory); // Find selected category from list
            observableCategoryList.set(selectedCategoryIndex, updatedCategory); // Replace with the updated one
            editCategoryTextField.setText(null); // Set the text field back to null
        }
    }

    /**
     * Adds the selected category or categories from the category table to the selected MediaFile
     *
     * - MultiSelect enabled on category grid
     */
    private void setSelectedCategory() {
        final MediaFile selectedMediaFile = mediaFileTableView.getSelectionModel().getSelectedItem();
        final List<Category> selectedCategories = categoryTableView.getSelectionModel().getSelectedItems();

        final MediaFile updatedMediaFile = new MediaFile();
        updatedMediaFile.setId(selectedMediaFile.getId());
        updatedMediaFile.setName(selectedMediaFile.getName());
        updatedMediaFile.setComment(selectedMediaFile.getComment());
        updatedMediaFile.setFilePath(selectedMediaFile.getFilePath());
        updatedMediaFile.setMediaFileType(selectedMediaFile.getMediaFileType());
        updatedMediaFile.setCategories(selectedCategories != null && !selectedCategories.isEmpty() ? selectedCategories : null);

        final int mediaFileIndex = observableMediaFileList.indexOf(selectedMediaFile);
        observableMediaFileList.set(mediaFileIndex, updatedMediaFile);

        updateMainList(selectedMediaFile, updatedMediaFile);

        // need to update all playlists here as well if the selected media file exists on any

    }

    /**
     * updates main media file list with updated object
     * @param selectedMediaFile selectedMediaFile
     * @param updatedMediaFile updatedMediaFile
     */
    private void updateMainList(MediaFile selectedMediaFile, MediaFile updatedMediaFile) {
        int mainListIndex = 0;
        if (mainMediaFileList != null && !mainMediaFileList.isEmpty()) {
            for (final MediaFile mediaFile : mainMediaFileList) {
                if (mediaFile.getId().equals(selectedMediaFile.getId())) {
                    mainListIndex = mainMediaFileList.indexOf(mediaFile);
                }
            }
            mainMediaFileList.set(mainListIndex, updatedMediaFile);
        }
    }

    /**
     * Edit the selected play list's name
     * <p>
     * - Get text from the text field to set as the new Playlist name
     * - If it is not null or empty, create a new Playlist, copy the list of media files attached and replace the selected one
     */
    private void editPlayList() {
        final PlayList selectedPlaylist = playListTableView.getSelectionModel().getSelectedItem();
        final String newPlaylistName = editPlayListTextField.getText();

        if (newPlaylistName != null && !newPlaylistName.trim().isEmpty()) { // Validate the new playlist name is not null or empty
            final PlayList updatedPlaylist = new PlayList();
            updatedPlaylist.setName(newPlaylistName);
            updatedPlaylist.setMediaFileList(selectedPlaylist.getMediaFileList()); // Copy the list of files from the selected playlist
            final int selectedPlaylistIndex = observableListPlayList.indexOf(selectedPlaylist); // Find selected playlist from list
            observableListPlayList.set(selectedPlaylistIndex, updatedPlaylist); // Replace with the updated one
            playListTableView.getSelectionModel().select(updatedPlaylist);
        }
    }

    /**
     * Shows all media files from the main list
     */
    private void showAllMediaFiles() {
        if (mainMediaFileList != null && !mainMediaFileList.isEmpty()) {
            observableMediaFileList.setAll(mainMediaFileList);
            playListTitle.setText("All Files");
            mediaFileTableView.refresh();
        }
    }

    /**
     * Remove selected play list
     */
    private void removePlayList() {
        final PlayList selectedPlayList = playListTableView.getSelectionModel().getSelectedItem();
        if (selectedPlayList != null) {
            observableListPlayList.remove(selectedPlayList);
        }
    }

    /**
     * Remove selected category
     */
    private void removeCategory() {
        final Category selectedCategory = categoryTableView.getSelectionModel().getSelectedItem();
        if (selectedCategory != null) {
            observableCategoryList.remove(selectedCategory);

            // TODO if category is assigned to mediafile when removed then remove from media file
        }
    }

    /**
     * Add new category
     */
    private void addNewCategory() {
        final String categoryName = editCategoryTextField.getText();
        if (categoryName != null && !categoryName.trim().isEmpty()) {
            final Category newCategory = new Category();
            newCategory.setName(categoryName.trim());
            observableCategoryList.add(newCategory);
            categoryTableView.getSelectionModel().select(newCategory);
        }
    }

    /**
     * Add a new playlist to the table
     */
    private void addNewPlayList() {
        final String playListName = editPlayListTextField.getText();
        if (playListName != null && !playListName.trim().isEmpty()) {
            final PlayList newPlayList = new PlayList();
            newPlayList.setName(playListName.trim());
            observableListPlayList.add(newPlayList);
            playListTableView.getSelectionModel().select(newPlayList);
        }
    }

    /**
     * Sets listeners to the search field to filter results.
     */
    private void initialiseSearchField() {
        // TODO add filtering on search
    }

    /**
     * Finds a category from the main list based on the searchName passed in if present.
     *
     * @param searchName searchName
     * @return Category
     */
    private Category getCategoryFromList(final String searchName) {
        final Optional<Category> optional = observableCategoryList
                .stream()
                .filter(category -> category.getName().equalsIgnoreCase(searchName))
                .findFirst();
        return optional.orElse(null);
    }

    /**
     * Gets a list of media files.
     */
    private List<MediaFile> getMediaFileList() {
        final List<MediaFile> mediaFileList = new ArrayList<>();

        final MediaFile mediaFile1 = new MediaFile();
        mediaFile1.setId(1L);
        mediaFile1.setName("Three Little Birds");
        mediaFile1.setCategories(Collections.singletonList(getCategoryFromList("Reggae")));
        mediaFile1.setComment("Bob Marley");
        mediaFile1.setFilePath("threelittlebirds.mp3");
        mediaFile1.setMediaFileType(MediaFileType.MP3);
        mediaFileList.add(mediaFile1);

        final MediaFile mediaFile2 = new MediaFile();
        mediaFile2.setId(2L);
        mediaFile2.setName("Back In Black");
        mediaFile2.setCategories(Collections.singletonList(getCategoryFromList("Rock")));
        mediaFile2.setComment("AC/DC");
        mediaFile2.setFilePath("backinblack.mp3");
        mediaFile2.setMediaFileType(MediaFileType.MP3);
        mediaFileList.add(mediaFile2);

        final MediaFile mediaFile3 = new MediaFile();
        mediaFile3.setId(3L);
        mediaFile3.setName("Jazz Song");
        mediaFile3.setCategories(Collections.singletonList(getCategoryFromList("Jazz")));
        mediaFile3.setComment("No comment");
        mediaFile3.setFilePath("jazzsong.mp3");
        mediaFile3.setMediaFileType(MediaFileType.MP3);
        mediaFileList.add(mediaFile3);

        final MediaFile mediaFile4 = new MediaFile();
        mediaFile4.setId(4L);
        mediaFile4.setName("Classical Song");
        mediaFile4.setCategories(Collections.singletonList(getCategoryFromList("Classical")));
        mediaFile4.setComment("classical");
        mediaFile4.setFilePath("classicalsong.mp3");
        mediaFile4.setMediaFileType(MediaFileType.MP3);
        mediaFileList.add(mediaFile4);

        return mediaFileList;
    }
}
