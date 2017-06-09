package AppServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;

import AppServer.Netwok.ReadString;
import AppServer.System.AppSystem;

/*
 * ClientServerThread.java
 * ���� ������ �Ǹ� Ŭ���̾�Ʈ�� ���� ���̿��� ������ ��ȯ�� ����ϴ� Ŭ�����̴�.
 * 
 * */

public class ClientServerThread extends Thread{

	Socket mClientSocket;
	DataInputStream mDataInputStream;
	DataOutputStream mDataOutputStream;
	
	public ClientServerThread(Socket socket) throws IOException{
		mClientSocket = socket;
		mDataInputStream = new DataInputStream(socket.getInputStream());
		mDataOutputStream = new DataOutputStream(socket.getOutputStream());
	}
	
	@Override
	public void run() {
		String clientRequest;
		String parsedMsg[];
		AppSystem system; 
		
		try {
			clientRequest = ReadString.readString(mDataInputStream);
			parsedMsg = MessageParser.parsingMsg(clientRequest);
			
			try {
				system = AppSystem.getInstance(parsedMsg,mDataInputStream,mDataOutputStream);
				if(system != null)
					system.excuteSystem();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}//end of ClientServerThread
