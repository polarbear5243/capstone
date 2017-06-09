package myAppServer;
import java.io.IOException;
import java.sql.SQLException;

import AppDB.Graph;
import AppDB.GroceryDB;
import AppServer.AppServerThread;

/*
 * Server.java
 * ���� ���� �Լ��� ������ Ŭ�����̴�.
 * 
 * */

public class Server {
	
	static AppServerThread mAppServer;
	static Graph myGraph;
	
	public static Graph getGraph() { return myGraph; }
	
	public static void main(String[] args) {
		
		try {
			
			GroceryDB.initDB();
			
			System.out.println("DB OPEN");
			
			myGraph = new Graph(GroceryDB.getRecipeDB(),GroceryDB.getIngredientDB());
			
			mAppServer = new AppServerThread();
			mAppServer.start();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		
	}

}//end of Server
