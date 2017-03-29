package AppDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LoginDB {
	
	protected Connection mConnection = null;
	protected Statement mStatement = null;
	protected PreparedStatement manipulate = null;
	protected ResultSet mResult = null;
	
	public LoginDB(Connection connection) throws SQLException{
		mStatement = connection.createStatement();
		mConnection = connection;
	}
	public boolean isExistUserID(String id, String pw) throws SQLException{
		mResult = mStatement.executeQuery("SELECT count(*) FROM userinfo WHERE userid ='" + id + "';");	
		mResult.next();
		
		if (mResult.getInt(1) > 0)
			return true;
		else 
			return false;
	}
	public boolean isExistUserDeviceid(String id, String devId) throws SQLException{
		mResult = mStatement.executeQuery("SELECT count(*) FROM device "
				+ "WHERE userid ='" + id + "' AND deviceid ='"+ devId + "';");	
		mResult.next();
		
		if (mResult.getInt(1) > 0)
			return true;
		else
			return false;
	}
	public boolean isExistUserID(String id) throws SQLException{
		mResult = mStatement.executeQuery("SELECT count(*) FROM device "
				+ "WHERE userid ='" + id + "';");	
		mResult.next();
		
		if (mResult.getInt(1) > 0)
			return true;
		else
			return false;
	}
	public void registerUser(String id, String pw) throws SQLException{
		manipulate = mConnection.prepareStatement("INSERT INTO userinfo(userid,password) VALUES(?,?)");
		manipulate.setString(1,id);
		manipulate.setString(2,pw);
		manipulate.executeUpdate();				
	}
	public void registerDevice(String id, String devId) throws SQLException{
		
		if(isExistUserDeviceid(id, devId) == true)
			return;
		
		manipulate = mConnection.prepareStatement("INSERT INTO userinfo(userid,password) VALUES(?,?)");
		manipulate.setString(1,id);
		manipulate.setString(2,devId);
		manipulate.executeUpdate();		
	}
}