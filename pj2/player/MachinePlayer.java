/* MachinePlayer.java */

package player;

/**
*  An implementation of an automatic Network player.  Keeps track of moves
*  made by both players.  Can select a move for itself.
*/
public class MachinePlayer extends Player {

    public static final int WHITE = 1;
    public static final int BLACK = 0;
    protected Board gameboard;
    protected int color;
    protected int searchDepth;

    // Creates a machine player with the given color.  Color is either 0 (black)
    // or 1 (white).  (White has the first move.)
    public MachinePlayer(int color) {
        this.color = color;
        gameboard = new Board();

    }

    // Creates a machine player with the given color and search depth.  Color is
    // either 0 (black) or 1 (white).  (White has the first move.)
    public MachinePlayer(int color, int searchDepth) {
        this(color);
        this.searchDepth = searchDepth;
    }

    // Returns a new move by "this" player.  Internally records the move (updates
    // the internal game board) as a move by "this" player.
    public Move chooseMove() {
        return new Move();
    } 

    // If the Move m is legal, records the move as a move by the opponent
    // (updates the internal game board) and returns true.  If the move is
    // illegal, returns false without modifying the internal state of "this"
    // player.  This method allows your opponents to inform you of their moves.
    public boolean opponentMove(Move m) {
        int opposingColor;
        if (color == WHITE) {
            opposingColor = BLACK;
        } else {
            opposingColor = WHITE;
        }
        return gameboard.makeMove(opposingColor, m);
    }

    // If the Move m is legal, records the move as a move by "this" player
    // (updates the internal game board) and returns true.  If the move is
    // illegal, returns false without modifying the internal state of "this"
    // player.  This method is used to help set up "Network problems" for your
    // player to solve.
    public boolean forceMove(Move m) {
        return gameboard.makeMove(color, m);
    }

    private Move bestMove(Board board, int searchDepth) {
        return new Move();
    }

}
