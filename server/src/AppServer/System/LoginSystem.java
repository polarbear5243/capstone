package AppServer.System;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import AppDB.GroceryDB;
import AppDB.LoginDB;
import AppServer.MessageParser;

public class LoginSystem extends AppSystem{

	protected static final String MANUAL_LOGIN = "Manual";
	protected static final String AUTO_LOGIN = "Auto";
	protected static final String REGISTER = "Register";
	
	protected String[] mMsg;
	protected DataInputStream mDIS;
	protected DataOutputStream mDOS;
	
	protected LoginDB mLoginDB;
	
	//------------------------------protected_method-----------------------------------------
	/*-------------------------------------------------------------------
	 * Manual - MSG FORMAT
	 * "Login"///"Manual"///ID(String)///PW(String)///DeviceID(String)
	 * 
	 * Result - FORMAT
	 * Success :			"Success"
	 * Fail-UnExistID :		"Fail"///"UnExistID"
	 * Fail-WrongPW :		"Fail"///"WrongPW"
	 * 
	 ------------------------------------------------------------------*/
	protected void manualLogin() throws SQLException, IOException{
		ArrayList<String> result = new ArrayList<String>();
		String sendStr;
		byte[] sendData;
		
		if(mLoginDB.compareIDandPW(mMsg[2], mMsg[3]) == true){
			try{
				mLoginDB.registerDevice(mMsg[2], mMsg[4]);
			} catch(SQLException e){
				// �̰� ó���ؾ���.
				e.printStackTrace();
			}
			System.out.println("�α��� ����");
			result.add("Success");
		}
		else{
			result.add("Fail");
			if(mLoginDB.isExistUserID(mMsg[2]) == true){
				// ���̵� ������ ����� Ʋ����
				result.add("WrongPW");				
			}
			else {
				// ���̵� ����.
				result.add("UnExistID");
			}

			System.out.println("�α��� ����");
		}

		sendStr = MessageParser.wrapMsg(result);
		sendData = sendStr.getBytes();
		mDOS.write(sendData,0,sendData.length);
	}
	/*-------------------------------------------------------------------
	 * Auto - MSG FORMAT
	 * "Login"///"Auto"///ID(String)///PW(String)///DeviceID(String)
	 * 
	 * Result - FORMAT
	 * Success :				"Success"
	 * Fail-WrongInfo :			"Fail"///"WrongInfo"
	 * Fail-WrongDevice :		"Fail"///"WrongDevice"
	 * 
	 ------------------------------------------------------------------*/	
	protected void autoLogin() throws SQLException, IOException{
		ArrayList<String> result = new ArrayList<String>();
		String sendStr;
		byte[] sendData;
		
		if(mLoginDB.compareIDandPW(mMsg[2], mMsg[3]) == true){
			if(mLoginDB.isExistUserDeviceid(mMsg[2], mMsg[4]) == true){
				result.add("Success");				
			}
			else{
				result.add("Fail");
				result.add("WrongDevice");				
			}
		}
		else{
			result.add("Fail");
			result.add("WrongInfo");
		}
		
		sendStr = MessageParser.wrapMsg(result);
		sendData = sendStr.getBytes();
		mDOS.write(sendData,0,sendData.length);
	}
	/*-------------------------------------------------------------------
	 * Register - MSG FORMAT
	 * "Login"///"Register"///ID(String)///PW(String)
	 * 
	 * Result - FORMAT
	 * Success :				"Success"
	 * Fail-ExistID :			"Fail"///"ExistID"
	 * 
	 ------------------------------------------------------------------*/	
	protected void register() throws SQLException, IOException{
		ArrayList<String> result = new ArrayList<String>();
		String sendStr;
		byte[] sendData;
		
		if(mLoginDB.isExistUserID(mMsg[2]) == true){
			//�̹� �̾� ID
			result.add("Fail");
			result.add("ExistID");			
		}
		else {
			mLoginDB.registerUser(mMsg[2], mMsg[3]);
			result.add("Success");
			System.out.println("����� �߰� �Ϸ�");
		}
		
		sendStr = MessageParser.wrapMsg(result);
		sendData = sendStr.getBytes();
		mDOS.write(sendData,0,sendData.length);		
	}
	//-------------------------------public_method-------------------------------------------
	public LoginSystem(String[] msg, DataInputStream dis, DataOutputStream dos) throws SQLException{
		mMsg = msg;
		mDIS = dis;
		mDOS = dos;
		mLoginDB = GroceryDB.getLoginDB();
	}

	public void excuteSystem() throws SQLException, IOException {
		if(mMsg[1].compareTo(LoginSystem.MANUAL_LOGIN)==0)
			manualLogin();
		else if(mMsg[1].compareTo(LoginSystem.AUTO_LOGIN)==0)
			autoLogin();
		else if(mMsg[1].compareTo(LoginSystem.REGISTER)==0)
			register();
		else 
			;
	}
	
	
}