package application;

public class Flights {
	
	public Flights(int flightId, int price, int seats, String from, String to, String cabin, String airline, String departTime,String arriveTime) {
		super();
		this.flightId = flightId;
		this.price = price;
		this.seats = seats;
		this.from = from;
		this.to = to;
		this.cabin = cabin;
		this.airline = airline;
		this.departTime = departTime;
		this.arriveTime = arriveTime;
	}
	
	public Integer flightId;
	public Integer price;
	Integer seats;
	String from, to, cabin, airline;
	String departTime;
	String arriveTime;
	
	//Getters and Setters
	public int getFlightId() {
		return flightId;
	}
	public void setFlightId(int flightId) {
		this.flightId = flightId;
	}
	public int getSeats() {
		return seats;
	}
	public void setSeats(int seats) {
		this.seats = seats;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getCabin() {
		return cabin;
	}
	public void setCabin(String cabin) {
		this.cabin = cabin;
	}
	public String getAirline() {
		return airline;
	}
	public void setAirline(String airline) {
		this.airline = airline;
	}
	public String getDepartTime() {
		return departTime;
	}
	public void setDepartTime(String departTime) {
		this.departTime = departTime;
	}
	public String getArriveTime() {
		return arriveTime;
	}
	public void setArriveTime(String arriveTime) {
		this.arriveTime = arriveTime;
	}
	

}
