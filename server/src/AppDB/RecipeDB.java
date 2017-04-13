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
	 * 모든 DB 레시피의 ID와 이름의 리스트를 리턴하는데, 
	 * 리스트 홀수번째에는 ID가 짝수에는 이름이 있다.
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
	 * 만약에 DB에서 이미지를 바로 꺼내온다 하면 필요한 함수(미구현)
	 * 
	 * */
	public byte[] getImageByRecipeId(String id) throws SQLException{
		//mResult = mStatement.executeQuery("SELECT ingredientimage FROM ingredientimage WHERE ingredientid = " + id + ";");
		//mResult.next();
		//return mResult.getBytes("ingredientimage");
		return new byte[2];
	}
	
	/* ArrayList<String> getRecipeInfo(recipeKeyword keyword, String value)
	 * 키워드를 칼럼으로 하여 value 와 같은 값을 가지는 행들을 모두 뽑아 리스트로 리턴
	 * 키워드는 recipeKeyword 참조
	 * 리스트 홀수번째에는 ID가 짝수에는 이름이 있다.
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
	 * 레시피 ID 로 레시피를 찾아 리턴한다. 대부분의 정보가 포함되어 있으며 ID 에 해당하는 레시피가 없으면
	 * 결과없음 이름을 가진 -1 레시피를 리턴한다.
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
		
		 
		return new Recipe("-1","결과없음");
	}
	
	/* void insertUserEvaluation(String userid, String recipeid, String Evaluate)
	 * 어떤 유저가 어떤 레시피에 대해 평가한 것을 DB에 기록한다.
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
