public class Driver
{

	  public static void main(String[] argv) {
		  AppInterface app = new AppInterface();
		  if (app.isConnected())
		  	app.startAdmin();
		  else 
			  System.out.println("App failed to start");
	  }
  
}