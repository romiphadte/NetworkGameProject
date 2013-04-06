package player;

class Best{

	public double score=0;
	public Move move=null;
	
    /**
     * Contructs a Best object that creates a STEP move along with a score
     *
     * @param xx1
     * the new x
     * @param yy1
     * the new y
     * @param xx2
     * the old x
     * @param yy2
     * the old y
     * @param score
     * the score associated with this move
     */
	public Best(int xx1, int yy1, int xx2, int yy2, int score) {
		this.move=new Move(xx1, yy1, xx2, yy2);
		this.score=score;
	}

    /**
     * Constructs a Best object that creates an ADD move along with a score
     *
     * @param x
     * the new x
     * @param y
     * the new y
     * @param score
     * the score associated with this move
     */
	public Best(int x, int y,double score) {
		move=new Move(x, y);
		this.score=score;
	}

    /**
     * Constructs a Best object that holds the Move along with a score
     *
     * @param move
     * the "best" Move
     * @param score
     * the score associated with this move
     */
	public Best(Move move, double score){
		this.move=move;
		this.score=score;
	}
	
    /**
     * Constructs a Best object that holds a score
     *
     * @param score
     * the "best" score
     */
	public Best(double score) {
		this.score=score;
	}

}
