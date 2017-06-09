package AppDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/*
 * IngredientDB.java
 * MySQL �����ͺ��̽��� ����� ���¿��� ����� ���� ������ �������ų� ������Ʈ �� ��
 * �ֵ��� �Լ��� �������ִ� DB Ŭ�����̴�.
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
	 * ���� ��� ����Ǿ� �ִ� ����� ����Ʈ�� �����մϴ�.
	 * Ȧ����°���� id �� ¦����°���� ����� �̸��� ���ϴ�.
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
	 * �ش� �������̵� ��ǲ���� �޾� �ش� ������ ����� ����Ʈ�� �����մϴ�.
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
	 * �ش� ������ ����� ����Ʈ�� ��ǲ���� ���� ����Ḧ �߰��մϴ�.
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
		
		//��ȣ�� �߰�.
		int count;
		mResult = mStatement.executeQuery("SELECT * FROM ingredientcount where userid = '" + userid + "' and ingredientid = '" + data.getIngredientID() + "';");
		if(mResult.next()){
			//�̹� ����
			count = mResult.getInt("count");
			count++;
			String qurery = "UPDATE ingredientcount set count = " + count + " WHERE userid = '" + userid + "' and ingredientid = '" + data.getIngredientID() + "';";
			mStatement.executeUpdate(qurery);
		}else{
			//���� ��������.
			manipulate = mConnection.prepareStatement(
					"INSERT INTO ingredientcount(userid,ingredientid,count) "
					+ "VALUES(?,?,?)");
			manipulate.setString(1,userid);
			manipulate.setString(2, data.getIngredientID());
			manipulate.setInt(3, 1);
			count = 1;
			
			manipulate.executeUpdate();
		}
		//�����ϴ� �� ����.
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
	 * ����ID�� ��ǰ �̸����� �˻��Ͽ� �Ῡ �ش� ������ ����Ḧ �����մϴ�.
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
	 * �ش� ������ id�� ��ǲ���� �޾� ������ ��� ����Ḧ �����մϴ�.
	 * 
	 * */
	public void deleteAllUserIngredient(String userid) throws SQLException{
		mStatement.executeUpdate("DELETE FROM myingredient WHERE userid = '" + userid + "';");		
	}
	
	/* void changeUserIngredientInfo (String userid, String productName, Ingredient data)
	 * ����id�� ��ǰ�̸�, ���� ��ǰ�� �����͸� �޾� �ش� ������ ��ǰ�� �����մϴ�.
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
	 * ��ǰ �̸��� ��ǲ���� �޾� �ش� �̸��� �������� ����Ʈ�� �����մϴ�.
	 * 
	 * */
	public ArrayList<String> searchByIngredientName(String productName) throws SQLException{
		ArrayList<String> result = new ArrayList<String>();
		String target = productName.toUpperCase();
		String choseong = getChoseong(productName);
		
		if (target != "" && choseong == ""){
			//�ʼ����� �˻��ϴ� ���
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
			//�Ϲ� �˻�
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
		String[] choseong = {"��","��","��","��","��","��","��","��","��",
				"��","��","��","��","��","��","��","��","��","��"};
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
