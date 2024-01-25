package application;

import java.sql.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class mySQLConnection {

	Connection conn = null;  // Connection object to hold the database connection
	public static Connection ConnectDb() {  // Method to establish a database connection
		 // Database connection parameters
		String user = "cis3270";
		String password = "Flightproject!";
		String url = "jdbc:mysql://cis3270project.mysql.database.azure.com/project_data";
		
		try { //handle potential errors or exceptions that might occur during the execution of the code inside the try block.  

			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection conn = (Connection)DriverManager.getConnection(url, user, password);
			return conn;
			
		} catch(Exception e) { // Handle connection failure
			System.out.println("Could not establish connection");
			return null;
		}
	}
	
	
	// Method to retrieve flight data from the database
	public static ObservableList<Flights> getDataFlights() {
		Connection conn = ConnectDb();
		ObservableList<Flights> list = FXCollections.observableArrayList(); // Create an ObservableList to store flight data
		
		try {
			//execute sql query to retrieve flight info: FlightId, Price, FromLoc, ToLoc, Cabin, Airline, Depart, Arrive
			PreparedStatement ps = conn.prepareStatement("select * From flights");
			ResultSet resultSet = ps.executeQuery();
			
			// Process the result set and populate the ObservableList with Flight objects
			while (resultSet.next()) {
				list.add(new Flights(
						Integer.parseInt(resultSet.getString("FlightId")), 
						Integer.parseInt(resultSet.getString("Price")), 
						Integer.parseInt(resultSet.getString("SeatsAvailable")),
						resultSet.getString("FromLoc"), 
						resultSet.getString("ToLoc"), 
						resultSet.getString("Cabin"), 
						resultSet.getString("Airline"), 
						resultSet.getString("Depart"), 
						resultSet.getString("Arrive")));
			}
			
		} catch (Exception e) {
			System.out.println("Could not get data"); // Handle failure to retrieve data
		} 
		
		return list;
	}
	

}
	

