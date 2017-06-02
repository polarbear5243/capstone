import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;

import AppDB.Graph;
import AppDB.GroceryDB;
import AppDB.Recipe;
import AppServer.AppServerThread;

public class Server {
	
	static AppServerThread mAppServer;
	
	public static void main(String[] args) {
		
		// TODO Auto-generated method stub
		try {
			
			GroceryDB.initDB();
			
			System.out.println("DB OPEN");
			
			mAppServer = new AppServerThread();
			mAppServer.start();
			
			long beforetime = 0, aftertime = 0;
			
			Graph myGraph = null;
			try {
				myGraph = new Graph(GroceryDB.getRecipeDB(),GroceryDB.getIngredientDB());
				int[] priority = new int[4];
				priority[0] = 2;
				priority[1] = 3;
				priority[2] = 4;
				priority[3] = 1;
				int[] recentRecipes = new int [1];
				recentRecipes[0] = 569; //레시피 변경 테스트
				beforetime = System.currentTimeMillis();
				double[][] result = myGraph.getUserRecomandRecipes("john419", priority, recentRecipes);
				aftertime = System.currentTimeMillis();	
				Recipe data = null;
				AppDB.Node Nodedata = null;
				
				for (int j = 0 ; j < Graph.getNodeList().size(); j++){
					if (Graph.getNodeList().get(j).getRecipeId() == 569){	//레시피 변경 테스트
						Nodedata = Graph.getNodeList().get(j);
						break;
					}
				}
				BufferedWriter out = new BufferedWriter(new FileWriter("꿔바로우_2341.csv"));
				String s = "순위,이름,총점,맛,재료,카테고리,나라\n";
				out.write(s);
				AppDB.Edge edgedata = null;
				for (int i = 0 ; i < Graph.getNodeList().size() - 1; i++){
					data = GroceryDB.getRecipeDB().getRecipeInfoByID(Integer.toString((int)result[0][i]));
					edgedata = Nodedata.getEdgeBytoRecipeId((int)result[0][i]);
					try {
					      ////////////////////////////////////////////////////////////////
						  s = Integer.toString(i) + "," + data.getRecipeName() + ","+result[1][i] + "," + edgedata.getValue(0) + "," +
								  edgedata.getValue(1) + "," + edgedata.getValue(2) + "," + edgedata.getValue(3) + "\n";
					      out.write(s);
					      
					      ////////////////////////////////////////////////////////////////
					    } catch (IOException e) {
					        System.err.println(e); 
					        
					    }
					/*System.out.println(i+"위 : "+data.getRecipeName() + " // " + result[1][i] + "           " + "맛 : " + edgedata.getValue(0)
					+ "      재료 : " + edgedata.getValue(1));
					*/
				}
				out.close();
			    System.out.println("파일 쓰기 완료");  
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			System.out.println("사용자 추천하는데 걸리는 시간 : " + (aftertime - beforetime) + "ms 걸림");
			/*
			long beforetime = System.currentTimeMillis();
			Graph myGraph = null;
			try {
				myGraph = new Graph(GroceryDB.getRecipeDB(),GroceryDB.getIngredientDB());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			long aftertime = System.currentTimeMillis();
			
			AppDB.Node node = myGraph.getNodeList().get(0);
			System.out.println("레시피 ID : " + node.getRecipeId() + " 와 가장 가까운 레시피는 ?" );
			java.util.ArrayList<AppDB.Edge> list = node.getEdgeList();
			double Score = list.get(0).getValue(0) + list.get(0).getValue(1) + list.get(0).getValue(2) + list.get(0).getValue(3);
			int closeRecipe = list.get(0).getToRecipe();
			
			for (int i = 0 ; i < list.size(); i++){
				if (list.get(i).getValue(0) + list.get(i).getValue(1) + list.get(i).getValue(2) + list.get(i).getValue(3) > Score){
					Score = list.get(i).getValue(0) + list.get(i).getValue(1) + list.get(i).getValue(2) + list.get(i).getValue(3);
					closeRecipe = list.get(i).getToRecipe();
				}
			}
			
			System.out.println("레시피 ID : " + closeRecipe);
			*/
			
			//System.out.println("그래프 생성 시 걸리는 시간 : " + (aftertime - beforetime) + "ms 걸림");
			/*
			AppDB.Node node = myGraph.getNodeList().get(0);
			AppDB.Edge edge = node.getEdgeBytoRecipeId(3);
			double value = edge.getValue(0) + edge.getValue(1) + edge.getValue(2) + edge.getValue(3);
			System.out.println("레시피 ID : " + node.getRecipeId() + " , " + "SCORE : " + value );
			
			AppDB.Node node2 = null;
			for (int i = 0 ; i < myGraph.getNodeList().size(); i++){
				if (myGraph.getNodeList().get(i).getRecipeId() == 3){
					node2 = myGraph.getNodeList().get(i);
					break;
				}
			}
			AppDB.Edge edge2 = node2.getEdgeBytoRecipeId(1);
			value = edge2.getValue(0) + edge2.getValue(1) + edge2.getValue(2) + edge2.getValue(3);
			System.out.println("레시피 ID : " + node2.getRecipeId() + " , " + "SCORE : " + value);
			*/
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}

}
