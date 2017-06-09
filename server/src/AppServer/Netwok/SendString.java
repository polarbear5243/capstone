package AppServer.Netwok;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/*
 * SendString.java
 * 클라이언트로 보낼 데이터 버퍼에 String 을 쓰는 클래스이다.
 * 
 * */

public class SendString {
	
	static final String delimiter = "!@#$"; 
	
	public static void sendString(String str, DataOutputStream dos) throws IOException{
		str+=delimiter;
		byte[] byteData = str.getBytes("UTF-8");
		dos.write(byteData, 0, byteData.length);
	}

}//end of SendString
