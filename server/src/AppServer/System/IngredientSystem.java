package AppServer.System;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import AppDB.GroceryDB;
import AppDB.Ingredient;
import AppDB.ingredientDB;
import AppServer.MessageParser;
import AppServer.Netwok.SendString;

public class IngredientSystem extends AppSystem{

	protected static final String GET_USER_INGREDIENT = "GetMy";
	protected static final String GET_ALL_INGREDIENT = "GetAll";
	protected static final String ADD_USER_INGREDIENT = "Add";
	protected static final String DELETE_USER_INGREDIENT = "Delete";
	
	protected String[] mMsg;
	protected DataInputStream mDIS;
	protected DataOutputStream mDOS;
	
	protected ingredientDB mIngrediendtDB;
	
	public IngredientSystem(String[] msg, DataInputStream dis, DataOutputStream dos) throws SQLException{
		mMsg = msg;
		mDIS = dis;
		mDOS = dos;
		mIngrediendtDB = GroceryDB.getIngredientDB();
	}
	@Override
	public void excuteSystem() throws SQLException, IOException {
		if(mMsg[1].compareTo(IngredientSystem.GET_USER_INGREDIENT)==0)
			getUserIngredient();
		else if(mMsg[1].compareTo(IngredientSystem.GET_ALL_INGREDIENT)==0)
			getAllIngredient();
		else if(mMsg[1].compareTo(IngredientSystem.ADD_USER_INGREDIENT)==0)
			addUserIngredient();
		else if(mMsg[1].compareTo(IngredientSystem.DELETE_USER_INGREDIENT)==0)
			deleteUserIngredient();
		else 
			;
	}
	//------------------------------protected_method-----------------------------------------
	/*-------------------------------------------------------------------
	 * getUserIngredient - MSG FORMAT
	 * "Ingredient"///"GetMy"///ID(String)
	 * 
	 * Result - FORMAT
	 * "Start///foodid(string)///foodname(string)///End"
	 * 
	 ------------------------------------------------------------------*/
	protected void getUserIngredient() throws SQLException, IOException{
		ArrayList<Ingredient> userIngredient = mIngrediendtDB.getUserIngredientList(mMsg[2]);
		ArrayList<String> result = new ArrayList<String>();
		String sendStr;
		
		result.add("Start");
		for(int i=0;i<userIngredient.size();i++){
			Ingredient tmp = userIngredient.get(i);
			result.add(tmp.getIngredientID());
			result.add(tmp.getProductName());
		}			
		result.add("End");
		
		sendStr = MessageParser.wrapMsg(result);
		
		SendString.sendString(sendStr, mDOS);
		System.out.println("���� ����� ����");
	}
	/*-------------------------------------------------------------------
	 * getAllIngredient - MSG FORMAT
	 * "Ingredient"///"GetAll"
	 * 
	 * Result - FORMAT
	 * "Start///foodid(string)///foodname(string)///End"
	 * 
	 ------------------------------------------------------------------*/
	protected void getAllIngredient() throws SQLException, IOException{
		ArrayList<String> userIngredient = mIngrediendtDB.getAllIngredientIDList();
		ArrayList<String> result = new ArrayList<String>();
		String sendStr;
		
		result.add("Start");
		for(int i=0;i<userIngredient.size();i++)
			result.add(userIngredient.get(i));		
		result.add("End");
		
		sendStr = MessageParser.wrapMsg(result);
	
		SendString.sendString(sendStr, mDOS);		
		System.out.println("��� ����� ����");
	}
	/*-------------------------------------------------------------------
	 * addUserIngredient - MSG FORMAT
	 * "Ingredient"///"Add"///userId///ingredientId///ingredientName
	 * 
	 * Result - FORMAT
	 * Success:
	 * "Success"
	 * Fail:
	 * NoId - "Fail"///"UnExistID"
	 * 
	 ------------------------------------------------------------------*/
	protected void addUserIngredient() throws SQLException, IOException{
		java.util.Date today = new java.util.Date();
		java.sql.Date date = new java.sql.Date(today.getTime());

		Ingredient ingredient = new Ingredient();
		ingredient.setIngredientID(mMsg[3]);
		ingredient.setAmount("L");
		ingredient.setBuyDate(date);
		ingredient.setFavorite("N");
		ingredient.setProductName(mMsg[4]);

		ArrayList<String> result = new ArrayList<String>();
		try{
			mIngrediendtDB.insertUserIngredient(mMsg[2], ingredient);
			result.add("Success");
		}catch(SQLException e){
			result.add("Fail");
			result.add("UnExistID");
		}
		String sendStr;
		sendStr = MessageParser.wrapMsg(result);
	
		SendString.sendString(sendStr, mDOS);		
		System.out.println("����� �߰�");
	}
	/*-------------------------------------------------------------------
	 * deleteUserIngredient - MSG FORMAT
	 * "Ingredient"///"Delete"///userId///ingredientName
	 * 
	 * Result - FORMAT
	 * Success:
	 * "Success"
	 * Fail:
	 * NoId - "Fail"
	 * 
	 ------------------------------------------------------------------*/
	protected void deleteUserIngredient() throws SQLException, IOException{
		ArrayList<String> result = new ArrayList<String>();
		try{
			mIngrediendtDB.deleteUserIngredient(mMsg[2], mMsg[3]);
			result.add("Success");
		}catch(SQLException e){
			result.add("Fail");
		}
		String sendStr;
		sendStr = MessageParser.wrapMsg(result);
	
		SendString.sendString(sendStr, mDOS);		
		System.out.println("����� ����");
	}
}