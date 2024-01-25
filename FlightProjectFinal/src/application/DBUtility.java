package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class DBUtility {
	 // Method to change scenes based on user action
	public static void changeScene(ActionEvent event, String fxmlFile, String title, String username, int isAdmin) {
		//Null set scene 
		Parent root = null; //serves as a placeholder depending on info entered 
		
		//Switches from login to welcome after entering correct credentials
		final String admin = String.valueOf(isAdmin);
		if(username != null && admin != null) { //If provided username and admin status are not null, it implies that the user has entered correct credentials, 
			try { //handle potential errors or exceptions that might occur during the execution of the code 
				
				 // Load FXML file and set controller properties
				FXMLLoader loader = new FXMLLoader(DBUtility.class.getResource(fxmlFile));
				root = loader.load();
				WelcomeController welcomeController = loader.getController();
				welcomeController.displayUserInfo(username, isAdmin);
			}catch (IOException e) { //handles exception and prints in console..reads files 
				e.printStackTrace();
			}
		}else //If the username is null, it switches to the login or register screen
			try {
				root = FXMLLoader.load(DBUtility.class.getResource(fxmlFile));
			}catch (IOException e) {
				e.printStackTrace();
			}
		
		//Changes scenes other than login to welcome or login to register
		Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		stage.setTitle(title);
		stage.setScene(new Scene(root, 1130, 700));
		stage.show();
	}
	
	//Method to Register the user and creates database entries for data
	public static void registerUser(ActionEvent event, String firstName, String lastName, String ssn, String address, String zip, String state, String email, String username, String password, String securityQuestion, String securityAnswer, int isAdmin) {
		//Null set initial variables which allows you to check whether the variable has been assigned a value later in the code.
		Connection connection = null; //Connection to the database
		PreparedStatement insert = null; //Inserts new data
		PreparedStatement check1 = null; //Checks new data vs. saved data
		PreparedStatement check2 = null; 
		ResultSet resultSet1 = null; //Results of a query
		ResultSet resultSet2 = null;
		
		try {
			//test
			//Establish connection to database
			connection = DriverManager.getConnection("jdbc:mysql://cis3270project.mysql.database.azure.com/project_data", "cis3270", "Flightproject!");
			
			//Performs the check and inserts user data into database if able
			check1 = connection.prepareStatement("SELECT * FROM users WHERE Email = ?"); //Check email query
			check1.setString(1, email);
			check2 = connection.prepareStatement("SELECT * FROM users WHERE Username = ?"); //Check username query
			check2.setString(1, username);
			resultSet1 = check1.executeQuery(); //If empty, email is available
			resultSet2 = check2.executeQuery(); //If empty, username is available
			
			//isBeforeFirst() checks if resultSet is empty(false), runs body if true
			if(resultSet1.isBeforeFirst()) { //Checks email vs. database
				System.out.println("Email already exists.");
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setContentText("An account with that email already exists.");
				alert.show();
			}else if(resultSet2.isBeforeFirst()){ //Checks username vs. database
				System.out.println("User already exists.");
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setContentText("Username is taken.");
				alert.show();
			}else //Inserts new user data into database
				insert = connection.prepareStatement("INSERT INTO users(FirstName, LastName, SSN, Address, Zip, State, Email, Username, Password, SecurityQuestion, SecurityAnswer, Admin, bookedFlights) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
				insert.setString(1, firstName);
				insert.setString(2, lastName);
				insert.setString(3, ssn);
				insert.setString(4, address);
				insert.setString(5, zip);
				insert.setString(6, state);
				insert.setString(7, email);
				insert.setString(8, username);
				insert.setString(9, password);
				insert.setString(10, securityQuestion);
				insert.setString(11, securityAnswer);
				insert.setLong(12, isAdmin);
				insert.setString(13, "");
				insert.executeUpdate();
				
				//Switches scene from sign up to welcome
				changeScene(event, "welcome.fxml", "Welcome", username, isAdmin);
		}catch(SQLException e) {
			e.printStackTrace();
		}finally { //Closes database resources. Its important to release the resources associated with these objects and prevent potential memory leaks
			if(check1 != null) {
				try {
					check1.close();
				}catch(SQLException e) {
					e.printStackTrace();
				}
			}
			if(check2 != null) {
				try {
					check2.close();
				}catch(SQLException e) {
					e.printStackTrace();
				}
			}
			if(resultSet1 != null) {
				try {
					resultSet1.close();
				}catch(SQLException e) {
					e.printStackTrace();
				}
			}
			if(resultSet2 != null) {
				try {
					resultSet2.close();
				}catch(SQLException e) {
					e.printStackTrace();
				}
			}
			if(connection != null) {
				try {
					connection.close();
				}catch(SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	//Logs user in method 
	public static void logInUser(ActionEvent event, String username, String password) {
		//Null set initial variables
		Connection connection = null; //Connection to the database
		PreparedStatement pstmnt = null; //Statement to query database
		ResultSet resultSet = null; //Results of a query
		
		try {
			//Establish connection to database
			connection = DriverManager.getConnection("jdbc:mysql://cis3270project.mysql.database.azure.com/project_data", "cis3270", "Flightproject!");
			
			//Check password and admin status against username
			pstmnt = connection.prepareStatement("SELECT Password, Admin FROM users WHERE Username = ?");
			pstmnt.setString(1, username);
			resultSet = pstmnt.executeQuery();
			
			//isBeforeFirst() checks if resultSet is empty(false), runs body if true
			if(!resultSet.isBeforeFirst()) { //Checks if there is a password and admin status for the entered username to see if user exists 
				System.out.println("User not found.");
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setContentText("An account with that username and password does not exist.");
				alert.show();
			}else
				while(resultSet.next()) { //Compares entered credentials to database credentials for given username
					String dbPassword = resultSet.getString("Password");
					int dbAdmin = resultSet.getInt("Admin");
					if(dbPassword.equals(password)) {
						changeScene(event, "welcome.fxml", "Welcome", username, dbAdmin);
					}else {
						System.out.println("Password is incorrect.");
						Alert alert = new Alert(Alert.AlertType.ERROR);
						alert.setContentText("The provided credentials are incorrect.");
						alert.show();
					}
				}
		}catch(SQLException e) {
			e.printStackTrace();
		}finally { //Closes database resources
			if(pstmnt != null) {
				try {
					pstmnt.close();
				}catch(SQLException e) {
					e.printStackTrace();
				}
			}
			if(resultSet != null) {
				try {
					resultSet.close();
				}catch(SQLException e) {
					e.printStackTrace();
				}
			}
			if(connection != null) {
				try {
					connection.close();
				}catch(SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static String getSecurityQuestion(ActionEvent event, String username) {
		//Null set initial variables
		Connection connection = null; //Connection to the database
		PreparedStatement pstmnt = null; //Statement to query database
		ResultSet resultSet = null; //Results of a query
		String dbSecurityQuestion = "";
				
		try {
			//Establish connection to database
			connection = DriverManager.getConnection("jdbc:mysql://cis3270project.mysql.database.azure.com/project_data", "cis3270", "Flightproject!");
			
			//Check  against username
			pstmnt = connection.prepareStatement("SELECT SecurityQuestion FROM users WHERE Username = ?");
			pstmnt.setString(1, username); //Username retrieved from controller
			resultSet = pstmnt.executeQuery();
			
			//isBeforeFirst() checks if resultSet is empty(false), runs body if true
			if(!resultSet.isBeforeFirst()) { //Checks if the username exists
				System.out.println("User not found.");
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setContentText("An account with that username does not exist.");
				alert.show();
			}else //Sets return variable to security question
				if(resultSet.next()) {
					dbSecurityQuestion = resultSet.getString("SecurityQuestion");
				}
			}catch(SQLException e) {
				e.printStackTrace();
		}finally {
			if(pstmnt != null) {
				try {
					pstmnt.close();
				}catch(SQLException e) {
					e.printStackTrace();
				}
			}
			if(resultSet != null) {
				try {
					resultSet.close();
				}catch(SQLException e) {
					e.printStackTrace();
				}
			}
			if(connection != null) {
				try {
					connection.close();
				}catch(SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return dbSecurityQuestion;
	}
	
	public static String checkSecurityAnswer(ActionEvent event, String username, String securityAnswer) {
		//Null set initial variables
		Connection connection = null; //Connection to the database
		PreparedStatement pstmnt = null; //Statement to query database
		ResultSet resultSet = null; //Results of a query
		String dbPassword = "";
				
		try {
			//Establish connection to database
			connection = DriverManager.getConnection("jdbc:mysql://cis3270project.mysql.database.azure.com/project_data", "cis3270", "Flightproject!");
					
			//Check password and admin status against username
			pstmnt = connection.prepareStatement("SELECT SecurityAnswer, Password FROM users WHERE Username = ?");
			pstmnt.setString(1, username); //Username retrieved from controller
			resultSet = pstmnt.executeQuery();
					
			while(resultSet.next()) { //Compares entered credentials to database credentials for given username
				if(securityAnswer.equals(resultSet.getString("SecurityAnswer"))) {
					dbPassword = resultSet.getString("Password");
				}else {
					System.out.println("Incorrect security answer.");
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setContentText("The security answer for the given username does not exist.");
					alert.show();
				}
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}finally { //Closes database resources
			if(pstmnt != null) {
				try {
					pstmnt.close();
				}catch(SQLException e) {
					e.printStackTrace();
				}
			}
			if(resultSet != null) {
				try {
					resultSet.close();
				}catch(SQLException e) {
					e.printStackTrace();
				}
			}
			if(connection != null) {
				try {
					connection.close();
				}catch(SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return dbPassword;
	}
}