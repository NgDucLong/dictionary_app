package Game.Hangman;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;
import java.util.Random;

public class HangmanController {

    @FXML
    private ImageView img;
    Image img1 = new Image(getClass().getResourceAsStream("/Image/1.png"));
    Image img2 = new Image(getClass().getResourceAsStream("/Image/2.png"));
    Image img3 = new Image(getClass().getResourceAsStream("/Image/3.png"));
    Image img4 = new Image(getClass().getResourceAsStream("/Image/4.png"));
    Image img5 = new Image(getClass().getResourceAsStream("/Image/5.png"));
    Image img6 = new Image(getClass().getResourceAsStream("/Image/6.png"));
    Image img7 = new Image(getClass().getResourceAsStream("/Image/7.png"));

    @FXML
    private TextField tf1, tf2, tf3, tf4, tf5, tf6, tf7, tf8; // The text field where the button will appear

    @FXML
    private Button checkButton;

    @FXML
    private Label hintLabel;

    @FXML
    private Label hint;

    @FXML
    private Label letterCount;

    @FXML
    private Label bonus1;
    @FXML
    private Label bonus2;

    @FXML
    private Button Q, W, E, R, T, Y, U, I, O, P, A, S, D, F, G, H, J, K, L, Z, X, C, V, B, N, M;

    private MediaPlayer backgroundSound;

    public void initialize() {
        setHint();

        backgroundSound = new MediaPlayer(new Media(getClass().getResource("/Game/Sound/background2.mp3").toExternalForm()));
        backgroundSound.setCycleCount(MediaPlayer.INDEFINITE);
        backgroundSound.play();
    }

    public HangmanController() {

    }

    String[] list = {
            "VIETNAM;COUNTRY;ASIA;EAST SEA",
            "GIRAFFE;ANIMAL;HERBIVOROUS;TALL",
            "ELEPHANT;MAMMAL;WILD;BIG",
            "KUAKATA;BEACH;SOUTH ASIA;BANGLADESH",
            "CANADA;COUNTRY;NORTH AMERICA;MAPLE LEAF",
            "DOLPHIN;MAMMAL;MARINE;INTELLIGENT",
            "DOCTOR;PROFESSION;HEALTHCARE;MEDICAL",
            "FOOTBALL;SPORT;BALL;GOAL",
            "TEACHER;OCCUPATION;EDUCATION;INSTRUCTOR",
            "LEOPARD;ANIMAL;BIG CAT;FAST",
            "BICYCLE;TRANSPORT;SLOW;2 WHEELS",
            "SALMON;ANIMAL;FISH;CANNED FOOD",
            "SPARROW;ANIMAL;BIRD;SMALL",
            "PARROTS;BIRD;COLORFUL;IMITATE VOICES",
            "AIRPLANE;TRANSPORT;AVIATION;FLIGHT",
            "TRAIN;TRANSPORT;RAIL;LOCOMOTIVE",
            "SHIP;TRANSPORT;MARITIME;VESSEL",
            "ENGINEER;PROFESSION;TECHNOLOGY;DESIGN",
            "SWIMMING;SPORT;INDIVIDUAL;POOL",
            "BOOK;LITERATURE;READING;PAPER",
            "KEYBOARD;ELECTRONIC;TYPING;INPUT",
            "UMBRELLA;WEATHER;PROTECTION;RAIN",
            "MUSIC;ART;SOUND;SONG"
    };

    int random = new Random().nextInt(list.length);
    String wordHint = list [random];
    String[] split = wordHint.split(";", 4);
    String answer = split[0];
    String hintStr = split[1];
    String Bonus1 = split[2];
    String Bonus2 = split[3];
    int letterSize = answer.length();

    public void setHint(){
        hint.setText(hintStr);
        letterCount.setText(letterSize + " Letters");
        if(letterSize==7){
            tf8.setVisible(false);
        }
        if(letterSize==6){
            tf7.setVisible(false);
            tf8.setVisible(false);
        }
        if(letterSize==5){
            tf6.setVisible(false);
            tf7.setVisible(false);
            tf8.setVisible(false);
        }
        if(letterSize==4){
            tf5.setVisible(false);
            tf6.setVisible(false);
            tf7.setVisible(false);
            tf8.setVisible(false);
        }
    }

    private int guessesLeft = 6;

    public void updateHangmanImage() {
        if(guessesLeft == 6)
            img.setImage(img2);
        else if(guessesLeft == 5)
            img.setImage(img3);
        else if(guessesLeft == 4)
            img.setImage(img4);
        else if(guessesLeft == 3) {
            img.setImage(img5);
            bonus1.setText(Bonus1);
        }
        else if(guessesLeft == 2) {
            img.setImage(img6);
            bonus2.setText(Bonus2);
        }
        else if(guessesLeft == 1) {
            img.setImage(img7);
        }
        guessesLeft--;
    }

    private String getLetter(int index) {
        switch(index) {
            case 0:
                return tf1.getText();
            case 1:
                return tf2.getText();
            case 2:
                return tf3.getText();
            case 3:
                return tf4.getText();
            case 4:
                return tf5.getText();
            case 5:
                return tf6.getText();
            case 6:
                return tf7.getText();
            case 7:
                return tf8.getText();
            default:
                return "";
        }
    }

    private void setLetter(int index, String str) {
        switch (index) {
            case 0:
                tf1.setText(str);
                break;
            case 1:
                tf2.setText(str);
                break;
            case 2:
                tf3.setText(str);
                break;
            case 3:
                tf4.setText(str);
                break;
            case 4:
                tf5.setText(str);
                break;
            case 5:
                tf6.setText(str);
                break;
            case 6:
                tf7.setText(str);
                break;
            case 7:
                tf8.setText(str);
                break;
        }
    }

    private void checkLetter(String letter) {
        boolean found = false;
        for (int i = 0; i < answer.length(); i++) {
            if (answer.charAt(i) == letter.charAt(0)) {
                setLetter(i, letter);
                found = true;
            }
        }
        if (!found) {
            updateHangmanImage();
        }
        if (guessesLeft == 0) {
            gameOver(false);
        } else if (isWordGuessed()) {
            gameOver(true);
        }
    }

    @FXML
    private void handleQButtonClick() {
        checkLetter("Q");
        Q.setDisable(true);
    }
    @FXML
    private void handleWButtonClick() {
        checkLetter("W");
        W.setDisable(true);
    }
    @FXML
    private void handleEButtonClick() {
        checkLetter("E");
        E.setDisable(true);
    }
    @FXML
    private void handleRButtonClick() {
        checkLetter("R");
        R.setDisable(true);
    }
    @FXML
    private void handleTButtonClick() {
        checkLetter("T");
        T.setDisable(true);
    }
    @FXML
    private void handleYButtonClick() {
        checkLetter("Y");
        Y.setDisable(true);
    }
    @FXML
    private void handleUButtonClick() {
        checkLetter("U");
        U.setDisable(true);
    }
    @FXML
    private void handleIButtonClick() {
        checkLetter("I");
        I.setDisable(true);
    }
    @FXML
    private void handleOButtonClick() {
        checkLetter("O");
        O.setDisable(true);
    }
    @FXML
    private void handlePButtonClick() {
        checkLetter("P");
        P.setDisable(true);
    }
    @FXML
    private void handleAButtonClick() {
        checkLetter("A");
        A.setDisable(true);
    }
    @FXML
    private void handleSButtonClick() {
        checkLetter("S");
        S.setDisable(true);
    }
    @FXML
    private void handleDButtonClick() {
        checkLetter("D");
        D.setDisable(true);
    }
    @FXML
    private void handleFButtonClick() {
        checkLetter("F");
        F.setDisable(true);
    }@FXML
    private void handleGButtonClick() {
        checkLetter("G");
        G.setDisable(true);
    }@FXML
    private void handleHButtonClick() {
        checkLetter("H");
        H.setDisable(true);
    }@FXML
    private void handleJButtonClick() {
        checkLetter("J");
        J.setDisable(true);
    }@FXML
    private void handleKButtonClick() {
        checkLetter("K");
        K.setDisable(true);
    }
    @FXML
    private void handleLButtonClick() {
        checkLetter("L");
        L.setDisable(true);
    }
    @FXML
    private void handleZButtonClick() {
        checkLetter("Z");
        Z.setDisable(true);
    }
    @FXML
    private void handleXButtonClick() {
        checkLetter("X");
        X.setDisable(true);
    }
    @FXML
    private void handleCButtonClick() {
        checkLetter("C");
        C.setDisable(true);
    }
    @FXML
    private void handleVButtonClick() {
        checkLetter("V");
        V.setDisable(true);
    }
    @FXML
    private void handleBButtonClick() {
        checkLetter("B");
        B.setDisable(true);
    }
    @FXML
    private void handleNButtonClick() {
        checkLetter("N");
        N.setDisable(true);
    }
    @FXML
    private void handleMButtonClick() {
        checkLetter("M");
        M.setDisable(true);
    }

    private boolean isWordGuessed() {
        for (int i = 0; i < answer.length(); i++) {
            if (!getLetter(i).equals(String.valueOf(answer.charAt(i)))) {
                return false;
            }
        }
        return true;
    }

    private void gameOver(boolean win) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        if (win) {
            alert.setTitle("Congratulations!");
            alert.setHeaderText("You've won!");
            alert.setContentText("You guessed the word correctly: " + answer);
        } else {
            alert.setTitle("Game Over");
            alert.setHeaderText("You've lost!");
            alert.setContentText("The correct word was: " + answer);
        }
        alert.showAndWait();
    }

    public void selectNewWord() {
        int random = new Random().nextInt(list.length);
        String wordHint = list[random];
        String[] split = wordHint.split(";", 4);
        answer = split[0];
        hintStr = split[1];
        Bonus1 = split[2];
        Bonus2 = split[3];
        letterSize = answer.length();

        // Update hint and letter count labels
        hint.setText(hintStr);
        letterCount.setText(letterSize + " Letters");

        // Reset text fields visibility based on letter size
        tf8.setVisible(letterSize >= 8);
        tf7.setVisible(letterSize >= 7);
        tf6.setVisible(letterSize >= 6);
        tf5.setVisible(letterSize >= 5);
        tf4.setVisible(letterSize >= 4);
        tf3.setVisible(letterSize >= 3);
        tf2.setVisible(letterSize >= 2);
        tf1.setVisible(letterSize >= 1);

        clearTextFields();
        // Reset hangman image and guesses left
        img.setImage(img1);
        guessesLeft = 6;

        bonus1.setText("");
        bonus2.setText("");

        setAllLetterButtonsEnable(false);
    }

    private void setAllLetterButtonsEnable(boolean disable) {
        Q.setDisable(disable);
        W.setDisable(disable);
        E.setDisable(disable);
        R.setDisable(disable);
        T.setDisable(disable);
        Y.setDisable(disable);
        U.setDisable(disable);
        I.setDisable(disable);
        O.setDisable(disable);
        P.setDisable(disable);
        A.setDisable(disable);
        S.setDisable(disable);
        D.setDisable(disable);
        F.setDisable(disable);
        G.setDisable(disable);
        H.setDisable(disable);
        J.setDisable(disable);
        K.setDisable(disable);
        L.setDisable(disable);
        Z.setDisable(disable);
        X.setDisable(disable);
        C.setDisable(disable);
        V.setDisable(disable);
        B.setDisable(disable);
        N.setDisable(disable);
        M.setDisable(disable);
    }

    @FXML
    private void handleNewWordButtonClick() {
        selectNewWord();
    }

    private void clearTextFields() {
        tf1.clear();
        tf2.clear();
        tf3.clear();
        tf4.clear();
        tf5.clear();
        tf6.clear();
        tf7.clear();
        tf8.clear();
    }

    @FXML
    private void backToHome(ActionEvent event) throws IOException {
        backgroundSound.stop();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Return to Home");
        alert.setHeaderText("Close this window and return to Home?");
        alert.setContentText("Unsaved data will be lost.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Game/Home.fxml"));
            Parent menuRoot = loader.load();
            Scene menuScene = new Scene(menuRoot);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(menuScene);
            stage.show();
        }
    }

}
