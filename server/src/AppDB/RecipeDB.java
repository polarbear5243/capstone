package AppDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;



public class RecipeDB {
	protected Connection mConnection = null;
	protected Statement mStatement = null;
	protected PreparedStatement manipulate = null;
	protected ResultSet mResult = null;
	
	public static enum recipeKeyword{
		id,
		recipename,
		countryname,
		categoryname,
		time,
		calories,
		amount,
		level,
		price
	}
	
	public RecipeDB(Connection connection) throws SQLException{
		mStatement = connection.createStatement();
		mConnection = connection;
	}
	
	/* ArrayList<String> getAllRecipeIdNameList()
	 * ��� DB �������� ID�� �̸��� ����Ʈ�� �����ϴµ�, 
	 * ����Ʈ Ȧ����°���� ID�� ¦������ �̸��� �ִ�.
	 * 
	 * */
	public ArrayList<String> getAllRecipeIdNameList() throws SQLException{
		
		ArrayList<String> result = new ArrayList<String>();
		mResult = mStatement.executeQuery("SELECT idrecipe, recipename From recipe;");
		while(mResult.next()){
			result.add(Integer.toString(mResult.getInt("idrecipe")));
			result.add(mResult.getString("recipename"));
		}
		
		return result;
	}
	
	/* byte[] getImageByRecipeId(String id)
	 * ���࿡ DB���� �̹����� �ٷ� �����´� �ϸ� �ʿ��� �Լ�(�̱���)
	 * 
	 * */
	public byte[] getImageByRecipeId(String id) throws SQLException{
		//mResult = mStatement.executeQuery("SELECT ingredientimage FROM ingredientimage WHERE ingredientid = " + id + ";");
		//mResult.next();
		//return mResult.getBytes("ingredientimage");
		return new byte[2];
	}
	
	/* ArrayList<String> getRecipeInfo(recipeKeyword keyword, String value)
	 * Ű���带 Į������ �Ͽ� value �� ���� ���� ������ ����� ��� �̾� ����Ʈ�� ����
	 * Ű����� recipeKeyword ����
	 * ����Ʈ Ȧ����°���� ID�� ¦������ �̸��� �ִ�.
	 * */
	public ArrayList<String> getRecipesInfoByKeyword(recipeKeyword keyword, String value) throws SQLException{
		ArrayList<String> result = new ArrayList<String>();
		String column ="";
		switch(keyword){
		case id:
			column = "idrecipe";
			break;
		case recipename:
			column = "recipename";
			break;
		case countryname:
			column = "countryname";
			break;
		case categoryname:
			column = "categoryname";
			break;
		case time:
			column = "time";
			break;
		case calories:
			column = "calories";
			break;
		case amount:
			column = "amount";
			break;
		case level:
			column = "level";
			break;
		case price:
			column = "price";
			break;
		}
		
		if (keyword != recipeKeyword.time)
		mResult = mStatement.executeQuery("SELECT idrecipe,recipename FROM recipe "
				+ "WHERE " + column + " = '" + value + "';");
		else
		mResult = mStatement.executeQuery("SELECT idrecipe,recipename FROM recipe "
				+ "WHERE " + column + " = " + value + ";");
		
		while(mResult.next()){
			
			result.add(Integer.toString(mResult.getInt("idrecipe")));
			result.add(mResult.getString("recipename"));
		}
		return result;
	}
	
	/* Recipe getRecipeInfoByID(String id)
	 * ������ ID �� �����Ǹ� ã�� �����Ѵ�. ��κ��� ������ ���ԵǾ� ������ ID �� �ش��ϴ� �����ǰ� ������
	 * ������� �̸��� ���� -1 �����Ǹ� �����Ѵ�.
	 * 
	 * */
	public Recipe getRecipeInfoByID(String id) throws SQLException{
		mResult = mStatement.executeQuery("SELECT * FROM recipe "
				+ "WHERE " + "idrecipe" + " = '" + id + "';");
		
		if (mResult.next()){
			Recipe data = new Recipe(
					Integer.toString(mResult.getInt("idrecipe")),
					mResult.getString("recipename"),
					mResult.getString("description"),
					mResult.getString("countryname"),
					mResult.getString("categoryname"),
					mResult.getInt("time"),
					mResult.getString("calories"),
					mResult.getString("amount"),
					mResult.getString("level"),
					mResult.getString("price"),
					mResult.getString("imageURL")
					);
			mResult = mStatement.executeQuery("SELECT * FROM recipeingredient "
					+ "WHERE " + "idrecipe" + " = '" + id + "';");
			
			ArrayList<Ingredient> temp = new ArrayList<Ingredient>();
			while(mResult.next()){
				temp.add(new Ingredient(Integer.toString(mResult.getInt("idingredient")),
						mResult.getString("ingredientName"), mResult.getString("amount"), 
						mResult.getString("typename")));
			}
			
			mResult = mStatement.executeQuery("SELECT stepdescription FROM recipestep "
					+ "WHERE " + "idrecipe" + " = '" + id + "'"
							+ "ORDER BY stepnumber;");
			
			ArrayList<String> temp2 = new ArrayList<String>();
			while(mResult.next()){
				temp2.add(mResult.getString("stepdescription"));
			}
			
			data.setIngredients(temp);
			data.setOrder(temp2);
			
			return data;
		}
		
		 
		return new Recipe("-1","�������");
	}
	
	/* void insertUserEvaluation(String userid, String recipeid, String Evaluate)
	 * � ������ � �����ǿ� ���� ���� ���� DB�� ����Ѵ�.
	 * 
	 * */
	public void insertUserEvaluation(String userid, String recipeid, String Evaluate) throws SQLException{
		manipulate = mConnection.prepareStatement(
				"INSERT INTO recipeevaluation(userid,recipeid,Evaluate) "
				+ "VALUES(?,?,?)");
		manipulate.setString(1,userid);
		manipulate.setInt(2, Integer.parseInt(recipeid));
		manipulate.setString(3,Evaluate);
		
		manipulate.executeUpdate();
	}
	
}