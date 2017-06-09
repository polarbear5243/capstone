package AppDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/*
 * RecipeDB.java
 * MySQL 데이터베이스에 연결된 상태에서 레시피 관련 정보를 가져오거나 업데이트 할 수
 * 있도록 함수를 제공해주는 DB 클래스이다.
 * */

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
		price,
		salty,
		sweet,
		seun,
		sin,
		hot,
		grosy,
		protein,
		mainflavor
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
		
		if (keyword == recipeKeyword.time)
			mResult = mStatement.executeQuery("SELECT idrecipe,recipename FROM recipe "
					+ "WHERE " + column + " = " + value + ";");
		else if (keyword == recipeKeyword.recipename)
			mResult = mStatement.executeQuery("SELECT idrecipe,recipename FROM recipe "
					+ "WHERE " + column + " like '%" + value + "%';");
		else
			mResult = mStatement.executeQuery("SELECT idrecipe,recipename FROM recipe "
					+ "WHERE " + column + " = '" + value + "';");
		
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
		
		boolean[] mainFlavor = getRecipeMainFlavorByID(recipeid);
		String FLAVOR[] = {"salty","sweet","seun","sin","hot","grosy","protein"};
		recipeKeyword [] keyword = {recipeKeyword.salty, recipeKeyword.sweet, recipeKeyword.seun, recipeKeyword.sin,
									recipeKeyword.hot, recipeKeyword.grosy, recipeKeyword.protein};
		
		int count=0;
		for(int i=0;i<7;i++){
			if(mainFlavor[i] == true){
				String target = FLAVOR[i];
				target = target + getRank(getRecipeFlavorAmountByID(recipeid,keyword[i]));				
				//토탈 추가
				mResult = mStatement.executeQuery("SELECT " + target + "_copy1" + " FROM userinfo "
						+ "WHERE " + "userid" + " = '" + userid + "';");
				String ss = target + "_copy1";
				if(mResult.next()){
					count = mResult.getInt(target + "_copy1");
				}
				count++;
				// 카운트 업데이트
				String qurery = "UPDATE userinfo set " + target + "_copy1" + " = " + count + " WHERE userid = '" + userid + "';";
				mStatement.executeUpdate(qurery);
				
				//좋아하면 추가
				if(Evaluate.compareTo("10")==0){
					mResult = mStatement.executeQuery("SELECT " + target + " FROM userinfo "
							+ "WHERE " + "userid" + " = '" + userid + "';");
					if(mResult.next()){
						count = mResult.getInt(target);
					}
					count++;
					// 카운트 업데이트
					qurery = "UPDATE userinfo set " + target + " = " + count + " WHERE userid = '" + userid + "';";
					mStatement.executeUpdate(qurery);
				}
			}
		}
	}
	
	public String getRank(int score){
		String rank = (score / 20 + 1) + "";
		if(rank.compareTo("6")==0) rank="5";
		return rank;
	}
	
	/* int getRecipeFlavorAmountByID(String id, recipeKeyword keyword)
	 * 레시피 ID와 얻고 싶은 맛 정보를 인풋으로 받아 해당 정보를 리턴한다. 결과가 없으면 -1을 리턴한다.
	 * 
	 * */
	public int getRecipeFlavorAmountByID(String id, recipeKeyword keyword) throws SQLException{
		String column = "";
		switch(keyword){
		case salty:
			column = "salty";
			break;
		case sweet:
			column = "sweet";
			break;
		case seun:
			column = "seun";
			break;
		case sin:
			column = "sin";
			break;
		case hot:
			column = "hot";
			break;
		case grosy:
			column = "grosy";
			break;
		case protein:
			column = "protein";
			break;
		}
		
		mResult = mStatement.executeQuery("SELECT "+column+" FROM recipe "
				+ "WHERE " + "idrecipe" + " = '" + id + "';");
		
		if (mResult.next()){
			return mResult.getInt(column);
		}
		return -1;
	}
	
	/* String getRecipeMainFlavorByID(String id)
	 * 레시피 ID를 인풋으로 받아 해당 레시피의 대표맛을 리턴한다.
	 * 대표맛은 스트링이며 다음과 같다.
	 *  ex) sweet/salty/grosy
	 *  ex) sweet
	 *  
	 *  대표맛은 DB에 숫자로 저장됨
	 *  1~7 : salty/sweet/seun/sin/hot/grosy/protein
	 *  EX) 1/2/7
	 *  
	 *  return 으로는 boolean배열이 리턴됨.
	 *  true면 그게 주된맛.
	 *  
	 *  1100001 --> 1/2/7 --> salty/sweet/protein
	 *  
	 * */
	public boolean[] getRecipeMainFlavorByID(String id) throws SQLException{
		String result = "";
		String[] splitResult;
		boolean [] mainFlavor = new boolean[7];
		
		for(int i=0;i<7;i++)
			mainFlavor[i]=false;
		
		mResult = mStatement.executeQuery("SELECT mainflavor FROM recipe "
				+ "WHERE " + "idrecipe" + " = '" + id + "';");
		if (mResult.next()){
			result = mResult.getString("mainflavor");
		}
		if (result.length() > 0){
			splitResult = result.split("/");
			for(int i=0;i<splitResult.length;i++){
				mainFlavor[Integer.parseInt(splitResult[i]) - 1] = true;
			}
		}
		return mainFlavor;
	}	
	
	/* int[] getRecentRecipeId(String userid, int num)
	 * 유저가 최근 좋다고 평가한 레시피 num 개를 가지고와 레시피 id 배열로 리턴한다.
	 * */
	public int[] getRecentRecipeId(String userid, int num) throws SQLException{
		int i;

		mResult = mStatement.executeQuery("SELECT * FROM recipeevaluation where userid = '" + userid + "' and Evaluate = 10	order by idrecipeEvaluation desc;");
		int [] result = new int[num];

		for(i=0;i<num;i++){
			if(mResult.next())
				result[i] = mResult.getInt("recipeid");
			else
				break;
		}
		
		if( i != num){
			int [] tmp = new int[i];
			
			for(int j=0;j<i;j++)
				tmp[j]=result[j];
			return tmp;
		}
		
		return result;
	}
	
	/* int getMaxOfRecipeid()
	 * 레시피 id MAX 를 가져온다.
	 */
	public int getMaxOfRecipeid() throws SQLException{
		mResult = mStatement.executeQuery("SELECT max(idrecipe) FROM recipe;");
		mResult.next();
		return mResult.getInt(1);
	}
	
	/* int getMinOfRecipeid()
	 * 레시피 id MAX 를 가져온다.
	 */
	public int getMinOfRecipeid() throws SQLException{
		mResult = mStatement.executeQuery("SELECT min(idrecipe) FROM recipe;");
		mResult.next();
		return mResult.getInt(1);
	}
	
	/* boolean isCorrectId(int id)
	 * 레시피 id 가 디비에 존재하는 지 확인하고 있으면 true 없으면 false 를 리턴한다.
	 * */
	public boolean isCorrectId(int id) throws SQLException{
		mResult = mStatement.executeQuery("SELECT * FROM recipe where idrecipe = " + id + ";");
		
		if(mResult.next())
			return true;
		else
			return false;
	}

}//end of RecipeDB
