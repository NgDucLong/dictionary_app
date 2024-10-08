package Screen;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import java.io.IOException;
import java.util.Optional;

import static org.apache.maven.surefire.shared.lang3.ClassUtils.getClass;

public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Screen/AppScreen.fxml"));
        Image icon = new Image(getClass().getResourceAsStream("/Image/LOGO.png"));
        primaryStage.getIcons().add(icon);
        Scene scene = new Scene(root);
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setTitle("KTL Dictionary");

    }

    /**
     * Switch to SearchWords .
     */
    @FXML
    private Button buttonSW;

    @FXML
    public void switchtoSearchWords(javafx.event.ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Screen/SearchWordsScreen.fxml"));
            Parent searchRoot = loader.load();
            SearchWords searchController = loader.getController();
            Stage stage = (Stage) buttonSW.getScene().getWindow();
            Scene searchScene = new Scene(searchRoot);
            stage.setScene(searchScene);
            searchController.handleButtonSwap();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Switch to ShowAllWords .
     */
    @FXML
    private Button buttonSAW;

    @FXML
    public void switchtoShowAllWords(javafx.event.ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Screen/ShowAllWordsScreen.fxml"));
            Parent searchRoot = loader.load();
            ShowAllWords showController = loader.getController();
            showController.handleButtonSwap();
            Stage stage = (Stage) buttonSAW.getScene().getWindow();
            Scene searchScene = new Scene(searchRoot);
            stage.setScene(searchScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Switch to AddWords .
     */
    @FXML
    private Button buttonAW;

    @FXML
    public void switchtoAddWords(javafx.event.ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Screen/AddWord.fxml"));
            Parent searchRoot = loader.load();
            AddWords showController = loader.getController();
            showController.handleButtonSwap();
            Stage stage = (Stage) buttonAW.getScene().getWindow();
            Scene searchScene = new Scene(searchRoot);
            stage.setScene(searchScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Switch to Translate .
     */
    @FXML
    private Button buttonTrans;

    @FXML
    public void switchtoTranslate(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader((getClass().getResource("/Screen/TranslateScreen.fxml")));
            Parent searchRoot = loader.load();
            Stage stage = (Stage) buttonTrans.getScene().getWindow();
            Scene translateScene = new Scene(searchRoot);
            stage.setScene(translateScene);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Switch to Game .
     */
    @FXML
    private Button gamebutton;

    @FXML
    public void switchtoGame(javafx.event.ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Game/Home.fxml"));
            Parent searchRoot = loader.load();
            Stage stage = (Stage) gamebutton.getScene().getWindow();
            Scene searchScene = new Scene(searchRoot);
            stage.setScene(searchScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Out App.
     */

    @FXML
    private Button buttonOut;

    @FXML
    public void handleButtonOutAction(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Anti-accident window");
        alert.setHeaderText("Are you sure you want to close the KTL Dictionary App?");
        alert.setContentText("Any unsaved changes will be lost.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Stage stage = (Stage) buttonOut.getScene().getWindow();
            stage.close();
        }
    }

}
