package AppDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*
 * GroceryDB.java
 * 서버가 MySQL 데이터베이스에 연결하기 위해 필요한 클래스이다.
 * 서버 초기화 작업 시 초기화되고 LoginDB, RecipeDB, ingredientDB 의 Connection 을 제공한다.
 * */

public class GroceryDB {
	private final static String dbURL = "jdbc:mysql://127.0.0.1:3306/grocerydb?useSSL=false";
	private final static String user = "root";
	private final static String password = "gns930714!";
	
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
	
	static public IngredientDB getIngredientDB() throws SQLException{
		return new IngredientDB(mConnect);
	}
	
	static public RecipeDB getRecipeDB() throws SQLException{
		return new RecipeDB(mConnect);
	}
}//end of GroceryDB
