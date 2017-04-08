package AppServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;

import AppServer.Netwok.ReadString;
import AppServer.System.AppSystem;

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
		// TODO Auto-generated method stub
		String clientRequest;
		String parsedMsg[];
		AppSystem system; 
		
		int debug;
		debug = 1;
		
		try {
			clientRequest = ReadString.readString(mDataInputStream);
			parsedMsg = MessageParser.parsingMsg(clientRequest);
			
			try {
				system = AppSystem.getInstance(parsedMsg,mDataInputStream,mDataOutputStream);
				if(system != null)
					system.excuteSystem();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
