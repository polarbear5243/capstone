package AppDB;

public class Edge {
	private int from;
	private int to;
	private double[] value = new double[4];
	// index 0 : ��
	// index 1 : �����
	// index 2 : ī�װ�
	// index 3 : ����
	
	public Edge(int from,int to){
		this.from = from;
		this.to = to;
	}
	
	void setValue(int i,double val){value[i] = val;}
	
	int getFromRecipe(){return from;}
	int getToRecipe(){return to;}
	double getValue(int i){return value[i];}
}
