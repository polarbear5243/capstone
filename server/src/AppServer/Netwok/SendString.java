package AppServer.Netwok;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class SendString {
	
	static final String delimiter = "!@#$"; 
	
	public static void sendString(String str, DataOutputStream dos) throws IOException{
		str+=delimiter;
		byte[] byteData = str.getBytes("UTF-8");
		dos.write(byteData, 0, byteData.length);
	}
}
