package Screen;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import DictionaryCommandLine.Word;


public class ShowAllWords extends App {

    public static void main(String[] args) {
        launch(args);
    }

    private Map<String, Word> data = new HashMap<>();

    @FXML
    private ListView<String> listView;

    @FXML
    private WebView webView;

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlloader = new FXMLLoader();
        AnchorPane root = fxmlloader.load(getClass().getResourceAsStream("ShowAllWordsScreen.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setTitle("ShowAllWords");

    }

    public void initComponents(Scene scene) {
        this.webView = (WebView) scene.lookup("#webView");
        this.listView = (ListView<String>) scene.lookup("#listView");

    }

    public void readWordsFromFiles(String filename) throws IOException {
        data.clear();
        listView.getItems().clear();
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split("<html>");
            String word = parts[0];
            String meaning = "<html>" + parts[1];
            Word wordObj = new Word(word, meaning);
            data.put(word, wordObj);
        }
        br.close();
    }


    public void loadWordList() {
        List<String> wordList = new ArrayList<>(data.keySet());
        Collections.sort(wordList); // Sắp xếp từ A-Z
        this.listView.getItems().addAll(wordList);
    }

    public void init() {
        ShowAllWords context = this;
        this.listView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        Word selectedWord = data.get(newValue.trim());
                        if (selectedWord != null) { // Check if selectedWord is not null
                            String meaning = selectedWord.getWord_explain();
                            context.webView.getEngine().loadContent(meaning, "text/html");
                        }
                    }
                }
        );
    }

    @FXML
    private Button buttonBack;

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

    @FXML
    private ImageView imageEng;
    @FXML
    private ImageView imageVNese;
    @FXML
    private Button buttonSwap;

    @FXML
    public void handleButtonSwap() {
        boolean check = imageVNese.isVisible();
        imageEng.setVisible(check);
        imageVNese.setVisible(!check);
        try {
            if (imageEng.isVisible()) {
                init();
                readWordsFromFiles("src/data/E_V.txt");

            } else if (imageVNese.isVisible()) {
                init();
                readWordsFromFiles("src/data/V_E.txt");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        loadWordList();
    }

    @FXML
    private void SoundButtonAction() {
        String selectedWord = listView.getSelectionModel().getSelectedItem();
        if (selectedWord == null) {
            System.out.println("No word selected");
            return;
        }
        boolean isEnglishVisible = imageEng.isVisible();
        new Thread(() -> {
            try {
                if (isEnglishVisible) {
                    DictionaryCommandLine.api.Speech.EnglishAPISpeech(selectedWord);
                } else {
                    DictionaryCommandLine.api.Speech.VietnameseAPISpeech(selectedWord);
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error occurred while speaking the word");
            }
        }).start();
    }
}