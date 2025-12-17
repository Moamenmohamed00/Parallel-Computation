import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class Main extends Application {

    private File selectedFile;

    @Override
    public void start(Stage stage) {

        Label title = new Label("Multi-Threaded File Downloader");
        Label fileLabel = new Label("No file selected");

        ProgressBar progressBar = new ProgressBar(0);
        progressBar.setPrefWidth(350);

        TextArea logArea = new TextArea();
        logArea.setPrefHeight(200);
        logArea.setEditable(false);

        Button chooseFile = new Button("Choose File");
        Button seqBtn = new Button("Run Sequential");
        Button parBtn = new Button("Run Parallel");

        chooseFile.setOnAction(e -> {
            FileChooser chooser = new FileChooser();
            selectedFile = chooser.showOpenDialog(stage);
            if (selectedFile != null) {
                fileLabel.setText("Selected: " + selectedFile.getName());
            }
        });

        seqBtn.setOnAction(e ->
                runDownload(false, progressBar, logArea)
        );

        parBtn.setOnAction(e ->
                runDownload(true, progressBar, logArea)
        );

        VBox root = new VBox(10,
                title,
                chooseFile,
                fileLabel,
                progressBar,
                seqBtn,
                parBtn,
                logArea
        );
        root.setStyle("-fx-padding:20; -fx-alignment:center;");

        stage.setScene(new Scene(root, 500, 500));
        stage.setTitle("Parallel Computing Project");
        stage.show();
    }

    private void runDownload(
            boolean parallel,
            ProgressBar bar,
            TextArea logArea
    ) {
        if (selectedFile == null) return;

        bar.setProgress(0);
        logArea.clear();

        new Thread(() -> {
            try {
                DownloadManager manager = new DownloadManager(4);
                long fileSize = selectedFile.length();
                final long[] downloaded = {0};

                long time;
                if (parallel) {
                    time = manager.downloadParallel(
                            selectedFile.getAbsolutePath(),
                            "output_parallel.dat",
                            bytes -> {
                                downloaded[0] += bytes;
                                updateProgress(bar, downloaded[0], fileSize);
                            },
                            msg -> updateLog(logArea, msg)
                    );
                } else {
                    time = manager.downloadSequential(
                            selectedFile.getAbsolutePath(),
                            "output_sequential.dat",
                            bytes -> {
                                downloaded[0] += bytes;
                                updateProgress(bar, downloaded[0], fileSize);
                            },
                            msg -> updateLog(logArea, msg)
                    );
                }

                updateLog(
                        logArea,
                        "Finished in " + time + " ms"
                );
                manager.shutdown();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }).start();
    }

    private void updateProgress(
            ProgressBar bar,
            long done,
            long total
    ) {
        Platform.runLater(() ->
                bar.setProgress((double) done / total)
        );
    }

    private void updateLog(TextArea area, String msg) {
        Platform.runLater(() ->
                area.appendText(msg + "\n")
        );
    }

    public static void main(String[] args) {
        launch();
    }
}
