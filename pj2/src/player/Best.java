package player;

public class Best{

	public double score=0;
	public Move move=null;
	
	public Best(int xx1, int yy1, int xx2, int yy2, int score) {
		this.move=new Move(xx1, yy1, xx2, yy2);
		this.score=score;
	}

	public Best(int x, int y,double score) {
		move=new Move(x, y);
		this.score=score;
	}

	public Best(Move move, double score){
		this.move=move;
		this.score=score;
	}
	
	public Best(double score) {
		this.score=score;
	}

}
