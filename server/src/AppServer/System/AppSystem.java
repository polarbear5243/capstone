package AppServer.System;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.SQLException;

public abstract class AppSystem {
	public static final String LOGIN = "Login";
	public static final String INGREDIENT = "Ingredient";
	public static final String RECIPE = "Recipe";
	public static final String SURVEY = "Survey";
	
	public static AppSystem getInstance(String[] msg, DataInputStream dis, DataOutputStream dos) throws SQLException{
		if(msg[0].compareTo(AppSystem.LOGIN) == 0)
			return new LoginSystem(msg, dis, dos);
		else if(msg[0].compareTo(AppSystem.INGREDIENT) == 0)
			return new IngredientSystem(msg, dis, dos);
		else if(msg[0].compareTo(AppSystem.RECIPE) == 0)
			return new RecipeSystem(msg, dis, dos);
		else if(msg[0].compareTo(AppSystem.SURVEY) == 0)
			return new Survey(msg, dis, dos);
		return null;
	}
	//-------------------------------------------------------------------------------------------
	abstract public void excuteSystem() throws SQLException, IOException;
}
