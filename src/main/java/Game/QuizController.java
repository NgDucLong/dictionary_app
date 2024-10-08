package Game;

import Game.Question.InputQuestion;
import Game.Question.Question;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import javafx.scene.paint.Color;

public class QuizController {

    @FXML
    private ArrayList<Question> questionList;

    private int currentQuestionIndex = 1;

    @FXML
    private Label questionLabel;

    @FXML
    private RadioButton option1, option2, option3, option4;

    @FXML
    private Button submitButton;

    @FXML
    private Label notificationLabel;

    @FXML
    private ToggleGroup toggleGroup;

    @FXML
    private Label timerLabel;

    private Timeline timer;
    private int timeRemaining = 15;

    private int questionCount = 0;

    private PauseTransition correctAnswerTransition;

    @FXML
    private Label scoreLabel;

    private int score = 0;

    @FXML
    private Label questionCountLabel;

    private MediaPlayer correctSound;

    private MediaPlayer wrongSound;

    private MediaPlayer backgroundSound;

    @FXML
    public void initialize() {
        scoreLabel.setText("Score: " + score); // Set initial score label
        questionList = InputQuestion.loadQuestionsFromFile("src/main/java/Game/Question/questionList.txt");

        Collections.shuffle(questionList);
        // Display the first question
        displayCurrentQuestion();

        initializeSoundEffects();

        initializeTimer();
        timer.play();

        backgroundSound.play();
    }

    private void initializeSoundEffects() {
        correctSound = new MediaPlayer(new Media(getClass().getResource("/Game/Sound/correct.mp3").toExternalForm()));
        wrongSound = new MediaPlayer(new Media(getClass().getResource("/Game/Sound/wrong.mp3").toExternalForm()));
        backgroundSound = new MediaPlayer(new Media(getClass().getResource("/Game/Sound/background.mp3").toExternalForm()));
        backgroundSound.setCycleCount(MediaPlayer.INDEFINITE); // Loop the background sound
    }


    private void updateQuestionCountLabel() {
        // Update question count label
        questionCountLabel.setText("Questions: " + String.format("%02d", questionCount + 1) + "/10");
    }

    private void initializeTimer() {
        timer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            timeRemaining--;
            updateTimerLabel();
            if (timeRemaining <= 0) {
                timer.stop();
            }
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
    }

    private void updateTimerLabel() {
        int minutes = timeRemaining / 60;
        int seconds = timeRemaining % 60;
        String timeFormatted = String.format("%02d:%02d", minutes, seconds);
        timerLabel.setText(timeFormatted);

        if (timeRemaining <= 0) {
            timer.stop();
            nextQuestion();
        }
    }

    private void resetTimer() {
        timeRemaining = 15; // Reset the timer to 15 seconds
        updateTimerLabel(); // Update the timer label
        timer.play();
    }

    @FXML
    private void submitAnswer(ActionEvent event) {
        // Get the selected option
        String selectedOption = getSelectedOption();

        if (selectedOption != null) {
            checkAnswer(selectedOption);
        } else {
            displayErrorMessage("Please select an option.");
        }
    }

    private void displayErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void backToMenu(ActionEvent event) throws IOException {
        backgroundSound.stop();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Return to Menu");
        alert.setHeaderText("Close this window and return to Menu?");
        alert.setContentText("Unsaved data will be lost.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Game/Menu.fxml"));
            Parent menuRoot = loader.load();
            Scene menuScene = new Scene(menuRoot);
            stage.setScene(menuScene);
            stage.show();
        }
    }

    private void nextQuestion() {
        resetTimer();
        if (currentQuestionIndex == 10) {
            displayResult();
        } else {
            currentQuestionIndex++;
            displayCurrentQuestion();
        }
    }

    public void nextQuestionButton() {
        questionCount++;
        nextQuestion();
    }

    private void displayResult() {
        backgroundSound.stop();
        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Game/Score.fxml"));
                Parent scoreRoot = loader.load();
                ScoreController scoreController = loader.getController();
                scoreController.setScore(score);
                Scene scoreScene = new Scene(scoreRoot);

                updateScoreLabel();
                saveScore();

                Stage stage = (Stage) questionLabel.getScene().getWindow();
                stage.setScene(scoreScene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        delay.play();
    }

    private void displayCurrentQuestion() {
        // Clear notification label
        notificationLabel.setText("");

        // Clear selection of radio buttons
        option1.setSelected(false);
        option1.setStyle("");
        option2.setSelected(false);
        option2.setStyle("");
        option3.setSelected(false);
        option3.setStyle("");
        option4.setSelected(false);
        option4.setStyle("");

        // Display current question
        Question currentQuestion = questionList.get(currentQuestionIndex);
        questionLabel.setText(currentQuestion.getQuestionText());
        questionLabel.setWrapText(true);
        questionLabel.setMaxWidth(350);
        ArrayList<String> options = currentQuestion.getOptions();
        option1.setText(options.get(0));
        option2.setText(options.get(1));
        option3.setText(options.get(2));
        option4.setText(options.get(3));

        updateQuestionCountLabel();
    }

    private void checkAnswer(String selectedOption) {
        timer.pause();
        disableOptions(); // Disable options while checking the answer

        Question currentQuestion = questionList.get(currentQuestionIndex);
        String correctAnswer = currentQuestion.getCorrectAnswer();
        RadioButton selectedRadioButton = null;

        correctSound.stop();
        wrongSound.stop();
        if (selectedOption.equals(correctAnswer)) {
            // Correct answer
            notificationLabel.setTextFill(Color.GREEN);
            notificationLabel.setText("Correct!");
            correctSound.play();
            // Increment score
            score += 10;
            updateScoreLabel();
            if (option1.getText().equals(correctAnswer)) {
                selectedRadioButton = option1;
            } else if (option2.getText().equals(correctAnswer)) {
                selectedRadioButton = option2;
            } else if (option3.getText().equals(correctAnswer)) {
                selectedRadioButton = option3;
            } else if (option4.getText().equals(correctAnswer)) {
                selectedRadioButton = option4;
            }
            if (selectedRadioButton != null) {
                selectedRadioButton.setStyle("-fx-background-color: lightgreen;");
            }
            // Display notification for 2 seconds
            displayNotificationForDuration(Duration.seconds(2));
        } else {
            // Incorrect answer
            notificationLabel.setTextFill(Color.RED);
            notificationLabel.setText("Wrong!");
            wrongSound.play();
            // Highlight the selected incorrect option in red
            if (option1.getText().equals(selectedOption)) {
                option1.setStyle("-fx-background-color: red;");
            } else if (option2.getText().equals(selectedOption)) {
                option2.setStyle("-fx-background-color: red;");
            } else if (option3.getText().equals(selectedOption)) {
                option3.setStyle("-fx-background-color: red;");
            } else if (option4.getText().equals(selectedOption)) {
                option4.setStyle("-fx-background-color: red;");
            }
            displayNotificationForDuration(Duration.seconds(4));
        }

        questionCount++;
        if (questionCount == 10) {
            displayResult();
            return; // Stop further execution
        }

        // Move to the next question after the notification duration
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), event -> {
            enableOptions();
            nextQuestion();
        }));
        timeline.play();
    }

    private void displayNotificationForDuration(Duration duration) {
        if (correctAnswerTransition != null) {
            correctAnswerTransition.stop();
        }
        correctAnswerTransition = new PauseTransition(duration);
        correctAnswerTransition.setOnFinished(event -> notificationLabel.setText(""));
        correctAnswerTransition.play();
    }

    private void disableOptions() {
        option1.setDisable(true);
        option2.setDisable(true);
        option3.setDisable(true);
        option4.setDisable(true);
    }

    private void enableOptions() {
        option1.setDisable(false);
        option2.setDisable(false);
        option3.setDisable(false);
        option4.setDisable(false);
    }

    private void saveScore() {
        try {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = now.format(formatter);
            String scoreData = formattedDateTime + " - " + ":  Score: " + score + "\n";

            Files.write(Paths.get("src/main/java/Game/History/Score.txt"), scoreData.getBytes(),
                    StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getSelectedOption() {
        if (option1.isSelected()) {
            return option1.getText();
        } else if (option2.isSelected()) {
            return option2.getText();
        } else if (option3.isSelected()) {
            return option3.getText();
        } else if (option4.isSelected()) {
            return option4.getText();
        } else {
            return null;
        }
    }

    private void updateScoreLabel() {
        scoreLabel.setText("Score: " + score);
    }

}
