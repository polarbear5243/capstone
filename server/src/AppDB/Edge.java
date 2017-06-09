package AppDB;

/*
 * Edge.java
 * 레시피 사이의 4가지 상관 관계를 나타내는 값을 가지고 있는 간선 클래스이다.
 * Edge.value index 0 : 맛, index 1 : 식재료, index 2 : 카테고리, index 3 : 나라
 * */

public class Edge {
	private int from;
	private int to;
	private double[] value = new double[4];
	
	public Edge(int from,int to){
		this.from = from;
		this.to = to;
	}
	
	public void setValue(int i,double val){value[i] = val;}
	
	public int getFromRecipe(){return from;}
	public int getToRecipe(){return to;}
	public double getValue(int i){return value[i];}
}//end of Edge
