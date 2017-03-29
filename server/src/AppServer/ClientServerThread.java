package AppServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;

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
		
		//while true¸¦ »ìÂ¦ ¹Ù²ã¾ßÇÔ. ³ªÁß¿¡
		while(true){
			try {
				clientRequest = mDataInputStream.readLine();
				parsedMsg = MessageParser.parsingMsg(clientRequest);
				
				try {
					system = AppSystem.getInstance(parsedMsg,mDataInputStream,mDataOutputStream);
					system.excuteSystem();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				break;
			}
		}
	}
}
