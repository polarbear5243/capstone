package AppServer.System;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.SQLException;

public abstract class AppSystem {
	public static final String LOGIN = "Login";
	
	public static AppSystem getInstance(String[] msg, DataInputStream dis, DataOutputStream dos) throws SQLException{
		if(msg[0].compareTo(AppSystem.LOGIN) == 0)
			return new LoginSystem(msg, dis, dos);
		return null;
	}
	//-------------------------------------------------------------------------------------------
	abstract public void excuteSystem() throws SQLException, IOException;
}
