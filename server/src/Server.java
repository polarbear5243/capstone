import java.io.IOException;
import java.sql.SQLException;

import AppDB.Graph;
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
	/*		try {
				Graph myGraph = new Graph(GroceryDB.getRecipeDB(),GroceryDB.getIngredientDB());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}

}
