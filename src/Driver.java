import java.util.Scanner;
public class Driver
{


	  public static void main(String[] argv) {
		  Scanner scan = new Scanner(System.in);
		  AppInterface app = new AppInterface();
		  System.out.println("Choose an option:\n"
					+ " (a): Access as admin.\n"
					+ " (u): Access as user");
		  if (app.isConnected())
		  {
			  String input = scan.nextLine();
			  if(input.equals("a")) {
				  app.startAdmin();
			  }
			  else if(input.equals("u")) {
				  app.startUser();
			  }
				  
		  }
		  else 
			  System.out.println("App failed to start");
	  }
  
}