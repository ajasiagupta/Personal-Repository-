package application;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class WelcomeController implements Initializable {

	//Initialize scene components
	@FXML // References to JavaFX components (buttons and text fields and labels ) defined in the associated FXML file.
	private Button bt_whome;
	
	@FXML 
	private Label lbl_wwelcome;
	
	@FXML 
	private Label lbl_wusername;
	
	@FXML 
	private Label lbl_wloggedinas;
	
	private static String currentUsername = null;
	
	//Component event handlers
	@Override
	public void initialize(URL url, ResourceBundle resources) {
		
		//Register screen home button functionality
		bt_whome.setOnAction(new EventHandler<ActionEvent>() { //when'home' button is clicked a special set of instructions is created (event handler is created)  

			
			@Override
			public void handle(ActionEvent event) {
				DBUtility.changeScene(event, "Main.fxml", "JetSetter", null, 0); //when home button is pressed method from dbutil is invoked that changes scene to main fxml file, named jetsetter
			}                          //null zero references parameter in change scene method, tells parameters to skip over the first if statement in change scene method so that it runs everything else. 
			
		});
	}
	
	//Displays the users info on the welcome scene
	public void displayUserInfo(String username, int isAdmin) { //defines a method named displayUserInfo that takes two parameters: a String called username and an int called isAdmin. 

		lbl_wusername.setText(username); //sets the text of  label (lbl_wusername) to the value of the username parameter, updating the label in the user interface to display the provided username.
		currentUsername = lbl_wusername.getText(); //assigns the text of the lbl_wusername label to a static variable named currentUsername. To keep  track of the current username for later use within the class.
		if (isAdmin == 1) {
			lbl_wloggedinas.setText("You are logged in as an Admin.");
		}else
			lbl_wloggedinas.setText("You are logged in as a Customer.");
	} // if else is used to determine the role of the user based on the value of the isAdmin parameter. If isAdmin is equal to 1, it sets the text of another label (lbl_wloggedinas) to indicate that the user is logged in as an admin. Otherwise, it indicates that the user is logged in as a customer.
	
	
	// Getting current user ssn
	public static String getCurrentUser() { //method returns string 
		Connection conn = mySQLConnection.ConnectDb();
		String sql = "Select ssn from users where username= " + "\'" + currentUsername + "\'";
		//running query to see if user exists with username and password
		PreparedStatement pst;
		
		String currentUser = null; //declares a local variable currentUser and initializes it with the value null. Variable is used to store the result obtained from the database.

		try { //handle potential errors or exceptions that might occur during the execution of the code inside the try block. designed to catch SQLException, which is an exception related to database operations.
			
			// prepare and execute a SQL query against the database. 
			pst = conn.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			
			//getting user ssn if user does exist
			while (rs.next()) {
				currentUser = rs.getString(1);
			}
			
		} catch (SQLException e) { //catch block contains code to handle the exception, and it prints the exception details to the console using e.printStackTrace().
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return currentUser;
	}


}
