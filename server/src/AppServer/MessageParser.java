package AppServer;

import java.util.ArrayList;

/*
 * MessageParser.java
 * ������ Ŭ���̾�Ʈ ������ ��ȯ�Ǵ� ������ ������ ����ų� �����ϴ� Ŭ�����̴�.
 * */

public class MessageParser {
	private static final String delimiter = "///";
	
	public static String[] parsingMsg(String str){
		 String [] result;
		 result = str.split(delimiter);
		 return result;
	}
	
	public static String wrapMsg(ArrayList<String> str){
		String result="";
		
		for(int i=0;i<str.size();i++)
			result = result + str.get(i) + MessageParser.delimiter;
		result = result + str.get(str.size()-1);

		return result;
	}

}//end of MessageParser
