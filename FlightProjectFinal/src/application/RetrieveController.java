package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class RetrieveController implements Initializable {

	//Initialize scene components
	@FXML // @fxml references to JavaFX components (buttons and text fields) defined in the associated FXML file allowing the controller to interact with the components. 
	private Button bt_rlogin;
	
	@FXML
	private Button bt_checkpw;
	
	@FXML
	private Button bt_getsq;
	
	@FXML
	private TextField tf_rusername;
	
	@FXML
	private TextField tf_sqanswer;
	
	@FXML
	private Label lbl_securityq;
	
	@FXML
	private Label lbl_rpassword;
		
	@Override
	public void initialize(URL url, ResourceBundle resources) {

		//Retrieve screen back to login button functionality
		bt_rlogin.setOnAction(new EventHandler<ActionEvent>() { //creates event handler for button so when login button is clicked action below takes place 

			
			@Override
			public void handle(ActionEvent event) {
				DBUtility.changeScene(event, "log-in.fxml", "Log In", null, 0); //CS method called from dbutil, changes scene to lofin fxml file with the name log in 
			}       //null zero references parameter in change scene method, tells parameters to skip over the first if statement in change scene method so that it runs everything else. 
			
		});
		
		//Retrieve screen check username button functionality
		bt_getsq.setOnAction(new EventHandler<ActionEvent>() {
					
			@Override
			public void handle(ActionEvent event) {
				lbl_securityq.setText(DBUtility.getSecurityQuestion(event, tf_rusername.getText()));
			}
					
		});
		
		//Retrieve screen check security answer button functionality
		bt_checkpw.setOnAction(new EventHandler<ActionEvent>() {
							
			@Override
			public void handle(ActionEvent event) {
				lbl_rpassword.setText(DBUtility.checkSecurityAnswer(event, tf_rusername.getText().trim(), tf_sqanswer.getText().trim()));
			}
							
		});
	}

}
