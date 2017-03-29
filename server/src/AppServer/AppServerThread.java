package AppServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class AppServerThread extends Thread{
//-----STATIC
	static final public int PORT_NUM = 26100;

//-----PROTECTED
	ServerSocket mServerSocket;
	LinkedList<ClientServerThread> mClient;
	
//-----PUBLIC

	
//-----STATIC


//-----PROTECTED
	

	
//-----PUBLIC
	public AppServerThread() throws IOException{
		mServerSocket = new ServerSocket(AppServerThread.PORT_NUM);
		mClient = new LinkedList<ClientServerThread>();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		Socket tmpSocket = null;
		ClientServerThread tmpClientServerThread = null;
		while(true){
			try {
				tmpSocket = mServerSocket.accept();
				tmpClientServerThread = new ClientServerThread(tmpSocket);
				
				mClient.add(tmpClientServerThread);
				tmpClientServerThread.start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
