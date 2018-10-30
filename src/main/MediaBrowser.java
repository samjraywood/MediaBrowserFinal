package main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import main.model.Category;
import main.model.MediaFile;
import main.model.MediaFileType;
import main.model.PlayList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class MediaBrowser {

    private static final String REMOVE_STRING = "Remove";

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
    private final MenuItem setCategory = new MenuItem("Set categories");
    private final ContextMenu mediaFileContextMenu = new ContextMenu(addToPlaylist, setCategory);

    private ObservableList<MediaFile> observableMediaFileList = FXCollections.observableArrayList(); // To be set to the media file table view
    private ObservableList<PlayList> observableListPlayList = FXCollections.observableArrayList(); // To be set to the play list table view
    private ObservableList<Category> observableCategoryList = FXCollections.observableArrayList(); // To be set to the play list table view

    private List<MediaFile> mainMediaFileList; // store all media files, which can then be added to multiple play lists

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

    }

    /**
     * Saves all Categories, Playlists and Mediafile data in Json format to a file.
     */
    private void saveAndExportData() {

    }

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
     * Create categories and add to list
     */
    private void initialiseCategories() {
        final Category category1 = new Category("Classical");
        final Category category2 = new Category("Rock");
        final Category category3 = new Category("Reggae");
        final Category category4 = new Category("Jazz");
        observableCategoryList.addAll(category1, category2, category3, category4);
        categoryTableView.setItems(observableCategoryList);
        categoryTableView.setContextMenu(categoryContextMenu);
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

        showAllButton.setOnAction(event -> showAllMediaFiles());
        showAllButton.setTooltip(new Tooltip("Show all media files"));
    }

    /**
     * Edit the selected category name
     *
     * - Get text from the text field to set as the new category name
     * - If it is not null or empty, create a new Category and replace the selected one
     */
    private void editCategory() {
        final Category selectedPlayCategory = categoryTableView.getSelectionModel().getSelectedItem();
        final String newCategoryName = editCategoryTextField.getText();

        if (newCategoryName != null && !newCategoryName.trim().isEmpty()) { // Validate new category name is not null or empty
            final Category updatedCategory = new Category(newCategoryName.trim());
            final int selectedCategoryIndex = observableCategoryList.indexOf(selectedPlayCategory); // Find selected category from list
            observableCategoryList.set(selectedCategoryIndex, updatedCategory); // Replace with the updated one
            editCategoryTextField.setText(null); // Set the text field back to null
        }

    }

    /**
     * Edit the selected play list's name
     *
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
        }
    }

    /**
     * Shows all media files from the main list
     */
    private void showAllMediaFiles() {
        observableMediaFileList.addAll(mainMediaFileList);
        mediaFileTableView.refresh();
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
        }
    }

    /**
     * Add new category
     */
    private void addNewCategory() {
        final String categoryName = editCategoryTextField.getText();
        if (categoryName != null && !categoryName.trim().isEmpty()) {
            final Category newCategory = new Category(categoryName.trim());
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
     * Sets the list of play lists available to the combo box
     */
    private void initialisePlaylists() {
        final PlayList playList1 = new PlayList();
        playList1.setName("First Playlist");
        playList1.setMediaFileList(getMediaFileList());
        observableListPlayList.add(playList1);
        playListTableView.setItems(observableListPlayList);
        playListTableView.setContextMenu(playListContextMenu);
        playListTableView.getSelectionModel().selectFirst();
        playListTitle.setText(observableListPlayList.get(0).getName());

        playListTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            final PlayList selectedPlaylist = playListTableView.getSelectionModel().getSelectedItem();
            if (selectedPlaylist != null) {
                playListTitle.setText(selectedPlaylist.getName());
                observableMediaFileList.setAll(selectedPlaylist.getMediaFileList() != null ? selectedPlaylist.getMediaFileList() : FXCollections.emptyObservableList());
                mediaFileTableView.refresh();
            }
        });
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
     * Creates list of media files to display in the table view
     */
    private void initialiseMediaFileTableView() {
        mediaFileTableView.setContextMenu(mediaFileContextMenu);
        observableMediaFileList.addAll(getMediaFileList());
        mediaFileTableView.setItems(observableMediaFileList);
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
