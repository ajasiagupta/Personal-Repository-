package application;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;


public class flightsController implements Initializable{
	
	/////////////////////////////////////// Scene Switch////////////////////////////////////////
	private Stage stage;
	private Scene scene;	
	private Parent root;
	
	//Switch to home flight scene
	public void switchToHome (ActionEvent event) {
		try {
			root = FXMLLoader.load(getClass().getResource("Main.fxml")); // Load Main.fxml when switching to the home flight scene
			// Get the current stage and set a new scene with the loaded root
			stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			scene = new Scene(root,1130,700);
			stage.setScene(scene);
			stage.show();
			
		} catch (IOException e) { // Handle IOException (thrown for input/output data like reading files) , for example, print the stack trace
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//Switch to admin scene
	public void switchToAdmin (ActionEvent event) {
		//Checking if user is admin or customer
		try {
			// Prepare and execute a SQL query to check if the user is an admin
			pst = conn.prepareStatement("select admin From users where ssn= " + "\'" + WelcomeController.getCurrentUser() + "\'");
			rs = pst.executeQuery();
			int status = 0;
			while (rs.next()) {
				//admin status...getting either 0 or 1 into status from the sql query result
				status = Integer.parseInt(rs.getString(1));
			}
			
			// If status is 1 then the user is an admin and can access
			if (status == 1) {
				root = FXMLLoader.load(getClass().getResource("Admin.fxml"));
				stage = (Stage)((Node)event.getSource()).getScene().getWindow();
				scene = new Scene(root,1130,700);
				stage.setScene(scene);
				stage.show();
			} else {
				JOptionPane.showMessageDialog(null, "Access Denied. Only Admin users have access");
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	//Switch to Account Scene
	public void switchToAccount (ActionEvent event) {
		try {
			 // Load Account.fxml when switching to the Account scene
			root = FXMLLoader.load(getClass().getResource("Account.fxml"));
			stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			scene = new Scene(root,1130,700);
			stage.setScene(scene);
			stage.show();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// Logout
	public void switchToLogin (ActionEvent event) {
		try {
			 // Load log-in.fxml when logging out
			root = FXMLLoader.load(getClass().getResource("log-in.fxml"));
			stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			scene = new Scene(root,1130,700);
			stage.setScene(scene);
			stage.show();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	///////////////////////////////// Flights Table information ///////////////////////////////
	@FXML
	private TableColumn<Flights, Integer> colNum;
	
	@FXML
	private TableColumn<Flights, Integer> colPrice;
	
	@FXML
	private TableColumn<Flights, String> colFrom;
	
	@FXML
	private TableColumn<Flights, String> colTo;
	
	@FXML
	private TableColumn<Flights, Date> colDepart;
	
	@FXML
	private TableColumn<Flights, Date> colArrive;
	
	@FXML
	private TableColumn<Flights, String> colCabin;
	
	@FXML
	private TableColumn<Flights, String> colAirline;
	
	@FXML
	private TableColumn<Flights, Integer> colSeats;

	@FXML
	private TableView<Flights> flightTable;
	
	
	ObservableList<Flights> listM;  // ObservableList to store flight data
		
	// Database connection and statement objects
	static Connection conn = mySQLConnection.ConnectDb();
	static ResultSet rs = null;
	static PreparedStatement pst = null;
	

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		//Sets up table view cells 
		colNum.setCellValueFactory(new PropertyValueFactory<Flights, Integer>("flightId"));
		colPrice.setCellValueFactory(new PropertyValueFactory<Flights, Integer>("price"));
		colSeats.setCellValueFactory(new PropertyValueFactory<Flights, Integer>("seats"));
		colFrom.setCellValueFactory(new PropertyValueFactory<Flights, String>("from"));
		colTo.setCellValueFactory(new PropertyValueFactory<Flights, String>("to"));
		colCabin.setCellValueFactory(new PropertyValueFactory<Flights, String>("cabin"));
		colAirline.setCellValueFactory(new PropertyValueFactory<Flights, String>("airline"));
		colArrive.setCellValueFactory(new PropertyValueFactory<Flights, Date>("arriveTime"));
		colDepart.setCellValueFactory(new PropertyValueFactory<Flights, Date>("departTime"));
			
		//Uses a list of flight objects to populate the table view
		listM = mySQLConnection.getDataFlights();
		flightTable.setItems(listM);
		
	}
	
	///////////////////////// On Click Show Flight Information on the side //////////////////////////////
	// TextFields for displaying flight information
	@FXML
	private TextField txtNum;
	
	@FXML
	private TextField txtSeats;
	
	@FXML
	private TextField txtAirline;

	@FXML
	private TextField txtCabin;
	
	@FXML
	private TextField txtDepart;

	@FXML
	private TextField txtArrive;
	
	@FXML
	private TextField txtFrom;

	@FXML
	private TextField txtPrice;

	@FXML
	private TextField txtTo;
	
	
	Integer index;
	
	@FXML
    void getFlight(MouseEvent event) {
		// gets the index of a selected flight
		index = flightTable.getSelectionModel().getSelectedIndex();
		
		if (index <= -1) {
			return;
		}
		//Sets the txt on the left side to the table cells corresponding to the txt
		txtNum.setText(colNum.getCellData(index).toString());
		txtPrice.setText(colPrice.getCellData(index).toString());
		txtSeats.setText(colSeats.getCellData(index).toString());
		txtAirline.setText(colAirline.getCellData(index).toString());
		txtCabin.setText(colCabin.getCellData(index).toString());
		txtFrom.setText(colFrom.getCellData(index).toString());
		txtTo.setText(colTo.getCellData(index).toString());
		
		txtDepart.setText((colDepart.getCellData(index) + "").toString());
		txtArrive.setText((colArrive.getCellData(index) + "").toString());
		
		
    }
	
	////////////////////////////////////// Admin controls ///////////////////////////////////////////
	
	//Update Flight
	public void updateFlight() {
		try {
			//getting the values of selected flight
			String num = txtNum.getText();
			String price = txtPrice.getText();
			String seats = txtSeats.getText();
			String airline = txtAirline.getText();
			String from = txtFrom.getText();
			String to = txtTo.getText();
			String depart = txtDepart.getText();
			String arrive = txtArrive.getText();
			String cabin = txtCabin.getText();
			
			//updates flights using an update query
			String sql = "update flights\n set flightId = " + num + 
											", Price= " + price +  
											", Airline= '" + airline + "'" +
											", FromLoc= '" + from +  "'" +
											", ToLoc= '" + to + "'" +
											", Depart= '" + depart +  "'" +
											", Arrive= '" + arrive + "'" +
											", cabin= '" + cabin + "'\n" +
											", SeatsAvailable= '" + seats + "'\n" +
											"where flightId= " + num;
			pst = conn.prepareStatement(sql);
			pst.execute();
			initialize(null, null);
			JOptionPane.showMessageDialog(null, "Flight " + num + " Updated");
 			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Failed to Update");
		}	
	}
	
	//Add Flight
	public void addFlight() {
		String sql = "insert into flights (FlightId, Price, Airline, FromLoc, ToLoc, Depart, Arrive, Cabin, SeatsAvailable)\n"
				+ "values(?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try {
			pst = conn.prepareStatement(sql);
			// Gets chosen flight info and creates a new flight in database with insert
			pst.setString(1, txtNum.getText());
			pst.setString(2, txtPrice.getText());
			pst.setString(3, txtAirline.getText());
			pst.setString(4, txtFrom.getText());
			pst.setString(5, txtTo.getText());
			pst.setString(6, txtDepart.getText());
			pst.setString(7, txtArrive.getText());
			pst.setString(8, txtCabin.getText());
			pst.setString(9, txtSeats.getText());
			pst.execute();
			
			initialize(null, null);
			
			JOptionPane.showMessageDialog(null, "Flight " + txtNum.getText() + " has been added");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e);
		}
	}
	
	//Delete Flight
	public void deleteFlight() {
		// delete selected flight where flight id matches
		String sql = "delete from flights where flightId = ?";
		try {
			pst = conn.prepareStatement(sql);
			pst.setString(1, txtNum.getText());
			pst.execute();
			initialize(null, null);
			
			JOptionPane.showMessageDialog(null, "Flight " + txtNum.getText() + " Deleted");
			
		}catch (Exception e) {
			JOptionPane.showMessageDialog(null, e);
		}
	}
	
	
	
	///////////////////////// Book Flights /////////////////////////////////////
	
	public static String getUserBookedFlights() {
		// used to get a string with the booked flights of current user
		String currentUser = "\'" + WelcomeController.getCurrentUser() + "\'";
		String bookedFlights = "";
		
		//Selects the string of booked flights of current user
		String sqlBooked = "Select bookedFlights from users where ssn=" + currentUser;
		try {
			pst = conn.prepareStatement(sqlBooked);
			rs = pst.executeQuery();

			while (rs.next()) {
				bookedFlights = rs.getString(1);
			}
			
		} catch (SQLException e) {
			System.out.println("Could not get user booked flights");
		}
		return bookedFlights;	
	}
		
	public void updateUserFlights() {
		// Adds the booked flight to the string bookedFlights in database
		String currentUser = "\'" + WelcomeController.getCurrentUser() + "\'";
		String flightsString = getUserBookedFlights();
		
		// If there are no booked flights just add the number, if there are booked flights add with comma
		if (flightsString.isEmpty()) {
			flightsString = txtNum.getText();
		} else {
			flightsString = flightsString + ", " + txtNum.getText();
		}
		
		//Sql query updates bookedFlights column in user
		String sql = "Update users "
					+ "set bookedFlights = " + "\'" + flightsString + "\' "
					+ "where ssn = " + currentUser;

		
		try {
			pst = conn.prepareStatement(sql);
			pst.execute();
			JOptionPane.showMessageDialog(null, "Flight " + txtNum.getText() + " Booked");
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Flight Booking Failed");
		}
		
	}
	
	public void updateFlightSeats() {
		// Sql query to update the seats available for flights
		String sql = "Update flights"
				+ " set seatsAvailable = " + (Integer.parseInt(txtSeats.getText()) - 1) // subtracts seats by 1 for a booked flight
				+ " where flightId = " + txtNum.getText();
		try {
		
			pst = conn.prepareStatement(sql);
			pst.execute();
						
			initialize(null, null);
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Failure to add flight");
		}
	}
		
	// What happens when we press book flight button
	public void bookFlight() {
		String bookedFlights = getUserBookedFlights();
		// checking if there are any booked flights yet
		if (!bookedFlights.isBlank()) {
			try {
				// Flight is already booked restriction if BookedFlight string contains the flight number
				if (bookedFlights.contains(txtNum.getText() + ", ")) {
					JOptionPane.showMessageDialog(null, "This flight is already booked");
					
				// Flight is full restriction if seats available == 0
				} else if (txtSeats.getText().equals("0")) {
					JOptionPane.showMessageDialog(null, "This flight is full");
				
				// Flight time conflicts with another flight time
				} else if (isOverlap(bookedFlights)){
					JOptionPane.showMessageDialog(null, "This flight conflicts with another booked flight");
				} else {
					updateUserFlights();
					updateFlightSeats();
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Could not book flight");
			}
		} else {
			updateUserFlights();
			updateFlightSeats();
		}
	}
	
	
	//method to determine if a bookedFlight's date time overlaps another
		public boolean isOverlap(String bookedFlights) throws ParseException, SQLException {
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    boolean isOverlap = false;
	    String[] bookedFlightsArray = bookedFlights.split(", ");

	    java.util.Date depart1 = dateFormat.parse(txtDepart.getText());
	    java.util.Date arrive1 = dateFormat.parse(txtArrive.getText());

	    for (String flight : bookedFlightsArray) {
	        PreparedStatement ps = conn.prepareStatement("select depart, arrive from flights where flightId= ?");
	        ps.setString(1, flight);
	        ResultSet rs = ps.executeQuery();

	        while (rs.next()) {
	            java.util.Date depart2 = dateFormat.parse(rs.getString("depart"));
	            java.util.Date arrive2 = dateFormat.parse(rs.getString("arrive"));

	            if ((depart1.before(arrive2) && arrive1.after(depart2)) ||
	                    (depart1.equals(depart2) || arrive1.equals(arrive2))) {
	                isOverlap = true;
	                break; // Found overlap, no need to check further
	            }
	        }

	        if (isOverlap) {
	            break; // Exit loop since overlap is found
	        }
	    }
	    return isOverlap;
	}
}
	
