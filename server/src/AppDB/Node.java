package AppDB;

import java.sql.SQLException;
import java.util.ArrayList;
import AppDB.Edge;

/*
 * Node.java
 * 레시피 네트워크의 노드 클래스이다.
 * 
 * */

public class Node {
	private int recipeId;
	private ArrayList<Edge> EdgeList;

	public Node(int recipeId, ArrayList<Edge> data){
		this.recipeId = recipeId;
		EdgeList = data;
	}
	
	public int getRecipeId(){
		return recipeId;
	}
	
	public ArrayList<Edge> getEdgeList(){return EdgeList;}
	
	public Edge getEdgeBytoRecipeId(int toRecipeId){
		for (int i = 0 ; i < EdgeList.size() ; i++){
			if (EdgeList.get(i).getToRecipe() == toRecipeId) return EdgeList.get(i);
		}
		return null;
	}

}//end of Node
