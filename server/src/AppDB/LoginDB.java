package AppDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/*
 * LoginDB.java
 * MySQL 데이터베이스에 연결된 상태에서 로그인 관련 정보를 가져오거나 업데이트 할 수
 * 있도록 함수를 제공해주는 DB 클래스이다.
 * */

public class LoginDB {
	
	protected Connection mConnection = null;
	protected Statement mStatement = null;
	protected PreparedStatement manipulate = null;
	protected ResultSet mResult = null;
	
	public LoginDB(Connection connection) throws SQLException{
		mStatement = connection.createStatement();
		mConnection = connection;
	}
	
	/* boolean compareIDandPW(String id, String pw)
	 * id,pw 를 인풋으로 받아 해당 유저가 DB에 등록이 되어있는 지 확인합니다.
	 * 등록되어 있으면 true 를 리턴합니다.
	 * */
	public boolean compareIDandPW(String id, String pw) throws SQLException{
		mResult = mStatement.executeQuery("SELECT count(*) FROM userinfo WHERE userid ='" + id + "' And password ='" + pw + "';");	
		mResult.next();
		
		if (mResult.getInt(1) > 0)
			return true;
		else 
			return false;
	}
	
	
	/* boolean isExistUserDeviceid(String id, String devId)
	 * devId, id 를 인풋으로 받아 해당 유저에 대해 디바이스가 등록이 되어있는지 확인합니다.
	 * 등록되어 있으면 true 를 리턴합니다.
	 * */
	public boolean isExistUserDeviceid(String id, String devId) throws SQLException{
		mResult = mStatement.executeQuery("SELECT count(*) FROM device " + "WHERE userid ='" + id + "' AND deviceid ='"+ devId + "';");	
		mResult.next();
		
		if (mResult.getInt(1) > 0)
			return true;
		else
			return false;
	}
	
	
	/* boolean isExistUserID(String id)
	 * id를 인풋으로 받아 해당 유저가 DB에 등록이 되어있는 지 확인합니다.
	 * 
	 * */
	public boolean isExistUserID(String id) throws SQLException{
		mResult = mStatement.executeQuery("SELECT count(*) FROM userinfo " + "WHERE userid ='" + id + "';");	
		mResult.next();
		
		if (mResult.getInt(1) > 0)
			return true;
		else
			return false;
	}
	
	/* void registerUser(String id, String pw)
	 * id,pw 를 받아 해당 유저를 DB에 등록합니다.
	 * 
	 * */
	public void registerUser(String id, String pw) throws SQLException{
		manipulate = mConnection.prepareStatement("INSERT INTO userinfo(userid,password) VALUES(?,?);");
		manipulate.setString(1,id);
		manipulate.setString(2,pw);
		manipulate.executeUpdate();				
	}
	
	/* void registerDevice(String id, String devId)
	 * id,pw 를 받아 해당 유저의 디바이스로 DB에 등록합니다.
	 * 
	 * */
	public void registerDevice(String id, String devId) throws SQLException{
		
		if(isExistUserDeviceid(id, devId) == true)
			return;
		
		manipulate = mConnection.prepareStatement("INSERT INTO device(deviceid,userid) VALUES(?,?);");
		manipulate.setString(1,devId);
		manipulate.setString(2,id);
		manipulate.executeUpdate();
	}
	
	/* boolean userIsVisit(String userid)
	 * userid 를 인풋으로 받아 해다 유저가 처음 접속인지 아닌지 판별합니다.
	 * 처음 접속이면 true 를 리턴합니다.
	 * */
	public boolean userIsVisit(String userid) throws SQLException{
		mResult = mStatement.executeQuery("SELECT * FROM userinfo " + "WHERE userid ='" + userid + "' and init = 0;");	
		
		if (mResult.next())
			return true;
		else
			return false;
	}
	
	/* void updateVisit(String userid)
	 * userid 를 인풋으로 받아 유저가 처음 접속하는 경우를 등록합니다.
	 * 
	 * */
	public void updateVisit(String userid) throws SQLException{
		String qurery = "UPDATE userinfo set init=1 WHERE userid = '" + userid + "';";
		mStatement.executeUpdate(qurery);

	}

}//end of LoginDB