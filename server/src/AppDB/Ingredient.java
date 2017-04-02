package AppDB;

import java.sql.Date;

public class Ingredient {
	private String ingredientID;
	private char amount;
	private Date buydate;
	private String productName;
	private char favorite;
	
	public Ingredient(){
		ingredientID = null;
		amount = 'L';
		java.util.Date utilDate = new java.util.Date();
		buydate = new Date(utilDate.getTime());
		productName = null;
		favorite = 'N';
	}
	
	public Ingredient(String ingredientID, char amount, Date buydate, String productName,char favorite){
		this.ingredientID = ingredientID;
		this.amount = amount;
		this.buydate = buydate;
		this.productName = productName;
		this.favorite = favorite;
	}
	
	public void setIngredientID(String id){
		ingredientID = id;
	}
	
	public String getIngredientID(){
		return ingredientID;
	}
	
	public void setAmount(char amount){
		this.amount = amount;
	}
	
	public char getAmount(){
		return this.amount;
	}
	
	public void setBuyDate(Date buydate){
		this.buydate = buydate;
	}
	
	public Date getBuyDate(){
		return this.buydate;
	}
	
	public void setProductName(String productName){
		this.productName = productName;
	}
	
	public String getProductName(){
		return this.productName;
	}

	public void setFavorite(char favorite){
		this.favorite = favorite;
	}
	
	public char getFavorite(){
		return this.favorite;
	}

	
}
