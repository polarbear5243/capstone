package AppDB;

import java.sql.SQLException;
import java.util.ArrayList;
import AppDB.Edge;

public class Node {
	private int recipeId;
	private double probability;
	private ArrayList<Edge> EdgeList;

	public Node(int recipeId, ArrayList<Edge> data){
		this.recipeId = recipeId;
		EdgeList = data;
	}
	
	public void calculateProbability(){
		
	}
	
	public double getProbability(){
		return this.probability;
	}
	
	public int getRecipeId(){
		return recipeId;
	}
	
	public Edge getEdgeBytoRecipeId(int toRecipeId){
		for (int i = 0 ; i < EdgeList.size() ; i++){
			if (EdgeList.get(i).getToRecipe() == toRecipeId) return EdgeList.get(i);
		}
		return null;
	}
}
