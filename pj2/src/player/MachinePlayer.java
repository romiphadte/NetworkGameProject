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
	public static final int SEARCHDEPTH = 3;
	protected Board gameboard;
	protected int color;
	protected int searchDepth;

	/**
     * Creates a machine player with the given color. Color is either 0 (black)
     * or 1 (white). (White has the first move.)
     * 
     * @param color
     * this machine's personal color
     */
	public MachinePlayer(int color) {
		this.color = color;
		gameboard = new Board();
		this.searchDepth = SEARCHDEPTH;
	}

	/**
     * Creates a machine player with the given color and search depth. Color is
     * either 0 (black) or 1 (white). (White has the first move.)
     *
     * @param color
     * this machine's personal color
     * @param searchDepth
     * how many recursive level down to go
     */
	public MachinePlayer(int color, int searchDepth) {
		this(color);
		this.searchDepth = searchDepth;
	}

	/**
     * Returns a new move by "this" player. Internally records the move (updates
     * the internal game board) as a move by "this" player.
     */
	public Move chooseMove() {
		Move m;
		Best b = new Best(9999999);
        if (gameboard.numPieces() <= 1)

        {
            if (color == BLACK) {
                m = new Move(3, 0);
            } else {
                m = new Move(0, 3);
            }
        } else if (gameboard.numPieces() <= 3) {
            if (color == BLACK) {
                m = new Move(4, 7);
            } else {
                m = new Move(7, 4);
            }
        } else {

            b = bestMove(gameboard, searchDepth, Board.LOWESTVAL,
                    Board.HIGHESTVAL, color);
            m = b.move;
        }

		DList pieces = gameboard.pieces();
		DListNode aNode = pieces.front();
		gameboard.makeMove(color, m);

		return m;
	}

	/**
     * If the Move m is legal, records the move as a move by the opponent
     * (updates the internal game board) and returns true. If the move is
     * illegal, returns false without modifying the internal state of "this"
     * player. This method allows your opponents to inform you of their moves.
     *
     * @param m
     * the move that the opponent will make
     */
	public boolean opponentMove(Move m) {
		return gameboard.makeMove(otherPlayer(color), m);
	}

	/**
     * If the Move m is legal, records the move as a move by "this" player
     * (updates the internal game board) and returns true. If the move is
     * illegal, returns false without modifying the internal state of "this"
     * player. This method is used to help set up "Network problems" for your
     * player to solve.
     *
     * @param m
     * the move that "this" player will make
     */
	public boolean forceMove(Move m) {
		return gameboard.makeMove(color, m);
	}
	
	/**
	 * recursively calls itself for the min max algorithm.
     * Chooses the best move using alpha beta pruning.
     *
     * @param board
     * the board the game is running on
     * @param searchDepth
     * the deepest recursion level to go down to
     * @param alpha
     * the greatest value
     * @param beta
     * the least value
     * @param color
     * the color of "this" player
	 */
    private Best bestMove(Board board, int searchDepth, double alpha,
			double beta, int color) {
		Best myBest = new Best(0); // My best move
		Best reply; // Opponent's best reply
		DList allNetworks=board.findNetworks(color);
		DList opnetworks=board.findNetworks(otherPlayer(color));
        DListNode curr = opnetworks.front();
        for (int i = 0; i < opnetworks.length(); i++) {
            allNetworks.insertBack(curr.item);
            curr = opnetworks.next(curr);
        }
		if (searchDepth == 0 || board.isFinished(allNetworks,color) || board.isFinished(allNetworks,otherPlayer(color))) {
			return new Best(board.value(allNetworks,this.color));
		}

		if (color == this.color) {
			myBest.score = -99999999;
		} else {
			myBest.score = 99999999;
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
				alpha = Math.max(reply.score, alpha);
			} else if ((color == otherPlayer(this.color))
					&& (reply.score <= myBest.score)) {
				myBest.move = (Move) aNode.item;
				myBest.score = reply.score;
				beta = Math.min(reply.score, beta);
			}
			if (alpha >= beta) {
				return myBest;
			}

			aNode = allMoves.next(aNode);
		}
		return myBest;

	}


	/**
	 * returns black if given white and white if given black.
     *
     * @param color
     * the color of the player, that need to be switched
	 */
	public static int otherPlayer(int color) {
		if (color == WHITE) {
			return BLACK;
		} else {
			return WHITE;
		}
	}
}
