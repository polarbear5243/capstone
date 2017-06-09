package AppServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

/*
 * AppServerThread.java
 * 서버와 클라이언트의 통신을 할 수 있도록 연결해주는 쓰레드 클래스이다.
 * 
 * */

public class AppServerThread extends Thread{

	static final public int PORT_NUM = 26100;

	ServerSocket mServerSocket;
	LinkedList<ClientServerThread> mClient;
	
	public AppServerThread() throws IOException{
		mServerSocket = new ServerSocket(AppServerThread.PORT_NUM);
		mClient = new LinkedList<ClientServerThread>();
	}

	@Override
	public void run() {
		
		Socket tmpSocket = null;
		ClientServerThread tmpClientServerThread = null;
		while(true){
			try {	
				tmpSocket = mServerSocket.accept();
				tmpClientServerThread = new ClientServerThread(tmpSocket);
				
				mClient.add(tmpClientServerThread);
				tmpClientServerThread.start();
				
				System.out.println("접속!");

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}//end of AppServerThread
