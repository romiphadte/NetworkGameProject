package player;

public class Best{

	public int score=0;
	public Move move=null;
	
	public Best(int xx1, int yy1, int xx2, int yy2, int score) {
		this.move=new Move(xx1, yy1, xx2, yy2);
		this.score=score;
	}

	public Best(int x, int y,int score) {
		move=new Move(x, y);
		this.score=score;
	}

	public Best(Move move, int score){
		this.move=move;
		this.score=score;
	}
	
	public Best(int score) {
		this.score=score;
	}

}
