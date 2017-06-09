package AppDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/*
 * LoginDB.java
 * MySQL �����ͺ��̽��� ����� ���¿��� �α��� ���� ������ �������ų� ������Ʈ �� ��
 * �ֵ��� �Լ��� �������ִ� DB Ŭ�����̴�.
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
	 * id,pw �� ��ǲ���� �޾� �ش� ������ DB�� ����� �Ǿ��ִ� �� Ȯ���մϴ�.
	 * ��ϵǾ� ������ true �� �����մϴ�.
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
	 * devId, id �� ��ǲ���� �޾� �ش� ������ ���� ����̽��� ����� �Ǿ��ִ��� Ȯ���մϴ�.
	 * ��ϵǾ� ������ true �� �����մϴ�.
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
	 * id�� ��ǲ���� �޾� �ش� ������ DB�� ����� �Ǿ��ִ� �� Ȯ���մϴ�.
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
	 * id,pw �� �޾� �ش� ������ DB�� ����մϴ�.
	 * 
	 * */
	public void registerUser(String id, String pw) throws SQLException{
		manipulate = mConnection.prepareStatement("INSERT INTO userinfo(userid,password) VALUES(?,?);");
		manipulate.setString(1,id);
		manipulate.setString(2,pw);
		manipulate.executeUpdate();				
	}
	
	/* void registerDevice(String id, String devId)
	 * id,pw �� �޾� �ش� ������ ����̽��� DB�� ����մϴ�.
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
	 * userid �� ��ǲ���� �޾� �ش� ������ ó�� �������� �ƴ��� �Ǻ��մϴ�.
	 * ó�� �����̸� true �� �����մϴ�.
	 * */
	public boolean userIsVisit(String userid) throws SQLException{
		mResult = mStatement.executeQuery("SELECT * FROM userinfo " + "WHERE userid ='" + userid + "' and init = 0;");	
		
		if (mResult.next())
			return true;
		else
			return false;
	}
	
	/* void updateVisit(String userid)
	 * userid �� ��ǲ���� �޾� ������ ó�� �����ϴ� ��츦 ����մϴ�.
	 * 
	 * */
	public void updateVisit(String userid) throws SQLException{
		String qurery = "UPDATE userinfo set init=1 WHERE userid = '" + userid + "';";
		mStatement.executeUpdate(qurery);

	}

}//end of LoginDB