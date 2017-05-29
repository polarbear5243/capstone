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
	
	public void setValue(int i,double val){value[i] = val;}
	
	public int getFromRecipe(){return from;}
	public int getToRecipe(){return to;}
	public double getValue(int i){return value[i];}
}
