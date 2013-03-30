/* MachinePlayer.java */

package player;

import list.*;

/**
 * An implementation of an automatic Network player. Keeps track of moves made
 * by both players. Can select a move for itself.
 */
public class MachinePlayer extends Player {

	public static final boolean TESTCODE = true;
	public static final boolean RANDOMBOT = true;
	public static final int WHITE = 1;
	public static final int BLACK = 0;
	public static final int SEARCHDEPTH = 2;
	protected Board gameboard;
	protected int color;
	protected int searchDepth;

	// Creates a machine player with the given color. Color is either 0 (black)
	// or 1 (white). (White has the first move.)
	public MachinePlayer(int color) {
		this.color = color;
		gameboard = new Board();

	}

	// Creates a machine player with the given color and search depth. Color is
	// either 0 (black) or 1 (white). (White has the first move.)
	public MachinePlayer(int color, int searchDepth) {
		this(color);
		this.searchDepth = searchDepth;
	}

	// Returns a new move by "this" player. Internally records the move (updates
	// the internal game board) as a move by "this" player.
	public Move chooseMove() {
		Move m;
		if (TESTCODE) {
			DList allMoves = gameboard.validMoves(color);
			// this.color-color
			DListNode aNode = allMoves.front();
			System.out
					.print("\n\n This is list of all moves the robot thinks is ok. Currently choses first option.\n");
			for (int i = 0; i < allMoves.length(); i++) {
				System.out.println(aNode.item);
				aNode = allMoves.next(aNode);
			}
			if (RANDOMBOT) {
				m = (Move) allMoves.random().item;
			} else {
				m = (Move) aNode.item;
			}
		} else {
			System.out.print(gameboard.numPieces());
			if (gameboard.numPieces() <= 1)

			{
				if (color == BLACK) {
					m = new Move(2, 0);
				} else {
					m = new Move(0, 2);
				}
			} else if (gameboard.numPieces() <= 3) {

				if (color == BLACK) {
					m = new Move(4, 7);
				} else {
					m = new Move(7, 4);
				}
			} else {
				m = bestMove(gameboard, SEARCHDEPTH, color).move;
			}
		}
		
		DList pieces=gameboard.pieces();
		DListNode aNode=pieces.front();
		System.out.print(pieces+ "\n");
		for(int i=0; i<pieces.length(); i++)
		{
			System.out.println(aNode.item+" sees "+((Chip) aNode.item).inSightString());
			aNode=pieces.next(aNode);
		}
		
		gameboard.makeMove(color, m);
		
		
		
		return m;
	}

	// If the Move m is legal, records the move as a move by the opponent
	// (updates the internal game board) and returns true. If the move is
	// illegal, returns false without modifying the internal state of "this"
	// player. This method allows your opponents to inform you of their moves.
	public boolean opponentMove(Move m) {
		
		boolean isvalid= gameboard.makeMove(otherPlayer(color), m);
		
		if(!isvalid)
		{
			int pi=1/0; //TODO remove this intentionally faulty code.
		}
		return isvalid;
	}

	// If the Move m is legal, records the move as a move by "this" player
	// (updates the internal game board) and returns true. If the move is
	// illegal, returns false without modifying the internal state of "this"
	// player. This method is used to help set up "Network problems" for your
	// player to solve.
	public boolean forceMove(Move m) {
		return gameboard.makeMove(color, m);
	}

	private Best bestMove(Board board, int searchDepth, int color) {
		Best myBest;

		if (searchDepth == 0 || board.isFinished()) {
			return new Best(board.value(this.color));
		}

		if (color == this.color) {
			myBest = new Best(Board.LOWESTVAL);
		} else {
			myBest = new Best(Board.HIGHESTVAL);
		}

		DList allMoves = board.validMoves(color);
		DListNode aNode = allMoves.front();

		for (int i = 0; i < allMoves.length(); i++) {
			Best reply = bestMove(new Board(board, color, (Move) aNode.item),
					searchDepth - 1, otherPlayer(color));
			if (((color == this.color) && (reply.score >= myBest.score))
					|| ((color != this.color) && (reply.score <= myBest.score))) {
				myBest.move = (Move) aNode.item;
				myBest.score = reply.score;
			}
		}
		return myBest;
	}

	private int otherPlayer(int color) {
		if (color == WHITE) {
			return BLACK;
		} else {
			return WHITE;
		}
	}

}
