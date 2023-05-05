import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class VideoPlayer extends Application {
    private static String resultFolder;

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: VideoPlayer <path_to_result_folder>");
            System.exit(1);
        }
        try {
            runPythonScript("C:/Users/genac/PycharmProjects/pythonProject6/main.py", args[0], "C:/Users/genac/Project/output");
        } catch (IOException e) {
            e.printStackTrace();
        }
        resultFolder = "C:/Users/genac/Project/output";
        launch(resultFolder);
    }

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        // Load videos from the Result folder
        ObservableList<File> videoFiles = loadVideoFiles(resultFolder);

        // Create a ListView for the videos
        ListView<File> videoListView = new ListView<>(videoFiles);
        videoListView.setPrefWidth(300);

        // Create a MediaPlayer array with a single element and set the MediaView's MediaPlayer
        MediaPlayer[] mediaPlayerArray = new MediaPlayer[]{new MediaPlayer(new Media(videoFiles.get(0).toURI().toString()))};

        // Create a MediaView for video playback
        MediaView mediaView = new MediaView(mediaPlayerArray[0]);
        DoubleProperty mvw = mediaView.fitWidthProperty();
        DoubleProperty mvh = mediaView.fitHeightProperty();
        mvw.bind(Bindings.selectDouble(mediaView.sceneProperty(), "width"));
        mvh.bind(Bindings.selectDouble(mediaView.sceneProperty(), "height"));
        mediaView.setPreserveRatio(true);

        // Event handler for ListView selection
        videoListView.getSelectionModel().selectedItemProperty().addListener(
                (ObservableValue<? extends File> observable, File oldValue, File newValue) -> {
                    mediaPlayerArray[0].stop();
                    mediaPlayerArray[0] = new MediaPlayer(new Media(newValue.toURI().toString()));
                    mediaPlayerArray[0].setOnEndOfMedia(() -> playNextVideo(mediaPlayerArray, videoFiles, mediaView, videoListView));
                    mediaView.setMediaPlayer(mediaPlayerArray[0]);
                    mediaPlayerArray[0].setAutoPlay(true);
                });

        // Add an event handler to play the next video when the current one ends
        mediaPlayerArray[0].setOnEndOfMedia(() -> playNextVideo(mediaPlayerArray, videoFiles, mediaView, videoListView));

        // Create control buttons and slider for video playback
        HBox controls = new HBox(10);
        controls.setAlignment(Pos.CENTER);
        Slider volumeSlider = createVolumeSlider(mediaPlayerArray);
        controls.getChildren().addAll(createPlayButton(mediaPlayerArray), createPauseButton(mediaPlayerArray), createStopButton(mediaPlayerArray), volumeSlider);
        VBox.setVgrow(controls, Priority.ALWAYS);

        // Add ListView and MediaView to the root layout
        root.setLeft(videoListView);
        root.setCenter(mediaView);
        root.setBottom(controls);

        Scene scene = new Scene(root, 1200, 800);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Video Player");
        primaryStage.show();
    }

    // Method to load video files from the Result folder
    private ObservableList<File> loadVideoFiles(String folderPath) {
        ObservableList<File> videoFiles = FXCollections.observableArrayList();
        try {
            List<File> files = Files.walk(Paths.get(folderPath))
                    .filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .filter(file -> file.getName().endsWith(".mp4"))
                    .collect(Collectors.toList());
            videoFiles.addAll(files);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return videoFiles;
    }

    // Method to create the play button
    private Button createPlayButton(MediaPlayer[] mediaPlayerArray) {
        Button playButton = new Button("Play");
        playButton.setOnAction(event -> mediaPlayerArray[0].play());
        return playButton;
    }

    // Method to create the pause button
    private Button createPauseButton(MediaPlayer[] mediaPlayerArray) {
        Button pauseButton = new Button("Pause");
        pauseButton.setOnAction(event -> mediaPlayerArray[0].pause());
        return pauseButton;
    }

    // Method to create the stop button
    private Button createStopButton(MediaPlayer[] mediaPlayerArray) {
        Button stopButton = new Button("Stop");
        stopButton.setOnAction(event -> mediaPlayerArray[0].stop());
        return stopButton;
    }

    // Method to create the volume slider
    private Slider createVolumeSlider(MediaPlayer[] mediaPlayerArray) {
        Slider volumeSlider = new Slider(0, 100, 50);
        volumeSlider.setPrefWidth(150);
        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> mediaPlayerArray[0].setVolume(newValue.doubleValue() / 100));
        return volumeSlider;
    }

    // Method to play the next video in the list
    private void playNextVideo(MediaPlayer[] mediaPlayerArray, ObservableList<File> videoFiles, MediaView mediaView, ListView<File> videoListView) {
        int currentIndex = videoListView.getSelectionModel().getSelectedIndex();
        if (currentIndex < videoFiles.size() - 1) {
            videoListView.getSelectionModel().select(currentIndex + 1);
            File nextVideo = videoFiles.get(currentIndex + 1);
            mediaPlayerArray[0].stop();
            mediaPlayerArray[0] = new MediaPlayer(new Media(nextVideo.toURI().toString()));
            mediaPlayerArray[0].setOnEndOfMedia(() -> playNextVideo(mediaPlayerArray, videoFiles, mediaView, videoListView));
            mediaView.setMediaPlayer(mediaPlayerArray[0]);
            mediaPlayerArray[0].setAutoPlay(true);
        }
    }

    public static void runPythonScript(String scriptPath, String arg1, String arg2) throws IOException {
        List<String> command = new ArrayList<>();
        command.add("python");
        command.add(scriptPath);
        command.add(arg1);
        command.add(arg2);

        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.directory(new File("C:/Users/genac/PycharmProjects/pythonProject6/"));
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        BufferedReader stdInput
                = new BufferedReader(new InputStreamReader(
                process.getInputStream()));
        String s;
        while ((s = stdInput.readLine()) != null) {
            System.out.println(s);
        }

        // Wait for the process to finish
        try {
            int exitCode = process.waitFor();
            System.out.println("Python script finished with exit code: " + exitCode);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

