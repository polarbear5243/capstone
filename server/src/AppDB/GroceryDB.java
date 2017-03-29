package AppDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class GroceryDB {
	private final static String dbURL = "jdbc:mysql://127.0.0.1:3306/grocerydb";
	private final static String user = "root";
	private final static String password = "a!353535";
//	private final static String password = "gns930714!";
	
	private static Connection mConnect = null;
	
	public static void initDB(){
		try{
			mConnect = DriverManager.getConnection(dbURL,user,password);
		}
		catch(SQLException e){
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
		}
	}
	static public LoginDB getLoginDB() throws SQLException{
		return new LoginDB(mConnect);
	}
}
