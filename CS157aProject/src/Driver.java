import java.sql.*;
import java.util.Scanner;

public class Driver
{

  public static void main(String[] argv) {

        System.out.println("-------- MySQL JDBC Connection Testing ------------");
        System.out.println("MySQL JDBC Driver Registered!");
        Connection connection = null;

        try {
               connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/theater?serverTimezone=UTC","root", "myfirstdb");
        	

        } catch (SQLException e) {
                System.out.println("Connection Failed! Check output console");
                e.printStackTrace();
                return;
        }

        if (connection != null) {
                System.out.println("You made it, take control your database now!");
        } else {
                System.out.println("Failed to make connection!");
        }
        
        Statement stmt = null;
        ResultSet rs = null;
        System.out.println("Type \"info\" to get employee's id and their role, or 'e' to exit.\n");
        Scanner scan = new Scanner(System.in);
        while (true) {
        	String input = scan.nextLine();
        	if (input.equals("e")) {
        		scan.close();
        		System.out.println("See you later!");
        		break;
        	}
        	else if (input.equals("info")) {
        		try {
	        		stmt = connection.createStatement();
	        		rs = stmt.executeQuery("SELECT * FROM Employee");
	        		while (rs.next()) {
	        			System.out.printf("Employee's ID: %d  |  Employee's role: %s\n", rs.getInt("eid"), rs.getString("role"));
	        		}
        		}
        		catch (SQLException e) {
        			System.out.println("Error creating statement");
        		}
        	}
        
        	System.out.println("Type \"info\" to get employee's id and their role, or 'e' to exit.\n");
        }
  }
}