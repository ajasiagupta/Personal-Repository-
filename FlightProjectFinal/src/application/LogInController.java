package application;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class LogInController implements Initializable {

	//Initialize scene components
	// @fxml references to JavaFX components (buttons and text fields) defined in the associated FXML file allowing the controller to interact with the components. 
	@FXML
	private Button bt_login;
	
	@FXML
	private Button bt_retrieve;
	
	@FXML
	private Button bt_register;
	
	@FXML
	private TextField tf_username;
	
	@FXML
	private TextField tf_password;
	
	//Component event handlers
	@Override
	public void initialize(URL url, ResourceBundle resources) {
		
		//Login button functionality
		bt_login.setOnAction(new EventHandler<ActionEvent>() { //when'Login' button is clicked a special set of instructions is created (event handler is created)  

		
			@Override
			public void handle(ActionEvent event) {
				DBUtility.logInUser(event, tf_username.getText(), tf_password.getText()); //when button is pressed method from dbutil is invoked that logs user in while getting there username and password 
			}
			
		});
			
		//Register button functionality
		bt_register.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				DBUtility.changeScene(event, "register.fxml", "Register", null, 0); //when register button is clicked change scene method from dbutil is invoked that tells the computer to change scenes to the "register.fxml" file while giving it the name scene "Register," 
                                                          //null zero references parameter in change scene method, tells parameters to skip over the first if statement in change scene method so that it runs everything else. 

			}
			
		});
		
		//Retrieve button functionality
		bt_retrieve.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				DBUtility.changeScene(event, "retrieve-password.fxml", "Retrieve Password", null, 0);
			}
			
		});
			
	}
	
}
