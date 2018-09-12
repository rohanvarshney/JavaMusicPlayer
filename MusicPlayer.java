import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.Media;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.awt.Desktop;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.ObservableMap;

/**
  *Class to create an MP3 Player application.
  * @author Rohan Varshney
  */

public class MusicPlayer extends Application {

    private BorderPane pane;
    private ImageView imageView;
    private String pwd = System.getProperty("user.dir");
    private List<File> songs = getSongs(pwd);
    private Media[] actualSongs = fileToSongs(songs);
    private int numberOfSongs = songs.size();
    private TableView table = new TableView();
    private Button playButton = new Button("Play");
    private Button pauseButton = new Button("Pause");
    private Button searchButton = new Button("Search Songs");
    private Button showButton = new Button("Show all Songs");
    private TableColumn<Integer, String> fileNameCol =
        new TableColumn("File Name");
    private TableColumn<Integer, String> artistCol =
        new TableColumn("Artist");
    private TableColumn<Integer, String> titleCol = new TableColumn("Title");
    private TableColumn<Integer, String> albumCol = new TableColumn("Album");
    private Stage secondary = new Stage();
    private String searchTerm = "";
    private String searchBy = "";
    private Media m;
    private MediaPlayer player;

    @Override
    public void start(Stage primaryStage) {
        pane = new BorderPane();
        pane.setBottom(getHBox());
        pane.setLeft(getVBox());
        pane.setMinSize(800, 420);

        Scene scene = new Scene(pane);
        primaryStage.setTitle("Music Player"); // Set the stage title
        primaryStage.setScene(scene); // Place the scene in the stage
        primaryStage.show(); // Display the stage
    }

    private ImageView getImage(String imageName) {
        imageView = new ImageView(new Image(imageName));
        return imageView;
    }

    private List<File> getSongs(String pWD) {
        List<File> songLocations = new ArrayList<>();

        File directory = new File(pWD);
        File[] fList = directory.listFiles();

        for (File file : fList) {
            String name = file.toString();
            //System.out.println(name);

            String ext = name.substring(name.length() - 3, name.length());

            if (ext.equals("mp3")) {
                System.out.println(name);
                songLocations.add(file);
            }
        }

        return songLocations;
    }

    private Media[] fileToSongs(List<File> lf) {
        Media[] media = new Media[lf.size()];
        for (int x = 0; x < lf.size(); x++) {
            String fileName = songs.get(x).getName();
            String path = "file:///" + System.getProperty("user.dir")
                .replace('\\', '/') + "/" + fileName;
            System.out.println(path);
            m = new Media(path);
            media[x] = m;
        }

        return media;
    }

    private String modifyArtist(String artist) {
        if (artist.contains(", raw meta")) {
            artist = artist.substring(0, artist.indexOf(", raw meta"));
        }
        if (artist.contains(", year=")) {
            artist = artist.substring(0, artist.indexOf(", year="));
        }
        if (artist.contains(", image=")) {
            artist = artist.substring(0, artist.indexOf(", image="));
        }
        if (artist.contains(", artist=")) {
            artist = artist.substring(0, artist.indexOf(", artist="));
        }
        if (artist.contains(", text-")) {
            artist = artist.substring(0, artist.indexOf(", text-"));
        }
        return artist;
    }

    private String modifyAlbum(String album) {
        if (album.contains(", comment")) {
            album = album.substring(0, album.indexOf(", comment"));
        }
        if (album.contains(", composer")) {
            album = album.substring(0, album.indexOf(", composer="));
        }
        if (album.contains(", title=")) {
            album = album.substring(0, album.indexOf(", title="));
        }
        if (album.contains(", text-")) {
            album = album.substring(0, album.indexOf(", text-"));
        }
        if (album.contains(", genre=")) {
            album = album.substring(0, album.indexOf(", genre="));
        }
        return album;
    }

    private String modifyTitle(String title) {
        if (title.contains(", track")) {
            title = title.substring(0, title.indexOf(", track"));
        }
        if (title.contains(", text-")) {
            title = title.substring(0, title.indexOf(", text-"));
        }
        return title;
    }

    private void modifyButtonsAndText(Button b1, Button b2,
        Button b3, Button b4, TextField tf) {
        b1.setLayoutX(20);
        b1.setLayoutY(20);
        b2.setLayoutX(20);
        b2.setLayoutY(50);
        b3.setLayoutX(20);
        b3.setLayoutY(80);
        b4.setLayoutX(20);
        b4.setLayoutY(110);
        tf.setLayoutX(90);
        tf.setLayoutY(55);
        tf.setMinHeight(50);
        tf.setMinWidth(50);
    }
    /**
    *Performs actions when the "Search Songs" button is pressed.
    *
    */
    public void pressSearchSongsButton() {
        searchButton.setOnAction(
            event -> {
                System.out.println("pressed Search Songs");
                secondary.setTitle("Search Songs");
                Text t = new Text("Input what term you want to search by: ");
                t.setLayoutX(15);
                t.setLayoutY(18);
                Button b1 = new Button("File Name");
                Button b2 = new Button("Artist");
                Button b3 = new Button("Title");
                Button b4 = new Button("Album");
                TextField tf = new TextField();
                modifyButtonsAndText(b1, b2, b3, b4, tf);


                Scene scene = new Scene(new Group(t, b1, b2, b3, b4, tf));
                secondary.setScene(scene);
                secondary.sizeToScene();
                secondary.show();

                b1.setOnAction(//filename
                    e -> {
                        searchTerm = tf.getText();
                        searchBy = b1.getText();
                        secondary.close();
                        System.out.println(searchTerm + " " + searchBy);
                        searchButton.setDisable(true);
                        showButton.setDisable(false);

                        boolean working = true;
                        int index = 0;
                        while (working) {
                            try {
                                table.getItems().remove(index);
                                index++;
                            } catch (Exception ee) {
                                working = false;
                            }
                        }

                    }
                );
                b2.setOnAction(//artist
                    e -> {
                        searchTerm = tf.getText();
                        searchBy = b2.getText();
                        secondary.close();
                        System.out.println(searchTerm + " " + searchBy);
                        searchButton.setDisable(true);
                        showButton.setDisable(false);

                        boolean working = true;
                        int index = 0;
                        while (working) {
                            try {
                                table.getItems().remove(index);
                                index++;
                            } catch (Exception ee) {
                                working = false;
                            }
                        }

                    }
                );
                b3.setOnAction(//title
                    e -> {
                        searchTerm = tf.getText();
                        searchBy = b3.getText();
                        secondary.close();
                        System.out.println(searchTerm + " " + searchBy);
                        searchButton.setDisable(true);
                        showButton.setDisable(false);

                        boolean working = true;
                        int index = 0;
                        while (working) {
                            try {
                                table.getItems().remove(index);
                                index++;
                            } catch (Exception ee) {
                                working = false;
                            }
                        }

                    }
                );
                b4.setOnAction(//album
                    e -> {
                        searchTerm = tf.getText();
                        searchBy = b4.getText();
                        secondary.close();
                        System.out.println(searchTerm + " " + searchBy);
                        searchButton.setDisable(true);
                        showButton.setDisable(false);

                        boolean working = true;
                        int index = 0;
                        while (working) {
                            try {
                                table.getItems().remove(index);
                                index++;
                            } catch (Exception ee) {
                                working = false;
                            }
                        }

                    }
                );

            }
        );
    }

    private HBox getHBox() {
        HBox hbox = new HBox();
        playButton.setPrefSize(100, 20);
        pauseButton.setPrefSize(100, 20);
        pauseButton.setDisable(true);
        searchButton.setPrefSize(200, 20);
        showButton.setPrefSize(200, 20);
        showButton.setDisable(true);
        hbox.getChildren()
            .addAll(playButton, pauseButton, searchButton, showButton);
        ArrayList<String> allTitles = new ArrayList<>();
        ArrayList<String> allArtists = new ArrayList<>();
        ArrayList<String> allAlbums = new ArrayList<>();
        for (int x = 0; x < actualSongs.length; x++) {
            Media media = actualSongs[x];
            ObservableMap<String, Object> metaData = media.getMetadata();
            String metaDataToString = metaData.toString();
            if (metaDataToString.contains("title=")) {
                int indOfTitles = metaDataToString.indexOf("title=");
                String title = metaDataToString
                    .substring(indOfTitles + 6, metaDataToString.length() - 1);
                title = modifyTitle(title);
                allTitles.add(title);
            } else {
                allTitles.add(" ");
            }
            if (metaDataToString.contains("artist=")) {
                int indOfArtist = metaDataToString.indexOf("artist=");
                String artist = metaDataToString
                    .substring(indOfArtist + 7, metaDataToString.length());
                artist = modifyArtist(artist);
                allArtists.add(artist);
            } else {
                allArtists.add(" ");
            }
            if (metaDataToString.contains("album=")) {
                int indOfAlbum = metaDataToString.indexOf("album=");
                String album = metaDataToString
                    .substring(indOfAlbum + 6, metaDataToString.length());
                album = modifyAlbum(album);
                allAlbums.add(album);
            } else {
                allAlbums.add(" ");
            }
        }
        artistCol.setCellValueFactory(cellData -> {
                Integer rowIndex = cellData.getValue();
                return new ReadOnlyStringWrapper(allArtists
                    .get(rowIndex));
            });
        albumCol.setCellValueFactory(cellData -> {
                Integer rowIndex = cellData.getValue();
                return new ReadOnlyStringWrapper(allAlbums
                    .get(rowIndex));
            });
        titleCol.setCellValueFactory(cellData -> {
                Integer rowIndex = cellData.getValue();
                return new ReadOnlyStringWrapper(allTitles
                    .get(rowIndex));
            });
        pressSearchSongsButton();
        showButton.setOnAction(
            event -> {
                System.out.println("pressed Show Songs");
                int iterations = 0;
                while (iterations < numberOfSongs * 2) {
                    boolean working = true;
                    int index = 0;
                    while (working) {
                        try {
                            table.getItems().remove(index);
                        } catch (Exception ee) {
                            working = false;
                        }
                        index++;
                    }
                    iterations++;
                }
                int index = 0;
                while (index < numberOfSongs) {
                    try {
                        table.getItems().add(index);
                    } catch (Exception ee) {
                        System.out.println("");
                    }
                    index++;
                }
                fileNameCol.setCellValueFactory(cellData -> {
                        Integer rowIndex = cellData.getValue();
                        return new ReadOnlyStringWrapper(songs
                            .get(rowIndex).getName());
                    });
                artistCol.setCellValueFactory(cellData -> {
                        Integer rowIndex = cellData.getValue();
                        return new ReadOnlyStringWrapper(allArtists
                            .get(rowIndex));
                    });
                albumCol.setCellValueFactory(cellData -> {
                        Integer rowIndex = cellData.getValue();
                        return new ReadOnlyStringWrapper(allAlbums
                            .get(rowIndex));
                    });
                titleCol.setCellValueFactory(cellData -> {
                        Integer rowIndex = cellData.getValue();
                        return new ReadOnlyStringWrapper(allTitles
                            .get(rowIndex));
                    });
                searchButton.setDisable(false);
                showButton.setDisable(true);
            }
        );
        playButton.setOnAction(
            event -> {
                System.out.println("pressed Play");
                try {
                    int index = Integer
                        .parseInt(table.getSelectionModel()
                        .getSelectedItem().toString());
                    File song = songs.get(index);
                    if (Desktop.isDesktopSupported()) {
                        try {
                            String fileName = song.getName();
                            m = new Media("file:///"
                                + System.getProperty("user.dir")
                                .replace('\\', '/')
                                + "/" + fileName);
                            System.out.println(fileName);
                            player = new MediaPlayer(m);
                            player.play();
                            playButton.setDisable(true);
                            pauseButton.setDisable(false);
                        } catch (Exception e) {
                            System.out.println("");
                        }
                    }
                } catch (NullPointerException e) {
                    System.out.println("");
                }
            }
        );
        pauseButton.setOnAction(
            event -> {
                System.out.println("pressed Pause");
                player.pause();
                playButton.setDisable(false);
                pauseButton.setDisable(true);
            }
        );
        return hbox;
    }

    private VBox getVBox() {
        VBox vbox = new VBox();
        table.setEditable(true);
        for (int i = 0; i < numberOfSongs; i++) {
            table.getItems().add(i);
        }

        fileNameCol.setCellValueFactory(cellData -> {
                Integer rowIndex = cellData.getValue();
                return new ReadOnlyStringWrapper(songs.get(rowIndex).getName());
            });


        TableColumn attributesCol = new TableColumn("Attributes");

        fileNameCol.setMinWidth(200);
        attributesCol.setMinWidth(600);
        artistCol.setMinWidth(200);
        titleCol.setMinWidth(200);
        albumCol.setMinWidth(200);

        table.getColumns().addAll(fileNameCol, attributesCol);
        attributesCol.getColumns().addAll(artistCol, titleCol, albumCol);

        vbox.getChildren().addAll(table);

        return vbox;
    }

}