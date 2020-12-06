import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Matcher; 
import java.util.regex.Pattern; 
import java.util.HashSet;
public class AppInterface {
	
	private Connection connection = null;
	private Statement stmt = null;
	private PreparedStatement pstmt = null;
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
		System.out.println("\nChoose an option:\n"
				+ " (r) : Make reservation\n"
				+ " (seatstatus) : Return seats that are reserved\n"
				+ " (cinfo) : Display customer information\n"
				+ " (5star) : Display movies with 5 stars\n"
				+ " (roomtitle) : Display title of movies playing in each room\n"
				+ " (4star) : Display title and ID of movie with at least 4 stars and a running time of less than 2 hours\n"
				+ " (create) : Create customer account\n"
				+ " (ongoingmovie) : Display ongoing movies\n"
				+ " (exit): Leave the app");
	}
	
	
	private void dispAMenu() {
		System.out.println("\nChoose an option:\n"
				+ " (einfo): Show employee's information.\n"
				+ " (cname): Show customer names that appear more than once.\n"
				+ " (avgage): Show average age of customers.\n"
				+ " (numreservation): Show number of reservations per customer.\n"
				+ " (archive): Archive screenings.\n"
				+ " (exit): Leave the app");
	}
	
	
	private boolean uHandler(String s) {
		s = s.toLowerCase();
		
		if (s.equals("r")){
			reservation();
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
		else if (s.equals("create")) 
		{
			createCustomer();
			//getCInfo();
		}
		
		else if(s.equals("ongoingmovie")) {
			showCurrentMovies();
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
		
		else if (s.equals("archive"))
		{
			phaseOut();
		}
		
		else if (s.equals("numreservation"))
		{
			numReservations();
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
    			System.out.printf("\nEmployee's ID: %d  | Employee's name: %s | Employee's role: %s\n ", rs.getInt("eid"), rs.getString("name"), rs.getString("role"));
    		}
		}
		catch (SQLException e) {
			System.out.println("Error creating statement: " + e.getMessage());
		}
	}
	
	private void getSeatStatus() {
		try {
    		stmt = connection.createStatement();
    		rs = stmt.executeQuery("SELECT * FROM Seat where status = 'reserved' ");
    		while (rs.next()) {
    			System.out.printf("Seat rID: %d  |  Seat ID: %d  |  Seat status: %s\n", rs.getInt("roomID"), rs.getInt("seatID"), rs.getString("status"));
    		}
		}
		catch (SQLException e) {
			System.out.println("Error creating statement: " + e.getMessage());
		}
	}
	
	private void getCInfo() {
		try {
    		stmt = connection.createStatement();
    		rs = stmt.executeQuery("SELECT * FROM Customer");
    		System.out.print("Customer ID	|	Name		|	Age\n");
    		while (rs.next()) {
    			System.out.printf("%d		|	%s		|	%d\n", rs.getInt("cID"), rs.getString("name"), rs.getInt("Age"));
    		}
		}
		catch (SQLException e) {
			System.out.println("Error creating statement: " + e.getMessage());
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
			System.out.println("Error creating statement: " + e.getMessage());
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
			System.out.println("Error creating statement: " + e.getMessage());
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
			System.out.println("Error creating statement: " + e.getMessage());
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
			System.out.println("Error creating statement: " + e.getMessage());
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
	
	private void showCurrentMovies() {
		try {
    		stmt = connection.createStatement();
    		rs = stmt.executeQuery("SELECT title FROM Movie WHERE endDate > NOW()");
    		while (rs.next()) {
    			System.out.printf("Movie Title: %s \n" , rs.getString("title"));
    		}
		}
		catch (SQLException e) {
			System.out.println("Error creating statement: " + e.getMessage());
		}
	}
	
	private void numReservations() {
		try {
    		stmt = connection.createStatement();
    		rs = stmt.executeQuery("Select name, count(*) From Customer, Reservation where customer.cID = reservation.cID group by reservation.cID");
    		while (rs.next()) {
    			System.out.printf("Customer Name: %s   | # of reservations: %d \n", rs.getString("name"), rs.getInt("count(*)"));
    		}
		}
		catch (SQLException e) {
			System.out.println("Error creating statement: " + e.getMessage());
		}
	}
	
	

	
	
	private void createCustomer() {
		try {
    		stmt = connection.createStatement();
    		Scanner scan1 = new Scanner(System.in);
    		System.out.println("Enter your name: ");
    		String nameOfCustomer = scan1.nextLine();
    		System.out.println("Enter your age: ");
    		int ageOfCustomer = scan1.nextInt();
    		stmt.executeUpdate("INSERT INTO Customer(Name, age) VALUES('"+ nameOfCustomer + "', " + ageOfCustomer + ")");
    		rs = stmt.executeQuery("SELECT max(cID) From Customer");
    		int tempCID = -1;
    		while(rs.next())
    		{
    			tempCID = rs.getInt("max(cID)");
    		}
    		System.out.println(
    				"Successfully created the account"
    				+ "\n name: " + nameOfCustomer 
    				+ "\n age:  " + ageOfCustomer 
    				+ "\n ID:   " + tempCID);
		}
		catch (SQLException e) {
			System.out.println("Error creating statement: " + e.getMessage());
		}
	}
	
	private void phaseOut() {
		String date;

		System.out.println("Please input a cutoff date to archive old screenings.\n"
				+ "Format should be mm/dd/yyyy: ");
		date = scan.nextLine();
		while (!isValidDate(date)) {
			System.out.println("Improper format, try again. mm/dd/yyyy");
			date = scan.nextLine();
		}

		try {
			pstmt = connection.prepareStatement("CALL PhaseScreeningOut(STR_TO_DATE(?, '%m/%d/%Y'))");
			pstmt.setString(1, date);
			pstmt.executeQuery();
			System.out.printf("Succesfully archived Screenings older than %s.\n", date);
		}
		catch (SQLException e) {
			System.out.println("Error creating statement: " + e.getMessage());
		}
		
	}
	
	private void reservation() {
		int cID, mID, roomID, seatID, screenID;
		String selectedMovie, name, showTime;
		HashSet<Integer> availableM = new HashSet<Integer>();
		HashSet<Integer> screenings = new HashSet<Integer>();
		HashSet<Integer> seats = new HashSet<Integer>();
		
		try {
			stmt = connection.createStatement();
			rs = stmt.executeQuery("SELECT Movie.mID, title, rating, duration FROM Movie, Screening, Room, Seat "
				+	"WHERE Movie.mID = Screening.mID AND Screening.mID = Movie.mID AND Seat.roomID = Room.roomID "
				+	"AND Seat.status = 'vacant' GROUP BY Movie.mID");
			System.out.println("\nCurrently, there are available seats for the following movies:");
			while (rs.next()) {
				System.out.printf("		\"%s\"  | Duration: %d |  Rated: %S  |  ID: %d\n",
					rs.getString("title"), rs.getInt("duration"), rs.getString("rating"), rs.getInt("Movie.mID"));
				availableM.add(rs.getInt("Movie.mID"));
			}
			System.out.println("Enter the ID of the movie you want to make a reservation for");
			
			try {
				mID = Integer.parseInt(scan.nextLine());
				while (!availableM.contains(mID)) {
					System.out.println("Incorrect movie ID, try again");
				}
			}
			catch (NumberFormatException e) {
				System.out.println("Expected a number but input was different, cancelling reservation.");
				return;
			}
			
			pstmt = connection.prepareStatement("SELECT title FROM Movie WHERE mID = ?"); 
			pstmt.setString(1, Integer.toString(mID));
			rs = pstmt.executeQuery();
			rs.next();
			selectedMovie = rs.getString("title");
			
			pstmt = connection.prepareStatement("SELECT showingTime, screenID FROM Screening WHERE mID = ?");
			pstmt.setString(1, Integer.toString(mID));
			rs = pstmt.executeQuery();
			System.out.printf("\nFor the movie \"%s\" we have the following showing times: \n", selectedMovie);
			while (rs.next()) {
				screenings.add(rs.getInt("screenID"));
				showTime = new SimpleDateFormat("EEE, MMM d, HH:mm a").format(rs.getTimestamp("ShowingTime"));
				System.out.printf("%s	|	ID = %d\n", showTime, rs.getInt("screenID"));
			}
			if (screenings.isEmpty()) {
				System.out.println("Oops, seems like there are no available showing times, check again later");
				return;
			}
			
			System.out.printf("\nEnter the ID of the showing time for \"%s\".\n", selectedMovie);
			
			try {
				screenID = Integer.parseInt(scan.nextLine());
				while (!screenings.contains(screenID)) {
					System.out.println("Incorrect showing time ID, try again");
				}
			}
			catch (NumberFormatException e) {
				System.out.println("Expected a number but input was different, cancelling reservation.");
				return;
			}
			
			pstmt = connection.prepareStatement("SELECT roomID FROM Screening WHERE screenID = ?");
			pstmt.setString(1, Integer.toString(screenID));
			rs = pstmt.executeQuery();
			rs.next();
			roomID = rs.getInt("roomID");
			System.out.println("\nBelow is a list of seats available, choose one: ");
			pstmt = connection.prepareStatement("SELECT seatID, status FROM Seat WHERE RoomID = ? ORDER BY seatID");
			pstmt.setString(1, Integer.toString(roomID));
			rs = pstmt.executeQuery();
			System.out.println("------------ SCREEN LOCATED HERE ---------- ");
			while (rs.next()) {
				for (int i = 0; i < 10; i++) {
					boolean vacant = rs.getString("status").equals("vacant") ? true : false;
					if (vacant) {
						System.out.printf("{%02d}    ", rs.getInt("seatID"));
						seats.add(rs.getInt("seatID"));
					}
					else {
						System.out.print("{RR}    ");
					}
	
					if (i != 9) rs.next();
				}
				System.out.println();
				
			}
			
			try {
				seatID = Integer.parseInt(scan.nextLine());
				while (!seats.contains(seatID)) {
					System.out.println("Incorrect seat ID, try again");
				}
			}
			catch (NumberFormatException e) {
				System.out.println("Expected a number but input was different, cancelling reservation.");
				return;
			}
			
			System.out.println("\nEnter the customer's id to finalize the reservation: ");
			try {
				cID = Integer.parseInt(scan.nextLine());
			}
			catch (NumberFormatException e) {
				System.out.println("Expected a number but input was different, cancelling reservation.");
				return;
			}
			pstmt = connection.prepareStatement("SELECT name FROM Customer WHERE cID = ?");
			pstmt.setString(1,  Integer.toString(cID));
			rs = pstmt.executeQuery();
			rs.next();
			name = rs.getString("name");
			
			
			// Reservation procedure happens here
			pstmt = connection.prepareStatement("CALL reserve(?, ?, ?, ?)");
			pstmt.setString(1, Integer.toString(cID));
			pstmt.setString(2, Integer.toString(mID));
			pstmt.setString(3, Integer.toString(screenID));
			pstmt.setString(4, Integer.toString(seatID));
			rs = pstmt.executeQuery();
			
			stmt = connection.createStatement();
			rs = stmt.executeQuery("SELECT showingTime FROM Screening");
			rs.next();
			showTime = new SimpleDateFormat("EEE, MMM d, HH:mm a").format(rs.getTimestamp("ShowingTime"));
			System.out.printf("\nReservation sucessfully made for customer %s. Below are the details.\n"
					+ "	Movie: %s\n"
					+ "	Date and time: %s\n"
					+ "	assigned seat: %d\n"
					+ "	assigned room: %d\n",
					name, selectedMovie, showTime, seatID, roomID);
			
		}
		
		catch (SQLException e) {
			System.out.println("Error creating statement: " + e.getMessage());
		}
	}
	
	/**
	 * Checks whether string is in a valid date format
	 * @param d the date
	 * @return boolean value
	 */
	private boolean isValidDate(String d) 
    { 
        String regex = "^(1[0-2]|0[1-9])/(3[01]"
                       + "|[12][0-9]|0[1-9])/[0-9]{4}$"; 
        Pattern pattern = Pattern.compile(regex); 
        Matcher matcher = pattern.matcher((CharSequence)d); 
        return matcher.matches();
    } 
	
	
}