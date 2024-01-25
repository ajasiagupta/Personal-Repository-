package application;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class accountController implements Initializable{

	@FXML // @fxml references to JavaFX components (buttons and text fields) defined in the associated FXML file allowing the controller to interact with the components. 
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
	private TableView<Flights> bookedFlightTable;
    

    @FXML
    private TextField txtAddress;

    @FXML
    private TextArea txtAnswer;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtFirst;

    @FXML
    private TextField txtLast;

    @FXML
    private TextArea txtQuestion;

    @FXML
    private TextField txtUsername;
    
    @FXML
    private Label labelType;
    
    
 // ObservableList to store flight data
    ObservableList<Flights> listM;
    
 // Database connection and statement objects
	Connection conn = null;
	ResultSet rs = null;
	PreparedStatement pst = null;
	Integer index;
	String[] sqlBookedFlights = flightsController.getUserBookedFlights().split(", ");  // Array of flight IDs booked by the user

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// setting up table view for cells to be populated
		colNum.setCellValueFactory(new PropertyValueFactory<Flights, Integer>("FlightId"));
		colPrice.setCellValueFactory(new PropertyValueFactory<Flights, Integer>("price"));
		colSeats.setCellValueFactory(new PropertyValueFactory<Flights, Integer>("seats"));
		colFrom.setCellValueFactory(new PropertyValueFactory<Flights, String>("from"));
		colTo.setCellValueFactory(new PropertyValueFactory<Flights, String>("to"));
		colCabin.setCellValueFactory(new PropertyValueFactory<Flights, String>("cabin"));
		colAirline.setCellValueFactory(new PropertyValueFactory<Flights, String>("airline"));
		colArrive.setCellValueFactory(new PropertyValueFactory<Flights, Date>("arriveTime"));
		colDepart.setCellValueFactory(new PropertyValueFactory<Flights, Date>("departTime"));
		
		// uses an list with flight objects as items to populate table
		listM = getDataFlights();
		bookedFlightTable.setItems(listM);
		
		
		
		// Setting account information into txt areas
		try {
			conn = mySQLConnection.ConnectDb();
			// Gets all information of current user 
			PreparedStatement ps = conn.prepareStatement("select * from users where ssn=" + "\'" + WelcomeController.getCurrentUser() + "\'");
			rs = ps.executeQuery();
			
			// gets and sets the account information to the values from the result set
			while (rs.next()) {
				String first = rs.getString(2);
				String last = rs.getString(3);
				String username = rs.getString(8);
				String address = rs.getString(5) + ", " + rs.getString(6) + ", " + rs.getString(7);
				String email = rs.getString(4);
				String securityQ = rs.getString(10);
				String securityA = rs.getString(11);
				
				if (rs.getInt(12) == 0) {
					labelType.setText("Customer");
				} else if (rs.getInt(12) == 1) {
					labelType.setText("Administrator");
				}
				
				txtFirst.setText(first);
				txtLast.setText(last);
				txtUsername.setText(username);
				txtAddress.setText(address);
				txtEmail.setText(email);
				txtQuestion.setText(securityQ);
				txtAnswer.setText(securityA);
			}
			

			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


	///////////////// Showing Booked Flights into Table /////////////////////
	public ObservableList<Flights> getDataFlights() {
		Connection conn = mySQLConnection.ConnectDb();
		ObservableList<Flights> bookedList = FXCollections.observableArrayList();		
		
		String queryFlights = "";
		
		// Looping through to decide to add a comma or not (no comma for last flight)
		for (int i = 0; i < sqlBookedFlights.length; i++) {
			if(i == sqlBookedFlights.length - 1) {
				queryFlights = queryFlights + sqlBookedFlights[i];
			} else {
				queryFlights = queryFlights + sqlBookedFlights[i] + ",";
			}
		}

		try {
			PreparedStatement ps = conn.prepareStatement("select * From flights where flightId in (" + queryFlights + ")");
			ResultSet resultSet = ps.executeQuery();
			
			while (resultSet.next()) {
				// getting flights objects into list
				bookedList.add(new Flights(
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
			System.out.println("Could not get data");
		} 
		
		return bookedList;
	}


    //////////////////////////////////// Navigation to Home and Admin Pages///////////////////////////////////////
    private Stage stage;
	private Scene scene;	
	private Parent root;
    
	//Switch to home flight scene
	public void switchToHome (ActionEvent event) {
		try {
			root = FXMLLoader.load(getClass().getResource("Main.fxml"));
			stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			scene = new Scene(root,1130,700);
			stage.setScene(scene);
			stage.show();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//Switch to admin scene
	public void switchToAdmin (ActionEvent event) {
		//Checking if user is admin or customer
		try {
			pst = conn.prepareStatement("select admin From users where ssn= " + "\'" + WelcomeController.getCurrentUser() + "\'");
			rs = pst.executeQuery();
			int status = 0;
			while (rs.next()) {
				//getting either 0 or 1 into status from the sql query
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
	
	
	// Logout
	public void switchToLogin (ActionEvent event) {
		try {
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

	
	//Deleting flights from account
	public void deleteFlight() {
		index = bookedFlightTable.getSelectionModel().getSelectedIndex();
		String flightToDelete = colNum.getCellData(index).toString();
		
		// Creating array list so can remove the flight chosen
		ArrayList<String> flightsList = new ArrayList<>(Arrays.asList(sqlBookedFlights));
		System.out.println(flightsList);
		
		// removing flight
		for (int i = 0; i < flightsList.size(); i++) {
			if (flightsList.get(i).equals(flightToDelete)) {
				flightsList.remove(i);
				
				//Putting array list back into string form
				String UpdatedFlights = String.join(", ", flightsList);
				System.out.println(UpdatedFlights);
				
				// Updating database
				PreparedStatement ps;
				try {
					ps = conn.prepareStatement("Update users set bookedFlights =" + "\'" + UpdatedFlights + "\'" + " where ssn= " + "\'" + WelcomeController.getCurrentUser() + "\'");
					ps.execute();
					JOptionPane.showMessageDialog(null, "Flight Deleted. Please refresh to see updated flights");
					
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(null, "Failed to Delete");;
				}
				
			}
		}
	}
	
	
	
    
    
}
