package AppDB;

import java.util.ArrayList;

/*
 * Recipe.java
 * 데이터 베이스에서 레시피 데이터 I/O 시 서버 내에서 담아두는 레시피 클래스이다.
 * 
 * */

public class Recipe {
	private String recipeID;
	private String recipeName;
	private String description = "";
	private String countryName = "";
	private String categoryName = "";
	private int time = 0;
	private String calories = "";
	private String amount = "";
	private String level = "";
	private String price = "";
	private String imageURL ="";
	private ArrayList<String> order = null;
	private ArrayList<Ingredient> ingredients = null;
	
	public Recipe(String recipeID, String recipeName){
		this.recipeID = recipeID;
		this.recipeName = recipeName;
	}
	
	public Recipe(String recipeID, String recipeName, String description, String countryName,String categoryName,
			int time, String calories, String amount, String level, String price, String imageURL){
		this.recipeID = recipeID;
		this.recipeName = recipeName;
		this.description = description;
		this.countryName = countryName;
		this.categoryName = categoryName;
		this.time = time;
		this.calories = calories;
		this.amount = amount;
		this.level = level;
		this.price = price;
		this.imageURL = imageURL;
	}
	
	public Recipe(String recipeID, String recipeName, String description, String countryName,String categoryName,
			int time, String calories, String amount, String level, String price, String imageURL, 
			ArrayList<String> ordercopy, ArrayList<Ingredient> ingredientscopy){
		this.recipeID = recipeID;
		this.recipeName = recipeName;
		this.description = description;
		this.countryName = countryName;
		this.categoryName = categoryName;
		this.time = time;
		this.calories = calories;
		this.amount = amount;
		this.level = level;
		this.price = price;
		this.imageURL = imageURL;
		this.order = new ArrayList<String>();
		for (String idx : ordercopy){
			this.order.add(idx);
		}
		this.ingredients = new ArrayList<Ingredient>();
		for (Ingredient idx : ingredientscopy){
			this.ingredients.add(idx);
		}
	}
	
	public void setRecipeID(String id){
		this.recipeID = id;
	}
	
	public String getRecipeID(){
		return this.recipeID;
	}
	
	public void setRecipeName(String name){
		this.recipeName = name;
	}
	
	public String getRecipeName(){
		return this.recipeName;
	}
	
	public void setDescription(String description){
		this.description = description;
	}
	
	public String getDescription(){
		return this.description;
	}
	
	public void setCountryName(String countryName){
		this.countryName = countryName;
	}
	
	public String getCountryName(){
		return this.countryName;
	}

	public void setCategoryName(String categoryName){
		this.categoryName = categoryName;
	}
	
	public String getCategoryName(){
		return this.categoryName;
	}

	public void setTime(int time){
		this.time = time;
	}
	
	public int getTime(){
		return this.time;
	}

	public String getCalories(){
		return this.calories;
	}

	public void setCalories(String calories){
		this.calories = calories;
	}
	
	public String getAmount(){
		return this.amount;
	}

	public void setAmount(String amount){
		this.amount = amount;
	}
	
	public String getLevel(){
		return this.level;
	}

	public void setLevel(String level){
		this.level = level;
	}
	
	public String getPrice(){
		return this.price;
	}

	public void setPrice(String price){
		this.price = price;
	}
	
	public String getImageURL(){
		return this.imageURL;
	}

	public void setImageURL(String ImageURL){
		this.imageURL = ImageURL;
	}
	
	public void setOrder(ArrayList<String> data){
		order = new ArrayList<String>();
		for (String idx : data){
			order.add(idx);
		}
	}
	
	public ArrayList<String> getOrder(){
		if (order != null)
			return order;
		else {
			return new ArrayList<String>();
		}
	}
	
	public void setIngredients(ArrayList<Ingredient> data){
		ingredients = new ArrayList<Ingredient>();
		for (Ingredient idx : data){
			ingredients.add(idx);
		}
	}
	
	public ArrayList<Ingredient> getIngredients(){
		if (ingredients != null)
			return ingredients;
		else {
			return new ArrayList<Ingredient>();
		}
	}
	
}//end of Recipe
