/* Board.java */

package player;

class Board {

  private int[][] gameboard;

  /**
   * makes a blank board
   */
  public Board() {
    gameboard = new int[8][8];
  }

  /**
   * makes hypothetical variations to a certain Board
   */
  public Board(Board b, Move m, int player) {
  }

  /**
   * changes the gameboard
   */
  public void makeMove(Move m, int player) {
  }
 
  /**
   * returns a score from -100 to 100
   * 100 is a win for self
   */
  public int value(int player) {
    return 0;
  }

  /**
   * win: 1  neither: 0  loss: -1
   */
  private int winner() {
    return 0;
  }

  /**
   * returns all valid moves for given player
   */
  public Move[] validMoves(int player) {
    return new Move[0];
  }

  /**
   * returns true if Move for given player is valid
   */
  public boolean isValid(int player) {
    return true;
  }
}
