package Screen;

import DictionaryCommandLine.Word;
import DictionaryCommandLine.api.Speech;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.*;
import java.util.*;
import java.util.List;

public class AddWords extends App {
    @FXML
    private ChoiceBox wordType;

    @FXML
    private Button buttonAdd;

    @FXML
    private Button buttonDelete;

    @FXML
    private ListView<String> listView;

    @FXML
    private TextArea textMeaning;

    @FXML
    private TextField textWord;

    @FXML
    private WebView webView;

    private Map<String, Word> data = new LinkedHashMap<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        AnchorPane root = fxmlLoader.load(getClass().getResourceAsStream("/Screen/AddWord.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setTitle("AddWords");

        initComponents(root);
        readWordsFromFiles();

        buttonAdd.setOnAction(event -> addWord());
        buttonDelete.setOnAction(event -> deleteWord());
        listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

        });
        initialize();
    }

    public void initComponents(AnchorPane root) {
        this.webView = (WebView) root.lookup("#webView");
        this.listView = (ListView<String>) root.lookup("#listView");
        this.textWord = (TextField) root.lookup("#textWord");
        this.textMeaning = (TextArea) root.lookup("#textMeaning");
        this.buttonAdd = (Button) root.lookup("#buttonAdd");
        this.buttonDelete = (Button) root.lookup("#buttonDelete");
        this.wordType = (ChoiceBox<String>) root.lookup("#wordType");
    }

    @FXML
    public void initialize() {
        wordType.getItems().clear();
        wordType.getItems().addAll("Danh từ", "Động từ", "Tính từ", "Trạng từ", "Khác");
        wordType.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

        });
        updateListView();
        boolean updateMeaning = true;
        listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (updateMeaning && newValue != null) {
                if (data.containsKey(newValue)) {
                    String meaning = data.get(newValue).getWord_explain();
                    textMeaning.setText(meaning);
                    showMeaningInWebView(meaning);
                    textMeaning.clear();
                } else {
                    // Ẩn các tin nhắn lỗi khi từ được chọn không tồn tại trong bản đồ dữ liệu
                    textMeaning.clear();
                    webView.getEngine().loadContent("");
                }
            } else {
                // Ẩn các tin nhắn lỗi khi không có từ nào được chọn
                textMeaning.clear();
                webView.getEngine().loadContent("");
            }
        });
    }


    @FXML
    public void addWord() {
        String word = textWord.getText().trim();
        String meaning = textMeaning.getText().trim();
        String wordType = null;
        if (this.wordType != null) {
            wordType = (String) this.wordType.getValue();
        }

        if (!word.isEmpty() && !meaning.isEmpty() && wordType != null) {
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter("src/data/Learn.txt", true));
                String formattedMeaning = "<html><i>" + word + "</i><br/><ul><li><b><i> " + wordType + "</i></b><ul><li><font color='#cc0000'><b> " + meaning + "</b></font></li></ul></li></ul></html>";
                writer.write(word + formattedMeaning + "\n");
                writer.close();
                Word newWord = new Word(word, "<html>" + formattedMeaning);
                data.put(word, newWord);
                updateListView();
                textWord.clear();
                textMeaning.clear();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void deleteWord() {
        String selectedWord = listView.getSelectionModel().getSelectedItem();
        if (selectedWord != null) {
            try {
                Word removedWord = data.remove(selectedWord);
                BufferedWriter writer = new BufferedWriter(new FileWriter("src/data/Learn.txt"));
                for (Word word : data.values()) {
                    writer.write(word.getWord_target() + "<html>" + word.getWord_explain() + "\n");
                }
                writer.close();
                updateListView();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void updateListView() {
        List<String> wordList = new ArrayList<>(data.keySet());
        listView.getItems().setAll(wordList);
    }


    private void showMeaningInWebView(String meaning) {
        String htmlContent = "<html><body><p>" + meaning + "</p></body></html>";
        webView.getEngine().loadContent(htmlContent);
    }


    public void readWordsFromFiles() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("src/data/Learn.txt"));
        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split("<html>");
            if (parts.length >= 2) {
                String word = parts[0];
                String meaning = parts[1];
                Word wordObj = new Word(word, meaning);
                data.put(word, wordObj);
            } else {
                // Xử lý các trường hợp không hợp lệ (nếu cần)
            }
        }
        br.close();
        updateListView();
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

    public void handleButtonSwap() throws IOException {
        initialize();
        readWordsFromFiles();
    }

    @FXML
    private Button soundButton;

    @FXML
    private void soundButtonAction() {
        String selectedWord = listView.getSelectionModel().getSelectedItem();
        new Thread(() -> {
            try {
                if (selectedWord != null && !selectedWord.trim().isEmpty()) {
                    Speech.EnglishAPISpeech(selectedWord);
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error occurred while speaking the word");
            }
        }).start();
    }
}