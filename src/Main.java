import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import controller.LoginController;
import databasemanager.DatabaseConnector;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        DatabaseConnector.initializeDatabase();

      
        //loading the loginView file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/loginView.fxml"));
        Scene scene = new Scene((Parent) loader.load());

        LoginController controller = loader.getController();
        controller.setPrimaryStage(primaryStage);

        primaryStage.setTitle("Burrito King Restaurant");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
