package Game.History;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class HistoryController {
    @FXML
    private TextArea historyLabel;

    private Scene scene;

    private Stage stage;

    private Parent root;


    public void initialize() {
        // Load and display the score history when the controller is initialized.
        loadHistory();
    }

    private void loadHistory() {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/java/Game/History/Score.txt"))) {
            StringBuilder historyText = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                historyText.append(line).append("\n");
            }

            historyLabel.setText(historyText.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void backToMenu(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/Game/Menu.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
