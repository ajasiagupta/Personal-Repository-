package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

public class RegisterController implements Initializable {

	//Initialize scene components
	@FXML
	private Button bt_register;
		
	@FXML
	private Button bt_rlogin;
		
	@FXML
	private RadioButton rb_customer;
		
	@FXML
	private RadioButton rb_admin;
		
	@FXML
	private TextField tf_firstname;
		
	@FXML
	private TextField tf_lastname;
		
	@FXML
	private TextField tf_ssn;
		
	@FXML
	private TextField tf_address;
		
	@FXML
	private TextField tf_zip;
		
	@FXML
	private TextField tf_state;
		
	@FXML
	private TextField tf_email;
		
	@FXML
	private TextField tf_rusername;
		
	@FXML
	private TextField tf_rpassword;
		
	@FXML
	private TextField tf_squestion;
		
	@FXML
	private TextField tf_sanswer;
		
	//Component event handlers
	@Override
	public void initialize(URL url, ResourceBundle resources) {
		
		//Toggle group for radio buttons so only one can be selected and default selection set to customer
		ToggleGroup tg = new ToggleGroup();
		rb_customer.setToggleGroup(tg);
		rb_admin.setToggleGroup(tg);
		rb_customer.setSelected(true);
		
		//Register button functionality
		bt_register.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent event) {
				
				//Gets the selected radio button
				String admin = ((RadioButton)tg.getSelectedToggle()).getText();
				int isAdmin = Integer.parseInt(admin);
				
				//Check if all fields are filled in
				if(!tf_firstname.getText().trim().isEmpty() && !tf_lastname.getText().trim().isEmpty() && !tf_ssn.getText().trim().isEmpty() && !tf_address.getText().trim().isEmpty() && !tf_zip.getText().trim().isEmpty() && !tf_state.getText().trim().isEmpty() && !tf_email.getText().trim().isEmpty() && !tf_rusername.getText().trim().isEmpty() && !tf_rpassword.getText().trim().isEmpty() && !tf_squestion.getText().trim().isEmpty() && !tf_sanswer.getText().trim().isEmpty()) {
					DBUtility.registerUser(event, tf_firstname.getText(), tf_lastname.getText(), tf_ssn.getText(), tf_address.getText(), tf_zip.getText(), tf_state.getText(), tf_email.getText(), tf_rusername.getText(), tf_rpassword.getText(), tf_squestion.getText(), tf_sanswer.getText(), isAdmin);
				}else {
					System.out.println("A field is empty.");
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setContentText("Please fill in all required fields.");
					alert.show();
				}
			}
			
		});
		
		//Register scene login button functionality
		bt_rlogin.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				DBUtility.changeScene(event, "log-in.fxml", "Log In", null, 0);
			}
			
		});
	}

}
