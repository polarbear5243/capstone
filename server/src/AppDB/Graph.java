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

class flavordata {
	int recipeid;
	int[] flavor;
	boolean[] mainflavor;
	ArrayList<Integer> ingredients = null;
	
	public flavordata(int recipeid, boolean[] mainflavor, ArrayList<Integer> ingredients, int[] flavor){
		this.recipeid = recipeid;
		this.mainflavor = mainflavor;
		this.ingredients = ingredients;
		this.flavor = flavor;
	}
	
	public int getRecipeId(){return recipeid;}
	public boolean[] getMainFlavor(){return mainflavor;}
	public ArrayList<Integer> getIngredinets(){return ingredients;}
	public int[] getFlavors(){return flavor;}
}

public class Graph {
	RecipeDB RecipeContents;
	ingredientDB IngredientContents;
	
	private static ArrayList<Node> nodeList = null;
	
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
				double ingredientsCount = 0.0;
				for (int t : mydata.ingredients){
					if(yourdata.ingredients.contains(t)) ingredientsCount++;
				}
				
				double ingredientsCategoryCount = 0.0;
				ArrayList<String> categoryCopy = new ArrayList<String>();
				for (String t : yourdata.categorys){
					categoryCopy.add(t);
				}
				for (int z = 0 ; z < mydata.categorys.size() ; z++){
					if (categoryCopy.contains(mydata.categorys.get(z))){
						ingredientsCategoryCount++;
						categoryCopy.remove(mydata.categorys.get(z));
					}
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
				double ingredientsCount = 0.0;
				for (int t : mydata.ingredients){
					if(yourdata.ingredients.contains(t)) ingredientsCount++;
				}
				
				double ingredientsCategoryCount = 0.0;
				ArrayList<String> categoryCopy = new ArrayList<String>();
				for (String t : yourdata.categorys){
					categoryCopy.add(t);
				}
				for (int z = 0 ; z < mydata.categorys.size() ; z++){
					if (categoryCopy.contains(mydata.categorys.get(z))){
						ingredientsCategoryCount++;
						categoryCopy.remove(mydata.categorys.get(z));
					}
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

	static public ArrayList<Node> getNodeList(){return nodeList;}
	
	public double[][] getUserRecomandRecipes(String userid, int[] priority, int[] recentRecipes) throws SQLException{
		if (nodeList == null)return null;
		if (recentRecipes == null)return null;
		
		 double[] mypriority = new double[4];
		 for (int i = 0 ; i < 4 ; i++){
			 if (priority[i] == 1){
				 mypriority[i] = 2.0;
			 }
			 else if (priority[i] == 2){
				 mypriority[i] = 1.67;
			 }
			 else if (priority[i] == 3){
				 mypriority[i] = 1.33;
			 }
			 else {
				 mypriority[i] = 1.0;
			 }
		 }
		//유저의 선호 식재료,맛 취향을 가지고 온다.
		IngredientContents.mResult = IngredientContents.mStatement.executeQuery(""
				+ "SELECT ingredientid1, ingredientid2, ingredientid3, ingredientid4, ingredientid5,"
				+ "salty1, salty1_copy1, salty2, salty2_copy1, salty3, salty3_copy1, salty4, salty4_copy1, salty5, salty5_copy1, "
				+ "sweet1, sweet1_copy1, sweet2, sweet2_copy1, sweet3, sweet3_copy1, sweet4, sweet4_copy1, sweet5, sweet5_copy1, "
				+ "seun1, seun1_copy1, seun2, seun2_copy1, seun3, seun3_copy1, seun4, seun4_copy1, seun5, seun5_copy1, "
				+ "sin1, sin1_copy1, sin2, sin2_copy1, sin3, sin3_copy1, sin4, sin4_copy1, sin5, sin5_copy1, "
				+ "hot1, hot1_copy1, hot2, hot2_copy1, hot3, hot3_copy1, hot4, hot4_copy1, hot5, hot5_copy1, "
				+ "grosy1, grosy1_copy1, grosy2, grosy2_copy1, grosy3, grosy3_copy1, grosy4, grosy4_copy1, grosy5, grosy5_copy1, "
				+ "protein1, protein1_copy1, protein2, protein2_copy1, protein3, protein3_copy1, protein4, protein4_copy1, protein5, protein5_copy1 "
				+ "FROM userinfo "
				+ "WHERE userid = '"+ userid + "';");
		IngredientContents.mResult.next();
		ArrayList<Integer> myIngredients = new ArrayList<Integer>();
		for (int i = 0 ; i < 5 ; i++){
			if (IngredientContents.mResult.getInt(i+1) != 0){
				myIngredients.add(IngredientContents.mResult.getInt(i+1));
			}
		}
		double [][] myFlavor = new double[7][5];
		for (int i = 0 ; i < 34 ; i++){
			try{
				myFlavor[i/5][i%5] = (double)IngredientContents.mResult.getInt(6+i)/(double)IngredientContents.mResult.getInt(6+i+1);
				
			}
			catch(ArithmeticException e){
				myFlavor[i/5][i%5] = 0;
			}
		}
		
		
		//전체 식재료ID와 대표맛, 7가지 맛을 가지고 온다.
		RecipeContents.mResult = RecipeContents.mStatement.executeQuery(""
				+ "SELECT recipeingredient.idrecipe, recipeingredient.idingredient, recipe.mainflavor, "
				+ "recipe.salty, recipe.sweet, recipe.seun, recipe.sin, recipe.hot, recipe.grosy, recipe.protein "
				+ "FROM recipeingredient, recipe "
				+ "WHERE recipeingredient.idrecipe = recipe.idrecipe AND recipeingredient.typecode = 3060001;");
		
		//레시피 식재료들과 맛, 대표맛 정보 저장
		ArrayList<flavordata> flavorDataList = new ArrayList<flavordata>();
		
		RecipeContents.mResult.next();
		int currentRecipeId = RecipeContents.mResult.getInt(1);
		ArrayList<Integer> ingredientList = new ArrayList<Integer>();
		String value = RecipeContents.mResult.getString(3);
		String[] splitResult;
		boolean[] mainFlavor = new boolean[7];
		if (value.length()>0){
			splitResult = value.split("/");
			for(int i=0;i<splitResult.length;i++){
				mainFlavor[Integer.parseInt(splitResult[i]) - 1] = true;
			}	
		}
		ingredientList.add(RecipeContents.mResult.getInt(2));
		int[] flavorData = new int[7];
		for (int i = 0 ; i < 7 ; i++ ){
			flavorData[i] = RecipeContents.mResult.getInt(4+i);
		}
		while(RecipeContents.mResult.next()){
			if (RecipeContents.mResult.getInt(1) == currentRecipeId){
				// 계속 데이터 넣어주면 됨
				ingredientList.add(RecipeContents.mResult.getInt(2));
			}
			else {
				flavorDataList.add(new flavordata(currentRecipeId,mainFlavor,ingredientList,flavorData));
				currentRecipeId = RecipeContents.mResult.getInt(1);
				ingredientList = new ArrayList<Integer>();
				value = RecipeContents.mResult.getString(3);
				mainFlavor = new boolean[7];
				if (value.length() > 0){
					splitResult = value.split("/");
					for(int i=0;i<splitResult.length;i++){
						mainFlavor[Integer.parseInt(splitResult[i]) - 1] = true;
					}	
				}
				ingredientList.add(RecipeContents.mResult.getInt(2));
				flavorData = new int[7];
				for (int i = 0 ; i < 7 ; i++ ){
					flavorData[i] = RecipeContents.mResult.getInt(4+i);
				}
			}
		}
		flavorDataList.add(new flavordata(currentRecipeId,mainFlavor,ingredientList,flavorData));
		
		double[][] result = new double[2][nodeList.size()];	//결과 배열 생성
		int resultIdx = 0;
		//메모리에 올린 정보들을 가지고 최종 스코어 계산
		for (int i = 0 ; i < recentRecipes.length; i++){
			for (int j = 0 ; j < this.getNodeList().size(); j++){
				if (this.getNodeList().get(j).getRecipeId() == recentRecipes[i]){
					//최근 좋은 평가를 내렸던 레시피들로 나머지 레시피들을 계산
					for(int k = 0 ; k < this.getNodeList().get(j).getEdgeList().size() ; k++){
						//레시피 노드 j 가 레시피 노드 k(targetRecipe) 에 대한 4가지 가중 스코어 계산
						int targetRecipe = this.getNodeList().get(j).getEdgeList().get(k).getToRecipe();
						double score = this.getNodeList().get(j).getEdgeList().get(k).getValue(0)*mypriority[0]
								+ this.getNodeList().get(j).getEdgeList().get(k).getValue(1)*mypriority[1]
								+ this.getNodeList().get(j).getEdgeList().get(k).getValue(2)*mypriority[2]
								+ this.getNodeList().get(j).getEdgeList().get(k).getValue(3)*mypriority[3];
						
						int flavorIdx = 0;
						for (int z = 0 ; z < flavorDataList.size() ; z++){
							if (flavorDataList.get(z).getRecipeId() == targetRecipe){
								flavorIdx = z;
								break;
							}
						}
						ArrayList<Integer> targetIngredients = flavorDataList.get(flavorIdx).ingredients;
						
						//식재료 포함 여부 확인하여 점수 +50
						boolean escape = false;
						for(int z = 0 ; z < myIngredients.size() ; z++){
							for (int x = 0 ; x < targetIngredients.size(); x++){
								if(myIngredients.get(z) == targetIngredients.get(x)){
									//내가 좋아하는 재료가 targetRecipe 에 하나라도 존재함
									score += 50;
									escape = true;
									break;
								}
							}
							if (escape == true) break;
						}
						
						//맛 취향 일치 여부 확인하여 점수 추가
						int count = 0;
						boolean[] targetMainFlavor = flavorDataList.get(flavorIdx).getMainFlavor();
						for (int z = 0 ; z < targetMainFlavor.length ; z++){
							if (targetMainFlavor[z]){
								int judgeValue = flavorDataList.get(flavorIdx).flavor[z];
								if(judgeValue < 20 && myFlavor[z][0] > 0.6){
									count++;
								}
								else if (judgeValue < 40 && myFlavor[z][1] > 0.6){
									count++;
								}
								else if (judgeValue < 60 && myFlavor[z][2] > 0.6){
									count++;
								}
								else if (judgeValue < 80 && myFlavor[z][3] > 0.6){
									count++;
								}
								else if (myFlavor[z][4] > 0.6){
									count++;
								}
							}
						}

						score += (50/targetMainFlavor.length)*count;
						escape = false;
						//확정된 스코어를 결과 배열의 MAX 값 확인 및 업데이트
						for (int z = 0 ; z < this.nodeList.size(); z++){
							//이미 결과 배열에 들어가 있는 경우
							if (result[0][z] == targetRecipe){
								if (result[1][z] < score){
									result[1][z] = score;	
								}
								escape = true;
								break;
							}
						}
						if (escape != true){
							//결과 배열에 들어가 있지 않은 경우
							result[0][resultIdx] = targetRecipe;
							result[1][resultIdx] = score;
							resultIdx++;
						}		
						
						
					}
					
				}
			}
		}
		
		//결과 배열 정렬
		sort(result,0,this.getNodeList().size()-1);
		
		return result;
	}
	
	public void sort(double[][] data, int l, int r){
        int left = l;
        int right = r;
        double pivot = data[1][(l+r)/2];
        
        do{
            while(data[1][left] > pivot) left++;
            while(data[1][right] < pivot) right--;
            if(left <= right){    
                double temp = data[1][left];
                data[1][left] = data[1][right];
                data[1][right] = temp;
                temp = data[0][left];
                data[0][left] = data[0][right];
                data[0][right] = temp;
                left++;
                right--;
            }
        }while (left <= right);
        
        if(l < right) sort(data, l, right);
        if(r > left) sort(data, left, r);
    }


}

