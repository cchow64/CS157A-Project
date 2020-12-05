import java.sql.*;
import java.util.Scanner;
public class AppInterface {
	
	private Connection connection = null;
	private Statement stmt = null;
	private ResultSet rs = null;
	
	private Scanner scan = null;
	
	/**
	 * Class constructor
	 */
	public AppInterface() {
		
		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/theater?serverTimezone=UTC","root", "myfirstdb");
		} 
		catch (SQLException e) {
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return;
		}

		if (connection != null) {
			scan = new Scanner(System.in);
			System.out.println("Connected succesfully to the theater management System.");
		} 
		else 
		{
			System.out.println("Error: Can't connect to the system.");
		}
	}
	
	public void startAdmin() {
			String input = null;
			boolean keepRunning = true;
			while (keepRunning) {
				dispAMenu();
				input = scan.nextLine();
				keepRunning = aHandler(input);
				System.out.println("\n\n");
			}
	}

	public void startUser() {
		String input = null;
		boolean keepRunning = true;
		while (keepRunning) {
			dispUMenu();
			input = scan.nextLine();
			keepRunning = uHandler(input);
		}
	}
	
	public boolean isConnected () {
		return connection == null ? false : true;
	}
	
	private void close() {
		try {
			if (rs != null) { rs.close(); }
			if (stmt != null) {stmt.close(); }
			if (connection != null) {connection.close(); }
		}
		catch (SQLException e) {
			System.out.println("Error closing off resources, stack trace below:");
			e.printStackTrace();
		}
		finally {
			System.out.println("Application closed, see you later!");
		}
	}
	
	
	private void dispUMenu() {
		System.out.println("Choose an option:\n"
				+ " (r) : Make reservation\n"
				+ " (seatstatus) : Return seats that are reserved\n"
				+ " (cinfo) : Display customer information\n"
				+ " (5star) : Display movies with 5 stars\n"
				+ " (roomtitle) : Display title of movies playing in each room\n"
				+ " (4star) : Display title and ID of movie with at least 4 stars and a running time of less than 2 hours\n"
				+ " (exit): Leave the app"
				+ "\n");
	}
	
	
	private void dispAMenu() {
		System.out.println("Choose an option:\n"
				+ " (einfo): Show employee's information.\n"
				+ " (cname): Show customer names that appear more than once.\n"
				+ " (avgage): Show average age of csutomers.\n"
				+ " (exit): Leave the app"
				+ "\n");
	}
	
	
	private boolean uHandler(String s) {
		s = s.toLowerCase();
		
		if (s.equals("r")){
			//dispReservationMenu();
		}
		else if(s.equals("seatstatus"))
		{
			getSeatStatus();
		}
		
		else if(s.equals("cinfo"))
		{
			getCInfo();
		}
		else if(s.equals("5star"))
		{
			get5Star(); 
		}
		else if(s.equals("roomtitle"))
		{
			roomTitle(); 
		}
		
		else if(s.equals("4star"))
		{
			get4Star(); 
		}
		
		else if (s.equals("exit")) {
			this.close();
			return false;
		}
		
		else {
			System.out.println("Sorry, wrong input or improperly formatted, try again.");
		}
		
		return true;
	}
	
	
	private boolean aHandler(String s) {
		if (s.equals("einfo")) {
			getEInfo();
		}
		
		else if (s.equals("cname")) {
			cName();
		}
		
		else if (s.equals("avgage")) {
			getAvgAge();
		}
		
		else if (s.equals("exit")) {
			this.close();
			return false;
		}
		
		
		
		else {
			System.out.println("Sorry, wrong input or improperly formatted, try again.");
		}
		
		return true;
	}
	
	/**
	 * Function retrieves employees IDs and their role
	 */
	private void getEInfo() {
		try {
    		stmt = connection.createStatement();
    		rs = stmt.executeQuery("SELECT * FROM Employee");
    		while (rs.next()) {
    			System.out.printf("Employee's ID: %d  | Employee's name: %s | Employee's role: %s\n ", rs.getInt("eid"), rs.getString("name"), rs.getString("role"));
    		}
		}
		catch (SQLException e) {
			System.out.println("Error creating statement");
		}
	}
	
	private void getSeatStatus() {
		try {
    		stmt = connection.createStatement();
    		rs = stmt.executeQuery("SELECT * FROM Seats where status = 'reserved' ");
    		while (rs.next()) {
    			System.out.printf("Seat rID: %d  |  Seat ID: %d  |  Seat status: %s\n", rs.getInt("rID"), rs.getInt("seatID"), rs.getString("status"));
    		}
		}
		catch (SQLException e) {
			System.out.println("Error creating statement");
		}
	}
	
	private void getCInfo() {
		try {
    		stmt = connection.createStatement();
    		rs = stmt.executeQuery("SELECT * FROM Customer");
    		while (rs.next()) {
    			System.out.printf("Customer ID: %d  |  Customer rID: %d  |  Customer name: %s\n", rs.getInt("cID"), rs.getInt("reservID"), rs.getString("name"));
    		}
		}
		catch (SQLException e) {
			System.out.println("Error creating statement");
		}
		
		
	}
	
	private void get5Star() {
		try {
    		stmt = connection.createStatement();
    		rs = stmt.executeQuery("SELECT distinct Movie.mID, Movie.title FROM Movie, MovieStats where Movie.mID in (SELECT MovieStats.mID FROM MovieStats where stars = 5)");
    		while (rs.next()) {
    			System.out.printf("Movie ID: %d  | Movie title: %s\n", rs.getInt("mID"), rs.getString("title"));
    		}
		}
		catch (SQLException e) {
			System.out.println("Error creating statement");
		}
	}
	
	private void roomTitle() {
		try {
    		stmt = connection.createStatement();
    		rs = stmt.executeQuery("SELECT roomID, title FROM Movie Left Outer Join Screening on Movie.mID = Screening.mID WHERE roomID IS NOT NULL");
    		while (rs.next()) {
    			System.out.printf("Room ID: %d  | Movie title: %s\n", rs.getInt("roomID"), rs.getString("title"));
    		}
		}
		catch (SQLException e) {
			System.out.println("Error creating statement");
		}
	}
	
	private void cName() {
		try {
    		stmt = connection.createStatement();
    		rs = stmt.executeQuery("SELECT cID, name FROM Customer old WHERE cID != ANY (SELECT cID FROM Customer WHERE name = old.name)");
    		while (rs.next()) {
    			System.out.printf("Customer ID: %d  | Customer name: %s\n", rs.getInt("cID"), rs.getString("name"));
    		}
		}
		catch (SQLException e) {
			System.out.println("Error creating statement");
		}
	}
	
	private void getAvgAge() {
		try {
    		stmt = connection.createStatement();
    		rs = stmt.executeQuery("SELECT avg(age) FROM Customer");
    		while (rs.next()) {
    			System.out.printf("Average age: %f ", rs.getFloat("avg(age)"));
    		}
		}
		catch (SQLException e) {
			System.out.println("Error creating statement");
		}
	}
	
	private void get4Star() {
		try {
    		stmt = connection.createStatement();
    		rs = stmt.executeQuery("SELECT Movie.mID, title FROM Movie, MovieStats WHERE Movie.mID = MovieStats.mID and stars >=4 and duration < 120");
    		while (rs.next()) {
    			System.out.printf("Movie ID: %d  | Movie title: %s\n" , rs.getInt("Movie.mID"), rs.getString("title"));
    		}
		}
		catch (SQLException e) {
			System.out.println("Error creating statement: " + e.getMessage());
		}
	}
	
	
}