package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;



public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try { // Code that might throw an exception
			Parent root = FXMLLoader.load(getClass().getResource("log-in.fxml")); // Load the FXML file for the login screen
			Scene scene = new Scene(root,1130,700);
			primaryStage.setTitle("JetSetter");
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			
			primaryStage.setScene(scene);  // Set the scene for the main stage
			primaryStage.show();
		} catch(Exception e) {   // handle...Print the stack trace in case of an exception
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}

