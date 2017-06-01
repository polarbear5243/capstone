import java.io.IOException;
import java.sql.SQLException;

import AppDB.Graph;
import AppDB.GroceryDB;
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
			
			long beforetime = System.currentTimeMillis();
			Graph myGraph = null;
			try {
				myGraph = new Graph(GroceryDB.getRecipeDB(),GroceryDB.getIngredientDB());
				int[] priority = new int[4];
				priority[0] = 1;
				priority[1] = 2;
				priority[2] = 3;
				priority[3] = 4;
				int[] recentRecipes = new int [1];
				recentRecipes[0] = 1;
				double[][] result = myGraph.getUserRecomandRecipes("john419", priority, recentRecipes);
				if(5 == 5){
					System.out.println(result[0][0]);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			long aftertime = System.currentTimeMillis();
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
