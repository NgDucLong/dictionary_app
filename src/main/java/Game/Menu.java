package Game;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;

public class Menu {

    private Stage stage;
    private Scene scene;
    private Parent root;

    public void EasyQuiz(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/Game/Menu.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    public void playGame(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/Game/QuizController.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void backToHome(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/Game/Home.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void backToApp(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/Screen/AppScreen.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void showHistory(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/Game/HistoryScores.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void HangMan(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/Game/hangman.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
