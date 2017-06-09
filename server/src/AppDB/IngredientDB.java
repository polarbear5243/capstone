package AppDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/*
 * IngredientDB.java
 * MySQL 데이터베이스에 연결된 상태에서 식재료 관련 정보를 가져오거나 업데이트 할 수
 * 있도록 함수를 제공해주는 DB 클래스이다.
 * */

public class IngredientDB {
	protected Connection mConnection = null;
	protected Statement mStatement = null;
	protected PreparedStatement manipulate = null;
	protected ResultSet mResult = null;
	
	public IngredientDB(Connection connection) throws SQLException{
		mStatement = connection.createStatement();
		mConnection = connection;
	}
	
	/* ArrayList<String> getAllIngredientIDList()
	 * 현재 디비에 저장되어 있는 식재료 리스트를 리턴합니다.
	 * 홀수번째에는 id 가 짝수번째에는 식재료 이름이 들어갑니다.
	 * 
	 * */
	public ArrayList<String> getAllIngredientIDList() throws SQLException{
		ArrayList<String> result = new ArrayList<String>();
		mResult = mStatement.executeQuery("SELECT ingredientid, bornname From ingredient;");
		while(mResult.next()){
			result.add(Integer.toString(mResult.getInt("ingredientid")));
			result.add(mResult.getString("bornname"));
		}
		
		return result;
	}

	/* ArrayList<Ingredient> getUserIngredientList(String userid)
	 * 해당 유저아이디를 인풋으로 받아 해당 유저의 릭재료 리스트를 리턴합니다.
	 * 
	 * */
	public ArrayList<Ingredient> getUserIngredientList(String userid) throws SQLException{
		ArrayList<Ingredient> result = new ArrayList<Ingredient>();
		mResult = mStatement.executeQuery("SELECT * FROM myingredient WHERE userid = '" + userid + "';");
		while(mResult.next()){
			Ingredient data = new Ingredient(
					Integer.toString(mResult.getInt("ingredientid")),
					mResult.getString("amount"),
					mResult.getDate("buydate"),
					mResult.getString("productname"),
					mResult.getString("favorite")
					);
			result.add(data);
		}
		return result;
	}
	
	/* void insertUserIngredient(String userid, Ingredient data)
	 * 해당 유저의 식재료 리스트에 인풋으로 받은 식재료를 추가합니다.
	 * 
	 * */
	public void insertUserIngredient(String userid, Ingredient data) throws SQLException{
		manipulate = mConnection.prepareStatement(
				"INSERT INTO myingredient(userid,ingredientid,amount,buydate,productname,favorite) "
				+ "VALUES(?,?,?,?,?,?)");
		manipulate.setString(1,userid);
		manipulate.setInt(2, Integer.parseInt(data.getIngredientID()));
		manipulate.setString(3, data.getAmount() );
		manipulate.setDate(4, data.getBuyDate());
		manipulate.setString(5, data.getProductName());
		manipulate.setString(6, data.getFavorite());
		
		manipulate.executeUpdate();
		
		manipulate = mConnection.prepareStatement("INSERT INTO userlog(userid,ingredientid,type) "
				+ "VALUES(?,?,?)");
		manipulate.setString(1,userid);
		manipulate.setInt(2,Integer.parseInt(data.getIngredientID()));
		manipulate.setString(3,"A");
		
		manipulate.executeUpdate();
		
		//선호도 추가.
		int count;
		mResult = mStatement.executeQuery("SELECT * FROM ingredientcount where userid = '" + userid + "' and ingredientid = '" + data.getIngredientID() + "';");
		if(mResult.next()){
			//이미 존재
			count = mResult.getInt("count");
			count++;
			String qurery = "UPDATE ingredientcount set count = " + count + " WHERE userid = '" + userid + "' and ingredientid = '" + data.getIngredientID() + "';";
			mStatement.executeUpdate(qurery);
		}else{
			//새로 만들어야함.
			manipulate = mConnection.prepareStatement(
					"INSERT INTO ingredientcount(userid,ingredientid,count) "
					+ "VALUES(?,?,?)");
			manipulate.setString(1,userid);
			manipulate.setString(2, data.getIngredientID());
			manipulate.setInt(3, 1);
			count = 1;
			
			manipulate.executeUpdate();
		}
		//좋아하는 것 갱신.
		String qurery;
		int score;
		String tmpStr;
		int start=6;
		
		qurery = "SELECT * FROM userinfo WHERE userid = '" + userid + "';";
		mResult = mStatement.executeQuery(qurery);
		if(mResult.next()){
			for(int i=1;i<=5;i++){
				String target = "ingredientid" + i;
				tmpStr = mResult.getString(target);
				if(tmpStr == null)
					continue;
				if(tmpStr.compareTo(data.getIngredientID())==0){
					start=i;
					break;
				}
			}
		}
		for(int i=start-1;i>0;i--){
			qurery = "SELECT Count FROM ingredientcount where userid = '" + userid + "' and ingredientid = (Select ingredientid" + i + " from userinfo where userid = '" + userid + "');";
			mResult = mStatement.executeQuery(qurery);
			if(mResult.next())
				score = mResult.getInt(1);
			else
				score = 0;
			
			if(score < count){
				if(i != 5){
					qurery = "update userinfo set ingredientid" + (i+1) + " = ingredientid" + i + " where userid = '" + userid + "';";
					mStatement.executeUpdate(qurery);
				}
				if(i == 1){
					qurery = "update userinfo set ingredientid" + i + " = " + data.getIngredientID() + " where userid = '" + userid + "';";
					mStatement.executeUpdate(qurery);					
				}
			}
			else{
				if(i != 5){
					qurery = "update userinfo set ingredientid" + (i+1) + " = " + data.getIngredientID() + " where userid = '" + userid + "';";
					mStatement.executeUpdate(qurery);
				}
				break;
			}
		}
	} 
	
	/* void deleteUserIngredient(String userid, String productName)
	 * 유저ID와 제품 이름으로 검색하여 햐여 해당 유저의 식재료를 삭제합니다.
	 * 
	 * */
	public void deleteUserIngredient(String userid, String productName) throws SQLException{
		mStatement.executeUpdate("DELETE FROM myingredient WHERE userid = '" + userid + "' AND productname = '"+ productName +"';");
		
		mResult = mStatement.executeQuery("SELECT ingredientid FROM ingredient where bornname = '"+ productName +"';");
		mResult = mStatement.executeQuery("SELECT ingredientid FROM ingredient where ingredient.bornname = '"+ productName +"';");

		mResult.next();
		
		manipulate = mConnection.prepareStatement("INSERT INTO userlog(userid,ingredientid,type) "
				+ "VALUES(?,?,?)");
		manipulate.setString(1,userid);
		manipulate.setInt(2,mResult.getInt(1));
		manipulate.setString(3,"D");
		
		manipulate.executeUpdate();
		
	}	
	
	/* void deleteAllUserIngredient(String userid)
	 * 해당 유저의 id를 인풋으로 받아 유저의 모든 식재료를 삭제합니다.
	 * 
	 * */
	public void deleteAllUserIngredient(String userid) throws SQLException{
		mStatement.executeUpdate("DELETE FROM myingredient WHERE userid = '" + userid + "';");		
	}
	
	/* void changeUserIngredientInfo (String userid, String productName, Ingredient data)
	 * 유저id와 제품이름, 넣을 제품의 데이터를 받아 해당 유저의 제품을 변경합니다.
	 * 
	 * */
	public void changeUserIngredientInfo(String userid, String productName, Ingredient data) throws SQLException{
		mStatement.executeUpdate("UPDATE myingredient "
				+ "SET amount = '"+ data.getAmount() 
				+ "', buydate = '" + data.getBuyDate().toString()
				+ "', productName = '" + data.getProductName()
				+ "', favorite = '" + data.getFavorite()
				+ "' WHERE userid = '" + userid + "' AND productname = '" + productName + "';");
	}
	
	/* ArrayList<String> searchByIngredientName(String productName)
	 * 제품 이름을 인풋으로 받아 해당 이름의 식재료들의 리스트를 리턴합니다.
	 * 
	 * */
	public ArrayList<String> searchByIngredientName(String productName) throws SQLException{
		ArrayList<String> result = new ArrayList<String>();
		String target = productName.toUpperCase();
		String choseong = getChoseong(productName);
		
		if (target != "" && choseong == ""){
			//초성으로 검색하는 경우
			mResult = mStatement.executeQuery("SELECT ingredientid FROM realproduct "
					+ "WHERE choseongSearch(productname) LIKE '%" + productName + "%';");
			while(mResult.next()){
				result.add(Integer.toString(mResult.getInt(1)));
			}
			mResult = mStatement.executeQuery("SELECT ingredientid FROM ingredient "
					+ "WHERE choseongSearch(bornname) LIKE '%" + productName + "%';");	
			while(mResult.next()){
				result.add(Integer.toString(mResult.getInt(1)));
			}
		}
		else {
			//일반 검색
			mResult = mStatement.executeQuery("SELECT ingredientid FROM realproduct "
					+ "WHERE productname LIKE '%" + productName + "%';");
			while(mResult.next()){
				result.add(Integer.toString(mResult.getInt(1)));
			}
			mResult = mStatement.executeQuery("SELECT ingredientid FROM ingredient "
					+ "WHERE bornname LIKE '%" + productName + "%';");
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
	
}//end of IngredientDB.java
