package AppDB;

public class Edge {
	private int from;
	private int to;
	private double[] value = new double[4];
	// index 0 : 맛
	// index 1 : 식재료
	// index 2 : 카테고리
	// index 3 : 나라
	
	public Edge(int from,int to){
		this.from = from;
		this.to = to;
	}
	
	void setValue(int i,double val){value[i] = val;}
	
	int getFromRecipe(){return from;}
	int getToRecipe(){return to;}
	double getValue(int i){return value[i];}
}
