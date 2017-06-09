package AppServer.Netwok;

import java.io.DataInputStream;
import java.io.IOException;

/*
 * ReadString.java
 * 클라이언트가 보낸 데이터 버퍼에서 String 을 읽어오는 클래스이다.
 * 
 * */

public class ReadString {
	private static byte[] input = new byte[8192];
	
	static public String readString(DataInputStream dis) throws IOException{

		int len = dis.read(input);
		byte [] byteStr = new byte[len];
		
		System.arraycopy(input, 0, byteStr, 0, len);
		
		String result = new String(byteStr,"UTF-8");
		
		return result;
	}

}//end of ReadString
