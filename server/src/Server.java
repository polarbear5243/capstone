import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

import AppDB.GroceryDB;
import AppDB.Ingredient;
import AppDB.ingredientDB;
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
