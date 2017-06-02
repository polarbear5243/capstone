package AppServer.System;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import AppDB.GroceryDB;
import AppDB.LoginDB;
import AppDB.RecipeDB;
import AppServer.MessageParser;
import AppServer.Netwok.SendString;

public class Survey extends AppSystem{

	protected static final String FIRST_VISIT = "FirstVisit";
	protected static final String SEARCH_RECIPE = "Search";
	protected static final String GET_RECIPE = "GetInfo";
	protected static final String EVALUATE_RECIPE = "Evaluate";
	protected static final String RECOMMEND_RECIPE = "Recommend";
	
	protected String[] mMsg;
	protected DataInputStream mDIS;
	protected DataOutputStream mDOS;
	
	protected LoginDB mLoginDB;
	
	//------------------------------protected_method-----------------------------------------
	/*-------------------------------------------------------------------
	 * GetAll - MSG FORMAT
	 * "Survey"///"FirstVisit"///userid
	 * 
	 * Result - FORMAT
	 * "True/False"
	 * 
	 ------------------------------------------------------------------*/
	protected void userIsFirstVist() throws SQLException, IOException{
		ArrayList<String> result = new ArrayList<String>();
		boolean tmp;
		String sendStr;
		
		tmp = mLoginDB.userIsVisit(mMsg[2]);
				
		if(tmp)
			result.add("True");
		else
			result.add("False");
		
		sendStr = MessageParser.wrapMsg(result);
		SendString.sendString(sendStr, mDOS);
		
		mLoginDB.updateVisit(mMsg[2]);
	}
	
	public Survey(String[] msg, DataInputStream dis, DataOutputStream dos) throws SQLException{
		mMsg = msg;
		mDIS = dis;
		mDOS = dos;
		mLoginDB = GroceryDB.getLoginDB();
	}

	@Override
	public void excuteSystem() throws SQLException, IOException {
		// TODO Auto-generated method stub
		if(mMsg[1].compareTo(Survey.FIRST_VISIT)==0)
			userIsFirstVist();
		else 
			;
	}

}
