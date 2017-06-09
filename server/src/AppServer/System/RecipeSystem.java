package AppServer.System;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

import com.mysql.fabric.Server;
import com.mysql.jdbc.SequentialBalanceStrategy;

import AppDB.Graph;
import AppDB.GroceryDB;
import AppDB.Ingredient;
import AppDB.LoginDB;
import AppDB.Recipe;
import AppDB.RecipeDB;
import AppServer.MessageParser;
import AppServer.Netwok.SendString;

/*
 * RecipeSystem.java
 * AppSystem를 상속받아 클라이언트에 레시피 관련 서비스를 제공하는 클래스이다.
 * 
 * */

public class RecipeSystem extends AppSystem {

	protected static final String GETALL_RECIPE = "GetAll";
	protected static final String SEARCH_RECIPE = "Search";
	protected static final String GET_RECIPE = "GetInfo";
	protected static final String EVALUATE_RECIPE = "Evaluate";
	protected static final String RECOMMEND_RECIPE = "Recommend";
	protected static final String GET_RAND_RECIPE = "GetRand";
	
	protected String[] mMsg;
	protected DataInputStream mDIS;
	protected DataOutputStream mDOS;
	
	protected RecipeDB mRecipeDB;
	
	/* void getAll()
	 * MSG FORMAT
	 * "Recipe"///"GetAll"
	 * 
	 * Result FORMAT
	 * "Start"///recipeId///recipeName///"End"
	 * 
	 * */
	protected void getAll() throws SQLException, IOException{
		ArrayList<String> result = new ArrayList<String>();
		ArrayList<String> tmp = new ArrayList<String>();
		String sendStr;
		
		tmp = mRecipeDB.getAllRecipeIdNameList();
		
		int tmpSize = tmp.size()/2;
		int index[] = new int[tmpSize];
		
		for(int i=0;i<tmpSize;i++)
			index[i]=i;
		
		int index1, index2;
		int tmpInt;
		for(int i=0;i<300;i++){
			index1 = (int)(Math.random()*tmpSize);
			index2 = (int)(Math.random()*tmpSize);
			
			tmpInt = index[index1];
			index[index1] = index[index2];
			index[index2] = tmpInt;
		}
		
		result.add("Start");
		for(int i=0;i<40;i++) {
			result.add(tmp.get(index[i]*2));
			result.add(tmp.get(index[i]*2+1));
		}
		result.add("End");
		
		sendStr = MessageParser.wrapMsg(result);
		SendString.sendString(sendStr, mDOS);
		
		System.out.println("레시피 전달");
	}
	
	/* void search()
	 * MSG FORMAT
	 * "Recipe"///"Search"///keyword(String)
	 * 
	 * Result - FORMAT
	 * "Start"///recipeId///recipeName///"End"
	 * 
	 * */	
	protected void search() throws SQLException, IOException{
		ArrayList<String> result = new ArrayList<String>();
		ArrayList<String> tmp;
		String sendStr;

		result.add("Start");
		
		tmp = mRecipeDB.getRecipesInfoByKeyword(RecipeDB.recipeKeyword.id, mMsg[2]);
		if(tmp.size() != 0)
			result.addAll(tmp);

		tmp = mRecipeDB.getRecipesInfoByKeyword(RecipeDB.recipeKeyword.recipename, mMsg[2]);
		if(tmp.size() != 0)
			result.addAll(tmp);
		
		tmp = mRecipeDB.getRecipesInfoByKeyword(RecipeDB.recipeKeyword.countryname, mMsg[2]);
		if(tmp.size() != 0)
			result.addAll(tmp);

		tmp = mRecipeDB.getRecipesInfoByKeyword(RecipeDB.recipeKeyword.categoryname, mMsg[2]);
		if(tmp.size() != 0)
			result.addAll(tmp);
		
		result.add("End");
		
		sendStr = MessageParser.wrapMsg(result);
		SendString.sendString(sendStr, mDOS);
	}
	
	/* void getRecipe()
	 * MSG FORMAT
	 * "Recipe"///"GetInfo"///ID(String)
	 * 
	 * Result FORMAT
	 * id///name///countryName///categoryName///time///calories///level///ingredients///...///"End"///order///...///"End"
	 * 
	 * */	
	protected void getRecipe() throws SQLException, IOException{	
		ArrayList<String> result = new ArrayList<String>();

		String sendStr;
		
		Recipe recipe = mRecipeDB.getRecipeInfoByID(mMsg[2]);
		result.add(recipe.getRecipeID());
		result.add(recipe.getRecipeName());
		result.add(recipe.getCountryName());
		result.add(recipe.getCategoryName());
		result.add(Integer.toString(recipe.getTime()));
		result.add(recipe.getCalories());
		result.add(recipe.getLevel());
		
		ArrayList<Ingredient> ingredients = recipe.getIngredients();
		for(int i=0;i<ingredients.size();i++)
			result.add(ingredients.get(i).getProductName());
		result.add("End");
		
		ArrayList<String> order = recipe.getOrder();
		for(int i=0;i<order.size();i++)
			result.add(order.get(i));
		result.add("End");

		sendStr = MessageParser.wrapMsg(result);
		SendString.sendString(sendStr, mDOS);
		
		System.out.println("레시피 전달");
	}
	
	/* void evaluate()
	 * MSG FORMAT
	 * "Recipe"///"Evaluate"///userId(String)///recipeId(String)///Score(String)
	 * 
	 * Result FORMAT
	 * 성공
	 * "Success"
	 * 
	 * */	
	protected void evaluate() throws SQLException, IOException{	
		ArrayList<String> result = new ArrayList<String>();
		String sendStr;
		
		mRecipeDB.insertUserEvaluation(mMsg[2], mMsg[3], mMsg[4]);

		result.add("Success");
		
		sendStr = MessageParser.wrapMsg(result);
		SendString.sendString(sendStr, mDOS);
		
		System.out.println("평가 완료");
	}
	
	/* void recommend()
	 * MSG FORMAT
	 * "Recipe"///"Recommend"///id(string)///1(rank-맛)///2(rank-재료)///3(rank-카테고리)///4(rank-나라)
	 * 
	 * Result FORMAT
	 * 성공
	 * "Start"///recipeId///recipeName///"End"
	 * 
	 * */	
	protected void recommend() throws SQLException, IOException{	
		
		int recipeNum = 10;
		int getNum = 10;
		
		ArrayList<String> result = new ArrayList<String>();
		String sendStr;
		
		int [] like = new int[4];
		String userid = mMsg[2];
		int [] recentRecipeId;
		
		for(int i=0;i<4;i++)
			like[i] = Integer.parseInt(mMsg[3+i]);
		recentRecipeId=mRecipeDB.getRecentRecipeId(userid, getNum);
		
		if(recentRecipeId.length == 0){
			result.add("Start");
			for(int i=0;i<recipeNum;i++){
				
				int min = mRecipeDB.getMinOfRecipeid();
				int max = mRecipeDB.getMaxOfRecipeid();
				
				int num = -1;
				Random random = new Random();
				random.setSeed(System.nanoTime());
				
				while(mRecipeDB.isCorrectId(num) == false){
					num = (int)(random.nextDouble() * max) + min;
				}			
				
				int id = num;
				result.add(id+"");
				Recipe info= mRecipeDB.getRecipeInfoByID(id+"");
				result.add(info.getRecipeName());
			}
			result.add("End");
			
			sendStr = MessageParser.wrapMsg(result);
			SendString.sendString(sendStr, mDOS);
			
			return;
		}
		
		Graph graph = myAppServer.Server.getGraph();
		
		double [][] recipeScore = graph.getUserRecomandRecipes(userid, like, recentRecipeId);
		
		//레시피 선택
		int [] selectedRecipedId = new int[recipeNum];
		double [] selectedScore = new double[recipeNum];
		
		int index=0;
		double bestscore = recipeScore[1][0];
		int probability;
	
		Random random = new Random();
		random.setSeed(System.nanoTime());
		int randInt;
		
		for(int i=0;i<recipeNum;i++){
			while(true){
				probability = (int)(recipeScore[1][index] / bestscore * 100) - (i * 2);
				
				randInt = (int)(random.nextDouble() * 100);
				
				if(randInt <= probability){
					selectedRecipedId[i] = (int)recipeScore[0][index];
					selectedScore[i] = recipeScore[1][index];
					index = (index + 1) % recipeScore[0].length;
					break;
				}
				index = (index + 1) % recipeScore[0].length;
			}
		}
		
		//데이터 보내기
		result.add("Start");
		for(int i=0;i<recipeNum;i++){
			int id = selectedRecipedId[i];
			result.add(id+"");
			Recipe info= mRecipeDB.getRecipeInfoByID(id+" ");
			result.add(info.getRecipeName()+"  "+selectedScore[i]);
		}

		result.add("End");

		sendStr = MessageParser.wrapMsg(result);
		SendString.sendString(sendStr, mDOS);
		
		System.out.println("평가 완료");
	}
	
	/* void getRandRecipe()
	 * MSG FORMAT
	 * "Recipe"///"GetRand"
	 * 
	 * Result FORMAT
	 * 성공
	 * recipeId
	 * 
	 * */	
	protected void getRandRecipe() throws SQLException, IOException{	
		ArrayList<String> result = new ArrayList<String>();
		String sendStr;
		
		int min = mRecipeDB.getMinOfRecipeid();
		int max = mRecipeDB.getMaxOfRecipeid();
		
		int num = -1;
		Random random = new Random();
		random.setSeed(System.nanoTime());
		
		while(mRecipeDB.isCorrectId(num) == false){
			num = (int)(random.nextDouble() * max) + min;
		}
		
		result.add(num + "");
		
		sendStr = MessageParser.wrapMsg(result);
		SendString.sendString(sendStr, mDOS);
		
		System.out.println("평가 완료");
	}
	
	public RecipeSystem(String[] msg, DataInputStream dis, DataOutputStream dos) throws SQLException{
		mMsg = msg;
		mDIS = dis;
		mDOS = dos;
		mRecipeDB = GroceryDB.getRecipeDB();
	}

	public void excuteSystem() throws SQLException, IOException {
		if(mMsg[1].compareTo(RecipeSystem.GETALL_RECIPE)==0)
			getAll();
		else if(mMsg[1].compareTo(RecipeSystem.SEARCH_RECIPE)==0)
			search();
		else if(mMsg[1].compareTo(RecipeSystem.GET_RECIPE)==0)
			getRecipe();
		else if(mMsg[1].compareTo(RecipeSystem.EVALUATE_RECIPE)==0)
			evaluate();
		else if(mMsg[1].compareTo(RecipeSystem.RECOMMEND_RECIPE)==0)
			recommend();
		else if(mMsg[1].compareTo(RecipeSystem.GET_RAND_RECIPE)==0)
			getRandRecipe();
		else 
			;
	}

}//end of RecipeSystem
