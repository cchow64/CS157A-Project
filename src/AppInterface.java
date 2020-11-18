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
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/theater?serverTimezone=UTC","root", "root");
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
				+ " (exit): Leave the app"
				+ "\n");
	}
	
	
	private void dispAMenu() {
		System.out.println("Choose an option:\n"
				+ " (einfo): Show employee's information.\n"
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
}