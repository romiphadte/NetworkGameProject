/* MachinePlayer.java */

package player;

import list.*;

/**
 * An implementation of an automatic Network player. Keeps track of moves made
 * by both players. Can select a move for itself.
 */
public class MachinePlayer extends Player {

	public static final boolean TESTCODE = false;
	public static final boolean RANDOMBOT = false;
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
		this.searchDepth = SEARCHDEPTH;
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
			/*
			 * System.out.print(
			 * "\n\n This is list of all moves the robot thinks is ok. Currently choses first option.\n"
			 * ); for (int i = 0; i < allMoves.length(); i++) {
			 * System.out.println(aNode.item); aNode = allMoves.next(aNode); }
			 */
			if (RANDOMBOT) {
				m = (Move) allMoves.random().item;
			} else {
				m = (Move) aNode.item;
			}
		} else {
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
				m = bestMove(gameboard, searchDepth, Board.LOWESTVAL,
						Board.HIGHESTVAL, color).move;
			}
		}

		DList pieces = gameboard.pieces();
		DListNode aNode = pieces.front();
		// System.out.print(pieces+ "\n");
		// for(int i=0; i<pieces.length(); i++)
		// {
		// System.out.println(aNode.item+" sees "+((Chip)
		// aNode.item).inSightString());
		// aNode=pieces.next(aNode);
		// }
		//System.out.print("Going to make move:" + m);

		gameboard.makeMove(color, m);

		return m;
	}

	// If the Move m is legal, records the move as a move by the opponent
	// (updates the internal game board) and returns true. If the move is
	// illegal, returns false without modifying the internal state of "this"
	// player. This method allows your opponents to inform you of their moves.
	public boolean opponentMove(Move m) {

		boolean isvalid = gameboard.makeMove(otherPlayer(color), m);

		if (!isvalid) {
			int pi = 1 / 0; // TODO remove this intentionally faulty code.
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

	private Best bestMove(Board board, int searchDepth, int alpha, int beta,
			int color) {
		Best myBest = new Best(0); // My best move
		Best reply; // Opponent's best reply
		if (searchDepth == 0 || board.isFinished(color)) {
			//System.out.print("\nFINISHED" + searchDepth + " "
			//		+ board.isFinished(color) + "\n");
			return new Best(board.value(this.color));
		}

		if (color == this.color) {
			myBest.score = alpha;
		} else {
			myBest.score = beta;
		}

		DList allMoves = board.validMoves(color);
		DListNode aNode = allMoves.front();
		for (int i = 0; i < allMoves.length(); i++) {
			board.makeMove(color, (Move) aNode.item);
			reply = bestMove(board, searchDepth - 1, alpha, beta,
					otherPlayer(color));
			board.undo((Move) aNode.item);
			if ((color == this.color) && (reply.score >= myBest.score)) {
				myBest.move = (Move) aNode.item;
				myBest.score = reply.score;
				alpha = reply.score;
			} else if ((color == otherPlayer(color))
					&& (reply.score <= myBest.score)) {
				myBest.move = (Move) aNode.item;
				myBest.score = reply.score;
				beta = reply.score;
			}
			if (alpha >= beta) {
				return myBest;
			}

			aNode = allMoves.next(aNode);
		}
		return myBest;

		/*
		 * Best myBest;
		 * 
		 * if (searchDepth == 0) //|| board.isFinished(color) return new
		 * Best(board.value(this.color)); }
		 * 
		 * if (color == this.color) { myBest = new Best(Board.LOWESTVAL); } else
		 * { myBest = new Best(Board.HIGHESTVAL); }
		 * 
		 * DList allMoves = board.validMoves(color);
		 * 
		 * System.out.print("\n "+color+" thinks these are valid\n"); DListNode
		 * bNode = allMoves.front(); for (int i = 0; i < allMoves.length(); i++)
		 * { System.out.println(bNode.item); bNode = allMoves.next(bNode); }
		 * 
		 * 
		 * DListNode aNode = allMoves.front();
		 * 
		 * for (int i = 0; i < allMoves.length(); i++) { Best reply =
		 * bestMove(new Board(board, color, (Move) aNode.item), searchDepth - 1,
		 * otherPlayer(color)); System.out.print("out one level"); if (((color
		 * == this.color) && (reply.score >= myBest.score)) || ((color !=
		 * this.color) && (reply.score <= myBest.score))) { myBest.move = (Move)
		 * aNode.item; myBest.score = reply.score; aNode=allMoves.next(aNode); }
		 * } return myBest;
		 */
	}

	public static int otherPlayer(int color) {
		if (color == WHITE) {
			return BLACK;
		} else {
			return WHITE;
		}
	}

	public static void main(String[] args) {
		System.out.println("\nTesting ###CLASS### DList");
		System.out.println("Testing equals");
		DList list1 = new DList();
		DList list2 = new DList();
		list1.insertFront("one");
		list1.insertFront("two");
		list2.insertFront("one");
		list2.insertFront("two");
		assert list1.equals(list2) : "list1.equals(list2) failed";
		list2.insertFront("three");
		assert !list1.equals(list2) : "!list1.equals(list2) failed";
		list2.remove(list2.front());
		list2.remove(list2.front());
		list2.insertBack("two");
		assert list1.equals(list2) : "list1.equals(list2) failed";
		list2.insertBack("three");
		assert !list1.equals(list2) : "!list1.equals(list2) failed";
		list1 = new DList();
		list1.insertFront(1);
		list1.insertFront(2);
		list1.insertFront(3);
		list2 = list1.copy();
		assert list1.equals(list2) : "list1.equals(list2) failed";
		/*
		 * assert list1.similar(list2) : "list1.similar(list2) failed"; list2 =
		 * new DList(); list2.insertFront(1); assert !list1.similar(list2) :
		 * "!list1.similar(list2) failed"; list2.insertFront(2); assert
		 * list1.similar(list2) : "list1.similar(list2) failed";
		 */
		list1 = new DList();
		assert !list1.hasRepeats() : "!list1.hasRepeats() failed";
		list1.insertFront(1);
		list1.insertFront(1);
		assert list1.hasRepeats() : "list1.hasRepeats() failed";
		list1.remove(list1.front());
		assert !list1.hasRepeats() : "!list1.hasRepeats() failed";
		list1.insertBack(2);
		list1.insertBack(3);
		list1.insertBack(2);
		assert list1.hasRepeats() : "list1.hasRepeats() failed";
		list1.remove(list1.back());
		assert !list1.hasRepeats() : "!list1.hasRepeats() failed";
		System.out.println("\nTesting ###CLASS### Chip");
		Chip chip = new Chip();
		// chip.tester();
		System.out.println("Testing equals");
		Chip chip1 = new Chip(1, 2, 1);
		Chip chip2 = new Chip(1, 2, 1);
		Chip chip3 = new Chip();
		assert chip1.equals(chip2) : "chip1.equals(chip2) failed";
		assert !chip1.equals(chip3) : "!chip1.equals(chip3) failed";
		System.out.println("\nTesting ###CLASS### Board");
		Board board = new Board();
		// board.tester();
		System.out.println("\nTesting ###CLASS### MachinePlayer");
	}
}
