package AppDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ingredientDB {
	protected Connection mConnection = null;
	protected Statement mStatement = null;
	protected PreparedStatement manipulate = null;
	protected ResultSet mResult = null;
	
	public ingredientDB(Connection connection) throws SQLException{
		mStatement = connection.createStatement();
		mConnection = connection;
	}
	
	public ArrayList<String> getAllIngredientIDList() throws SQLException{
		ArrayList<String> result = new ArrayList<String>();
		mResult = mStatement.executeQuery("SELECT ingredientid From ingredient;");
		while(mResult.next()){
			result.add(Integer.toString(mResult.getInt(1)));
		}
		
		return result;
	}
	
	public byte[] getImageByIngredientId(String id) throws SQLException{
		mResult = mStatement.executeQuery("SELECT ingredientimage FROM ingredientimage WHERE ingredientid = " + id + ";");
		mResult.next();
		return mResult.getBytes("ingredientimage");
	}
	
	public ArrayList<Ingredient> getUserIngredientList(String userid) throws SQLException{
		ArrayList<Ingredient> result = new ArrayList<Ingredient>();
		mResult = mStatement.executeQuery("SELECT * FROM ingredient WHERE userid = '" + userid + "';");
		while(mResult.next()){
			Ingredient data = new Ingredient(
					Integer.toString(mResult.getInt("ingredientid")),
					(char)mResult.getByte("amount"),
					mResult.getDate("buydate"),
					mResult.getString("productname"),
					(char)mResult.getByte("favorite")
					);
			result.add(data);
		}
		return result;
	}
	
	public void insertUserIngredient(String userid, Ingredient data) throws SQLException{
		manipulate = mConnection.prepareStatement(
				"INSERT INTO myingredient(userid,ingredientid,amount,buydate,productname,favorite) "
				+ "VALUES(?,?,?,?,?,?)");
		manipulate.setString(1,userid);
		manipulate.setInt(2, Integer.parseInt(data.getIngredientID()));
		manipulate.setByte(3, (byte)data.getAmount() );
		manipulate.setDate(4, data.getBuyDate());
		manipulate.setString(5, data.getProductName());
		manipulate.setByte(6, (byte)data.getFavorite());
		
		manipulate.executeUpdate();		
	} 
	
	
	public void deleteUserIngredient(String userid, String productName) throws SQLException{
		mStatement.executeUpdate("DELETE FROM myingredient WHERE userid = '" + userid + "' AND productname = '"+ productName +"';");
	}	
	
	public void deleteAllUserIngredient(String userid) throws SQLException{
		mStatement.executeUpdate("DELETE FROM myingredient WHERE userid = '" + userid + "';");		
	}
	
	
	public void changeUserIngredientInfo(String userid, String productName, Ingredient data) throws SQLException{
		mStatement.executeUpdate("UPDATE myingredient "
				+ "SET amount = '"+ data.getAmount() 
				+ "', buydate = '" + data.getBuyDate()
				+ "', productName = '" + data.getProductName()
				+ "', favorite = '" + data.getFavorite()
				+ "WHERE userid = '" + userid + "' AND productname = '" + productName + "';");
	}
	
	public ArrayList<String> searchByIngredientName(String productName) throws SQLException{
		ArrayList<String> result = new ArrayList<String>();
		String target = productName.toUpperCase();
		String choseong = getChoseong(productName);
		
		if (target != "" && choseong == ""){
			//초성으로 검색하는 경우
			mResult = mStatement.executeQuery("SELECT ingredientid FROM realproduct "
					+ "WHERE choseongSearch(productname) LIKE concat('%'," + productName + ",'%');");
			while(mResult.next()){
				result.add(Integer.toString(mResult.getInt(1)));
			}
			mResult = mStatement.executeQuery("SELECT ingredientid FROM ingredient "
					+ "WHERE choseongSearch(bornname) LIKE concat('%'," + productName + ",'%');");
			while(mResult.next()){
				result.add(Integer.toString(mResult.getInt(1)));
			}
		}
		else {
			//일반 검색
			mResult = mStatement.executeQuery("SELECT ingredientid FROM realproduct "
					+ "WHERE productname LIKE concat('%'," + productName + ",'%');");
			while(mResult.next()){
				result.add(Integer.toString(mResult.getInt(1)));
			}
			mResult = mStatement.executeQuery("SELECT ingredientid FROM ingredient "
					+ "WHERE bornname LIKE concat('%'," + productName + ",'%');");
			while(mResult.next()){
				result.add(Integer.toString(mResult.getInt(1)));
			}
		}
		
		return result;
	}
	
	private String getChoseong(String data){
		String[] choseong = {"ㄱ","ㄲ","ㄴ","ㄷ","ㄸ","ㄹ","ㅁ","ㅂ","ㅃ",
				"ㅅ","ㅆ","ㅇ","ㅈ","ㅉ","ㅊ","ㅋ","ㅌ","ㅍ","ㅎ"};
		String result = "";
		int code;
		for (int i = 0 ; i<data.length(); i++){
			code = (int)data.charAt(i) - 44032;
			if(code > -1 && code < 11172){
				result += choseong[(int)Math.floor(code/588)];
			}
		}
		return result;
	}
	
}
