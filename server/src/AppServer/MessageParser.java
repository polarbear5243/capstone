package AppServer;

import java.util.ArrayList;

/*
 * MessageParser.java
 * 서버와 클라이언트 사이의 교환되는 데이터 포맷을 만들거나 추출하는 클래스이다.
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
