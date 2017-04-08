package AppServer.Netwok;

import java.io.DataInputStream;
import java.io.IOException;

public class ReadString {
	private static byte[] input = new byte[8192];
	
	static public String readString(DataInputStream dis) throws IOException{
//		String result = dis.readLine();

		int len = dis.read(input);
		byte [] byteStr = new byte[len];
		
		System.arraycopy(input, 0, byteStr, 0, len);
		
		String result = new String(byteStr,"UTF-8");
//		result = new String(result.getBytes("UTF-8"),"MS949");
		
//		int resul = result.length();
		
		return result;
	}
}
