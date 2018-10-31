package main.UI;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import main.model.Category;
import main.model.MediaFile;
import main.model.MediaFileType;
import main.model.PlayList;

import java.io.*;
import java.util.*;

public class MediaBrowser extends VBox {

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
    private Button saveAllButton;
    @FXML
    private Button saveCommentButton;
    @FXML
    private TextField commentTextField;

    private final MenuItem removePlaylistMenuItem = new MenuItem(REMOVE_STRING);
    private final ContextMenu playListContextMenu = new ContextMenu(removePlaylistMenuItem);

    private final MenuItem removeCategoryMenuItem = new MenuItem(REMOVE_STRING);
    private final ContextMenu categoryContextMenu = new ContextMenu(removeCategoryMenuItem);

    private final Menu addToPlaylistMenu = new Menu("Add to playlist");
    private final MenuItem setCategoryMenuItem = new MenuItem("Set selected categories");
    private final MenuItem removeFromPlaylistMenuItem = new MenuItem("Remove from playlist");
    private final ContextMenu mediaFileContextMenu = new ContextMenu(addToPlaylistMenu, removeFromPlaylistMenuItem, setCategoryMenuItem);

    private ObservableList<MediaFile> observableMediaFileList = FXCollections.observableArrayList(); // To be set to the media file table view
    private ObservableList<PlayList> observableListPlayList = FXCollections.observableArrayList(); // To be set to the play list table view
    private ObservableList<Category> observableCategoryList = FXCollections.observableArrayList(); // To be set to the category table view

    private List<MediaFile> mainMediaFileList = new ArrayList<>(); // store all media files, which can then be added to multiple play lists

    @FXML
    public void initialize() {
        loadAllFiles();
        initialiseButtons();
        initialiseCategories();
        initialisePlaylists();
        initialiseMediaFileTableView();
    }

    /**
     * Initialise all buttons and menu items
     * <p>
     * - set on action
     * - set tool tips
     */
    private void initialiseButtons() {
        removePlaylistMenuItem.setOnAction(event -> removePlayList());
        removeCategoryMenuItem.setOnAction(event -> removeCategory());
        setCategoryMenuItem.setOnAction(event -> setSelectedCategory());
        mediaFileContextMenu.setOnShown(event -> createAddToPlaylistOptions());
        removeFromPlaylistMenuItem.setOnAction(event -> removeFromPlaylist());

        addPlayListButton.setOnAction(event -> addNewPlayList());
        addPlayListButton.setTooltip(new Tooltip("Add new Playlist"));

        editPlayListButton.setOnAction(event -> editPlayList());
        editPlayListButton.setTooltip(new Tooltip("Edit Playlist"));

        addCategoryButton.setOnAction(event -> addNewCategory());
        addCategoryButton.setTooltip(new Tooltip("Add new Category"));

        editCategoryButton.setOnAction(event -> editCategory());
        editCategoryButton.setTooltip(new Tooltip("Edit Category"));

        showAllButton.setOnAction(event -> showAllMediaFiles());
        showAllButton.setTooltip(new Tooltip("Show all media files"));

        saveAllButton.setOnAction(event -> saveAndExportData());
        saveAllButton.setTooltip(new Tooltip("Save all progress"));

        saveCommentButton.setOnAction(event -> saveCommentToMediaFile());
        saveCommentButton.setTooltip(new Tooltip("Save comment"));
    }

    /**
     * Removes selected media file from current playlist
     */
    private void removeFromPlaylist() {
        final PlayList currentPlaylist = playListTableView.getSelectionModel().getSelectedItem();
        final List<Long> mediaFileIdList = currentPlaylist.getMediaFileIdList();
        final MediaFile selectedMediaFile = getSelectedMediaFile();
        mediaFileIdList.removeIf(id -> selectedMediaFile != null && id.equals(selectedMediaFile.getId()));
        mediaFileTableView.refresh();
    }

    /**
     * Creates menu items to show all play list options to add to
     */
    private void createAddToPlaylistOptions() {
        final List<MenuItem> playlistMenuItems = new ArrayList<>();
        for (PlayList playList : observableListPlayList) {
            final MenuItem menuItem = new MenuItem(playList.getName());
            menuItem.setOnAction(event -> addToPlayList(playList.getName()));
            playlistMenuItems.add(menuItem);
        }
        addToPlaylistMenu.getItems().setAll(playlistMenuItems);

        removeFromPlaylistMenuItem.setDisable(playListTableView.getSelectionModel().getSelectedItem() == null);
    }

    /**
     * Finds selected playlist and adds the selected media file to its list.
     *
     * @param playlistName playlistName
     */
    private void addToPlayList(final String playlistName) {
        for (final PlayList playList : observableListPlayList) {
            if (playList.getName().equals(playlistName)) {
                playList.getMediaFileIdList().add(getSelectedMediaFile().getId());
                mediaFileContextMenu.hide();
            }
        }
    }

    /**
     * Gets current selected MediaFile from the table
     * @return MediaFile
     */
    private MediaFile getSelectedMediaFile() {
        return mediaFileTableView.getSelectionModel().getSelectedItem();
    }

    /**
     * Saves comment to selected media file. Takes text from comment text field and over writes.
     */
    private void saveCommentToMediaFile() {
        final MediaFile selectedMediaFile = getSelectedMediaFile();
        final String updatedComment = commentTextField.getText();

        if (selectedMediaFile != null) {
            final MediaFile updatedMediaFile = new MediaFile();
            updatedMediaFile.setId(selectedMediaFile.getId());
            updatedMediaFile.setName(selectedMediaFile.getName());
            updatedMediaFile.setComment(updatedComment != null ? updatedComment : "");
            updatedMediaFile.setFilePath(selectedMediaFile.getFilePath());
            updatedMediaFile.setMediaFileType(selectedMediaFile.getMediaFileType());
            updatedMediaFile.setCategories(selectedMediaFile.getCategories());

            final int mediaFileIndex = observableMediaFileList.indexOf(selectedMediaFile);
            observableMediaFileList.set(mediaFileIndex, updatedMediaFile);

            updateMainList(selectedMediaFile, updatedMediaFile);
        }
        commentTextField.setText(null);
    }

    /**
     * Create categories and add to list
     */
    private void initialiseCategories() {
//        createCategories();
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
     * TEMP
     */
    private void createCategories() {
        final Category category1 = new Category();
        category1.setName("Classical");
        final Category category2 = new Category();
        category2.setName("Rock");
        final Category category3 = new Category();
        category3.setName("Reggae");
        final Category category4 = new Category();
        category4.setName("Jazz");
        observableCategoryList.addAll(category1, category2, category3, category4);
    }

    /**
     * Sets the list of play lists available to the combo box
     */
    private void initialisePlaylists() {
//        createPlaylist();
        playListTableView.setItems(observableListPlayList);
        playListTableView.setContextMenu(playListContextMenu);

        playListTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                playListTitle.setText(newValue.getName());
                editPlayListTextField.setText(newValue.getName());
                observableMediaFileList.setAll(getMediaFilesById(newValue.getMediaFileIdList()));
                mediaFileTableView.refresh();
            }
        });

        if (!observableListPlayList.isEmpty()) {
            playListTitle.setText(observableListPlayList.get(0).getName());
        }
    }

    /**
     * TEMP TODO
     */
    private void createPlaylist() {
        final PlayList playList1 = new PlayList();
        playList1.setName("First Playlist");
        final Long[] fileIdList = {1L, 2L, 3L, 4L};
        playList1.setMediaFileIdList(Arrays.asList(fileIdList));
        observableListPlayList.add(playList1);
    }

    /**
     * Takes any Media File ids that are stored with a playlist and returns the corresponding Media File
     *
     * @param mediaFileIdList mediaFileIdList
     * @return List of Media File
     */
    private List<MediaFile> getMediaFilesById(final List<Long> mediaFileIdList) {
        if (mediaFileIdList != null && !mediaFileIdList.isEmpty()) {
            final List<MediaFile> mediaFiles = new ArrayList<>();
            mediaFileIdList
                    .stream()
                    .map(id -> mainMediaFileList.stream().filter(mediaFile -> mediaFile.getId().equals(id)).findFirst())
                    .forEach(optional -> optional.ifPresent(mediaFiles::add));
            return mediaFiles;

        }
        return Collections.emptyList();
    }

    /**
     * Creates list of media files to display in the table view
     */
    private void initialiseMediaFileTableView() {
        mediaFileTableView.setContextMenu(mediaFileContextMenu);
        showAllMediaFiles();
        mediaFileTableView.setItems(observableMediaFileList);
    }

    /**
     * Loads all files from the text file.
     * <p>
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
     * Temp - add a list of all media files to a main list TODO
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
                final List<Category> categories = new ObjectMapper().readValue(stringBuilder.toString(), new TypeReference<List<Category>>() {
                });
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
                final List<MediaFile> mediaFiles = new ObjectMapper().readValue(stringBuilder.toString(), new TypeReference<List<MediaFile>>() {
                });
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
                final List<PlayList> playLists = new ObjectMapper().readValue(stringBuilder.toString(), new TypeReference<List<PlayList>>() {
                });
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
        final String initialCategoryName = selectedPlayCategory.getName();
        final String newCategoryName = editCategoryTextField.getText();

        if (newCategoryName != null && !newCategoryName.trim().isEmpty()) { // Validate new category name is not null or empty
            final Category updatedCategory = new Category();
            updatedCategory.setName(newCategoryName.trim());
            final int selectedCategoryIndex = observableCategoryList.indexOf(selectedPlayCategory); // Find selected category from list
            observableCategoryList.set(selectedCategoryIndex, updatedCategory); // Replace with the updated one
            editCategoryTextField.setText(null); // Set the text field back to null

            updateAllMediaFileCategories(initialCategoryName, updatedCategory, mainMediaFileList);
            updateAllMediaFileCategories(initialCategoryName, updatedCategory, observableMediaFileList);
            mediaFileTableView.refresh();
        }
    }

    /**
     * Check all media files to see if they need to update the category when editing the name.
     *
     * @param initialCategoryName initialCategoryName
     * @param updatedCategory     updatedCategory
     */
    private void updateAllMediaFileCategories(final String initialCategoryName,
                                              final Category updatedCategory,
                                              final Collection<MediaFile> mediaFileList) {
        for (final MediaFile mediaFile : mediaFileList) {
            final List<Category> categories = mediaFile.getCategories();
            for (final Category category : categories) {
                if (category.getName().equals(initialCategoryName)) {
                    final int categoryIndex = categories.indexOf(category);
                    categories.set(categoryIndex, updatedCategory);
                }
            }
        }
    }

    /**
     * Adds the selected category or categories from the category table to the selected MediaFile
     * <p>
     * - MultiSelect enabled on category grid
     */
    private void setSelectedCategory() {
        final MediaFile selectedMediaFile = getSelectedMediaFile();
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
    }

    /**
     * updates main media file list with updated object
     *
     * @param selectedMediaFile selectedMediaFile
     * @param updatedMediaFile  updatedMediaFile
     */
    private void updateMainList(final MediaFile selectedMediaFile, final MediaFile updatedMediaFile) {
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
            updatedPlaylist.setMediaFileIdList(selectedPlaylist.getMediaFileIdList()); // Copy the list of files from the selected playlist
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
            playListTableView.getSelectionModel().clearSelection();
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
            removeCategoryFromAllMediaFiles(selectedCategory.getName());
        }
    }

    /**
     * Removes the selected deleted category from all media files that reference it.
     *
     * @param initialCategoryName initialCategoryName
     */
    private void removeCategoryFromAllMediaFiles(final String initialCategoryName) {
        if (initialCategoryName != null) {
            for (final MediaFile mediaFile : mainMediaFileList) {
                final List<Category> categories = mediaFile.getCategories();
                categories.removeIf(category1 -> category1.getName().equals(initialCategoryName));
            }
            for (final MediaFile mediaFile : observableMediaFileList) {
                final List<Category> categories = mediaFile.getCategories();
                categories.removeIf(category1 -> category1.getName().equals(initialCategoryName));
            }
        }
        mediaFileTableView.refresh();
    }

    /**
     * Add new category
     */
    private void addNewCategory() {
        final String categoryName = editCategoryTextField.getText();
        if (categoryName != null && !categoryName.trim().isEmpty()) {
            final Category newCategory = new Category();
            newCategory.setName(categoryName.trim());
            if (observableCategoryList.contains(newCategory)) {
                showDialog("Error adding new Category", "Name already exists");
            } else {
                observableCategoryList.add(newCategory);
                categoryTableView.getSelectionModel().select(newCategory);
            }
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
            if (observableListPlayList.contains(newPlayList)) {
                showDialog("Error adding new playlist", "Name already exists");
            } else {
                observableListPlayList.add(newPlayList);
                playListTableView.getSelectionModel().select(newPlayList);
            }
        }
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
     * Gets a list of media files. TODO remove
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

    /**
     * Shows an alert to the user
     *
     * @param headerMessage  headerMessage
     * @param contentMessage contentMessage
     */
    private void showDialog(final String headerMessage, final String contentMessage) {
        final Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(headerMessage);
        alert.setContentText(contentMessage);
        alert.showAndWait();
    }
}
