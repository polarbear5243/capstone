import java.io.IOException;

import AppDB.GroceryDB;
import AppServer.AppServerThread;

public class Server {
	
	static AppServerThread mAppServer;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			GroceryDB.initDB();
			
			System.out.println("DB OPEN");
			
			mAppServer = new AppServerThread();
			mAppServer.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}