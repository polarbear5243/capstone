package AppDB;

import java.util.ArrayList;
import java.sql.SQLException;


class data {
	int recipeid;
	ArrayList<String> categorys;
	ArrayList<Integer> ingredients;
	public data(int recipeid, ArrayList<Integer> ingredients ,ArrayList<String> categorys){
		this.recipeid = recipeid;
		this.ingredients = ingredients;
		this.categorys = categorys;
	}
	
	public int getRecipeId(){return recipeid;}
	public ArrayList<String> getCategorys(){return categorys;}
	public ArrayList<Integer> getIngredients(){return ingredients;}
	
}

public class Graph {
	RecipeDB RecipeContents;
	ingredientDB IngredientContents;
	
	ArrayList<Node> nodeList;
	
	public Graph(RecipeDB recipe_DB,ingredientDB ingredient_DB) throws SQLException{
		RecipeContents = recipe_DB;
		IngredientContents = ingredient_DB;
		nodeList = new ArrayList<Node>();
		ArrayList<data> recipeList = new ArrayList<data>();
		
		recipe_DB.mResult = recipe_DB.mStatement.executeQuery(
				"SELECT idrecipe, countryName, categoryName, salty, sweet, seun, sin, hot, grosy, protein"
				+ " FROM recipe");
		
		recipe_DB.mResult.last();
		int rowNum = recipe_DB.mResult.getRow();
		
		//////////////////////// 레시피 자료구조 하나 만들고 거기에 식재료 넣는 형식....
		ingredient_DB.mResult = ingredient_DB.mStatement.executeQuery(
				"SELECT recipeingredient.idrecipe, recipeingredient.idingredient, ingredient.categorycode "
				+ "FROM recipeingredient, ingredient "
				+ "WHERE recipeingredient.idingredient = ingredient.ingredientid  "
				+ "AND (ingredient.categorycode = 'A1' OR ingredient.categorycode = 'B1' OR ingredient.categorycode = 'B2') ORDER BY recipeingredient.idrecipe;"); 
		//////////////////////////
		
		ingredient_DB.mResult.next();
		int recipeid = ingredient_DB.mResult.getInt(1);
		ArrayList<Integer> ingredients = new ArrayList<Integer>();
		ArrayList<String> categorys = new ArrayList<String>();
		ingredients.add(ingredient_DB.mResult.getInt(2));
		categorys.add(ingredient_DB.mResult.getString(3));
		
		while(ingredient_DB.mResult.next()){
			if (recipeid == ingredient_DB.mResult.getInt(1)){
				//기존 리스트에 계속 추가하면 됨
				ingredients.add(ingredient_DB.mResult.getInt(2));
				categorys.add(ingredient_DB.mResult.getString(3));
			}
			else {
				//새로운 레시피에 대한 데이터를 만들어야 함
				recipeList.add(new data(recipeid, ingredients, categorys));
				ingredients = new ArrayList<Integer>();
				categorys = new ArrayList<String>();
				recipeid = ingredient_DB.mResult.getInt(1);
				ingredients.add(ingredient_DB.mResult.getInt(2));
				categorys.add(ingredient_DB.mResult.getString(3));
			}
			
		}
		recipeList.add(new data(recipeid, ingredients, categorys));
		
		for (int j = 1 ; j <= rowNum ; j++){
			recipe_DB.mResult.absolute(j);
			// 노드 본인의 특성을 받아옴.
			/*
			ingredient_DB.mResult = ingredient_DB.mStatement.executeQuery(
					"SELECT recipeingredient.idingredient, ingredient.categorycode"
					+ " FROM recipeingredient, ingredient"
					+ " WHERE recipeingredient.idrecipe = "+ recipe_DB.mResult.getInt(1) 
					+ " AND (ingredient.categorycode = 'A1' OR ingredient.categorycode = 'B1' OR ingredient.categorycode = 'B2');"); 
			*/
			int myRecipeId = recipe_DB.mResult.getInt(1);
			String myCountryName = recipe_DB.mResult.getString(2);
			String myCategoryName = recipe_DB.mResult.getString(3);
			int mySalty = recipe_DB.mResult.getInt(4);
			int mySweet = recipe_DB.mResult.getInt(5);
			int mySeun = recipe_DB.mResult.getInt(6);
			int mySin = recipe_DB.mResult.getInt(7);
			int myHot = recipe_DB.mResult.getInt(8);
			int myGrosy = recipe_DB.mResult.getInt(9);
			int myProtein = recipe_DB.mResult.getInt(10);
			
			//System.out.println("현재 작업 중인 레시피 노드: "+ myRecipeId);
			
			data mydata = null;
			for (int p = 0 ; p < recipeList.size() ; p++){
				if (recipeList.get(p).recipeid == myRecipeId){
					mydata = recipeList.get(p);
					break;
				}
			}
			/*
			ArrayList<String> myIngredients = new ArrayList<String>();
			while(ingredient_DB.mResult.next()){
				myIngredients.add(ingredient_DB.mResult.getString(1));
			}
			ArrayList<String> myCategorys = new ArrayList<String>();
			ingredient_DB.mResult.beforeFirst();
			while(ingredient_DB.mResult.next()){
				myCategorys.add(ingredient_DB.mResult.getString(2));
			}
			*/
			ArrayList<Edge> myEdges = new ArrayList<Edge>();
			for (int k = 1 ; k < j ; k++ ){
				recipe_DB.mResult.absolute(k);
				/*
				ingredient_DB.mResult = ingredient_DB.mStatement.executeQuery(
						"SELECT recipeingredient.idingredient, ingredient.categorycode"
						+ " FROM recipeingredient, ingredient"
						+ " WHERE recipeingredient.idrecipe = "+ recipe_DB.mResult.getInt(1) 
						+ " AND (ingredient.categorycode = 'A1' OR ingredient.categorycode = 'B1' OR ingredient.categorycode = 'B2');"); 
				*/
				int yourRecipeId = recipe_DB.mResult.getInt(1);
				String yourCountryName = recipe_DB.mResult.getString(2);
				String yourCategoryName = recipe_DB.mResult.getString(3);
				int yourSalty = recipe_DB.mResult.getInt(4);
				int yourSweet = recipe_DB.mResult.getInt(5);
				int yourSeun = recipe_DB.mResult.getInt(6);
				int yourSin = recipe_DB.mResult.getInt(7);
				int yourHot = recipe_DB.mResult.getInt(8);
				int yourGrosy = recipe_DB.mResult.getInt(9);
				int yourProtein = recipe_DB.mResult.getInt(10);
				
				data yourdata = null;
				for (int p = 0 ; p < recipeList.size() ; p++){
					if (recipeList.get(p).recipeid == yourRecipeId){
						yourdata = recipeList.get(p);
						break;
					}
				}
				/*
				ArrayList<String> yourIngredients = new ArrayList<String>();;
				while(ingredient_DB.mResult.next()){
					yourIngredients.add(ingredient_DB.mResult.getString(1));
				}
				ArrayList<String> yourCategorys = new ArrayList<String>();;
				ingredient_DB.mResult.beforeFirst();
				while(ingredient_DB.mResult.next()){
					yourCategorys.add(ingredient_DB.mResult.getString(2));
				}
				*/
				//System.out.println("엣지 생성 작업 정상 완료 :" + k + "/" + rowNum);
				Edge edge = new Edge(myRecipeId,yourRecipeId); //엣지 생성
				
				///////////////////////////////// 코스트 계산 /////////////////////////////////////
				// 맛 계산
				double result = (100/7)*(1/Math.pow(Math.E,Math.abs((mySalty-yourSalty)/10))
										+ 1/Math.pow(Math.E,Math.abs((mySweet-yourSweet)/10))
										+ 1/Math.pow(Math.E,Math.abs((mySeun-yourSeun)/10))
										+ 1/Math.pow(Math.E,Math.abs((mySin-yourSin)/10))
										+ 1/Math.pow(Math.E,Math.abs((myHot-yourHot)/10))
										+ 1/Math.pow(Math.E,Math.abs((myGrosy-yourGrosy)/10))
										+ 1/Math.pow(Math.E,Math.abs((myProtein-yourProtein)/10)));
				edge.setValue(0, result);
				
				// 식재료 계산
				int ingredientsCount = 0;
				for (int t : mydata.ingredients){
					if(yourdata.ingredients.contains(t)) ingredientsCount++;
				}
				
				int ingredientsCategoryCount = 0;
				for (String t : mydata.categorys){
					if(yourdata.categorys.contains(t)) ingredientsCategoryCount++;
				}
				
				result = ((ingredientsCount/(2*mydata.ingredients.size()))+(ingredientsCategoryCount/(2*mydata.categorys.size())))*100;
				edge.setValue(1, result);
				
				// 카테고리 계산
				if (myCategoryName.equals(yourCategoryName)){
					edge.setValue(2, 80);
				}
				else {
					edge.setValue(2, 20);
				}
				
				// 나라 계산
				if (myCountryName.equals(yourCountryName)){
					edge.setValue(3, 80);
				}
				else {
					edge.setValue(3, 20);
				}
				
				myEdges.add(edge);	//계산된 값을 엣지 리스트에 추가
			}
			
			for (int k = j+1 ; k <= rowNum ; k++ ){
				recipe_DB.mResult.absolute(k);
				/*
				ingredient_DB.mResult = ingredient_DB.mStatement.executeQuery(
						"SELECT recipeingredient.idingredient, ingredient.categorycode"
						+ " FROM recipeingredient, ingredient"
						+ " WHERE recipeingredient.idrecipe = "+ recipe_DB.mResult.getInt(1) 
						+ " AND (ingredient.categorycode = 'A1' OR ingredient.categorycode = 'B1' OR ingredient.categorycode = 'B2');"); 
				*/
				int yourRecipeId = recipe_DB.mResult.getInt(1);
				String yourCountryName = recipe_DB.mResult.getString(2);
				String yourCategoryName = recipe_DB.mResult.getString(3);
				int yourSalty = recipe_DB.mResult.getInt(4);
				int yourSweet = recipe_DB.mResult.getInt(5);
				int yourSeun = recipe_DB.mResult.getInt(6);
				int yourSin = recipe_DB.mResult.getInt(7);
				int yourHot = recipe_DB.mResult.getInt(8);
				int yourGrosy = recipe_DB.mResult.getInt(9);
				int yourProtein = recipe_DB.mResult.getInt(10);
				
				
				data yourdata = null;
				for (int p = 0 ; p < recipeList.size() ; p++){
					if (recipeList.get(p).recipeid == yourRecipeId){
						yourdata = recipeList.get(p);
						break;
					}
				}
				/*
				ArrayList<String> yourIngredients = new ArrayList<String>();;
				while(ingredient_DB.mResult.next()){
					yourIngredients.add(ingredient_DB.mResult.getString(1));
				}
				ArrayList<String> yourCategorys = new ArrayList<String>();;
				ingredient_DB.mResult.beforeFirst();
				while(ingredient_DB.mResult.next()){
					yourCategorys.add(ingredient_DB.mResult.getString(2));
				}
				*/
				Edge edge = new Edge(myRecipeId,yourRecipeId); //엣지 생성	
				//System.out.println("엣지 생성 작업 정상 완료 :" + k + "/" + rowNum);
				
				
				///////////////////////////////// 코스트 계산 /////////////////////////////////////
				// 맛 계산
				double result = (100/7)*(1/Math.pow(Math.E,Math.abs((mySalty-yourSalty)/10))
										+ 1/Math.pow(Math.E,Math.abs((mySweet-yourSweet)/10))
										+ 1/Math.pow(Math.E,Math.abs((mySeun-yourSeun)/10))
										+ 1/Math.pow(Math.E,Math.abs((mySin-yourSin)/10))
										+ 1/Math.pow(Math.E,Math.abs((myHot-yourHot)/10))
										+ 1/Math.pow(Math.E,Math.abs((myGrosy-yourGrosy)/10))
										+ 1/Math.pow(Math.E,Math.abs((myProtein-yourProtein)/10)));
				edge.setValue(0, result);
				
				// 식재료 계산
				int ingredientsCount = 0;
				for (int t : mydata.ingredients){
					if(yourdata.ingredients.contains(t)) ingredientsCount++;
				}
				
				int ingredientsCategoryCount = 0;
				for (String t : mydata.categorys){
					if(yourdata.categorys.contains(t)) ingredientsCategoryCount++;
				}
				
				result = ((ingredientsCount/(2*mydata.ingredients.size()))+(ingredientsCategoryCount/(2*mydata.categorys.size())))*100;
				edge.setValue(1, result);
				
				// 카테고리 계산
				if (myCategoryName.equals(yourCategoryName)){
					edge.setValue(2, 80);
				}
				else {
					edge.setValue(2, 20);
				}
				
				// 나라 계산
				if (myCountryName.equals(yourCountryName)){
					edge.setValue(3, 80);
				}
				else {
					edge.setValue(3, 20);
				}
				
				myEdges.add(edge);	//계산된 값을 엣지 리스트에 추가
			}
			
			
			nodeList.add(new Node(myRecipeId,myEdges));	//그래프에 노드 추가
		
		} 
		
		
		
		
	} 
}

