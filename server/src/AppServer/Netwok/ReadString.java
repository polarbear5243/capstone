package AppServer.Netwok;

import java.io.DataInputStream;
import java.io.IOException;

/*
 * ReadString.java
 * Ŭ���̾�Ʈ�� ���� ������ ���ۿ��� String �� �о���� Ŭ�����̴�.
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
