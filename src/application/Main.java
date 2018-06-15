package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		
        try {
            // Read file fxml and draw interface.
            Parent root = FXMLLoader.load(getClass()
                    .getResource("/application/OODLEScene.fxml"));
            primaryStage.setTitle("目錄查看及擴展");
            primaryStage.setScene(new Scene(root));
            primaryStage.getIcons().add(new Image("/icon.jpg"));
            primaryStage.show();
            
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
	

	public static void main(String[] args) {
		launch(args);
	}
}
