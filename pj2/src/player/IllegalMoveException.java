package player;

public class IllegalMoveException extends Exception {

	public IllegalMoveException() {
		super("The player has made an illegal move!");
	}

	public IllegalMoveException(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public IllegalMoveException(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public IllegalMoveException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

}
