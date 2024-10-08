package Screen;

import DictionaryCommandLine.api.Speech;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Translate extends App {
    @FXML
    private Button buttonBack;

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("TranslateScreen.fxml"));
        TextArea textTarget = new TextArea();
        TextArea textExplain = new TextArea();
        textTarget.setOnKeyReleased(event -> {
            String text = textTarget.getText();
            String translatedText = DictionaryCommandLine.api.Translate.translateText(text, "en", "vi");
            textExplain.setText(translatedText);
        });
        VBox Translateroot = new VBox(textTarget, textExplain);
        primaryStage.setTitle("Translation Application");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    @FXML
    private AnchorPane translateEtoVPane;

    @FXML
    private AnchorPane translateVtoEPane;

    @FXML
    private TextArea text_target;

    @FXML
    private TextArea text_explain;
    private MediaPlayer mediaPlayer;
    private Media media;

    @FXML
    public void switchBack(javafx.event.ActionEvent actionEvent) {
        try {
            Parent previousRoot = FXMLLoader.load(getClass().getResource("/Screen/AppScreen.fxml"));
            Stage stage = (Stage) buttonBack.getScene().getWindow();
            Scene previousScene = new Scene(previousRoot);
            stage.setScene(previousScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isVietnameseToEnglish = true;

    // ExecutorService để thực hiện dịch văn bản sau một khoảng thời gian chờ
    private ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    @FXML
    public void initialize() {
        translateEtoVPane.setVisible(false);
        text_target.textProperty().addListener((observable, oldValue, newValue) -> {
            cancelTranslationTask();
            scheduleTranslationTask(newValue);
        });
    }

    @FXML
    private void swapButtonClicked() {
        boolean eToVVisible = translateEtoVPane.isVisible();
        boolean vToEVisible = translateVtoEPane.isVisible();
        translateEtoVPane.setVisible(!eToVVisible);
        translateVtoEPane.setVisible(!vToEVisible);
        isVietnameseToEnglish = !isVietnameseToEnglish;
    }

    private void cancelTranslationTask() {
        executorService.shutdownNow();
        executorService = Executors.newSingleThreadScheduledExecutor();
    }

    private void scheduleTranslationTask(String text) {
        Task<Void> translationTask = new Task<>() {
            @Override
            protected Void call() {
                try {
                    String translatedText;
                    if (isVietnameseToEnglish) {
                        translatedText = DictionaryCommandLine.api.Translate.translateText(text, "vi", "en");
                    } else {
                        translatedText = DictionaryCommandLine.api.Translate.translateText(text, "en", "vi");
                    }
                    updateUI(translatedText);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        };

        executorService.schedule(translationTask, 100, TimeUnit.MILLISECONDS);
    }

    private void updateUI(String translatedText) {
        text_explain.setText(translatedText);
    }

    @FXML
    private void soundButton1Clicked() {
        String textToSpeak = text_target.getText();
        if (textToSpeak != null && !textToSpeak.isEmpty()) { // Kiểm tra xem textToSpeak có được xác định không
            new Thread(() -> {
                try {
                    if (isVietnameseToEnglish) {
                        Speech.VietnameseAPISpeech(textToSpeak);
                    } else {
                        Speech.EnglishAPISpeech(textToSpeak);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        } else {
            System.out.println("Text to speak is undefined.");
        }
    }

    @FXML
    private void soundButton2Clicked() {
        String textToSpeak = text_explain.getText();
        if (textToSpeak != null && !textToSpeak.isEmpty()) {
            new Thread(() -> {
                try {
                    if (isVietnameseToEnglish) {
                        Speech.EnglishAPISpeech(textToSpeak);
                    } else {
                        Speech.VietnameseAPISpeech(textToSpeak);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        } else {
            System.out.println("Text to speak is undefined.");
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}