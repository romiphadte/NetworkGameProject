/* Board.java */

package player;

class Board {

  //private Chip[][] gameboard;
  //private int color;

  /**
   * makes a blank board
   */
  public Board() {
    //gameboard = new Chip[8][8];
  }

  public Board(int color) {
    //gameboard = new Chip[8][8];
    //this.color = color;
  }

  /**
   * makes hypothetical variations to a certain Board
   */
  public Board(Board b, int color, Move m) {
    //for (int i = 0; i <
  }

  /**
   * changes the gameboard
   */
  public void makeMove(int color, Move m) {
  }
 
  /**
   * returns a score from -100 to 100
   * 100 is a win for self
   */
  public int value(int color) {
    return 0;
  }

  /**
   * win: 1  neither: 0  loss: -1
   */
  private int winner() {
    return 0;
  }

  /**
   * returns all valid moves for given color
   */
  public Move[] validMoves(int color) {
    return new Move[0];
  }

  /**
   * returns true if Move for given color is valid
   *
   * chip not in four corners
   * black pieces are not in 0-1 to 0-6 or 7-1 to 7-6 (inclusive)
   * white pieces are not in 1-0 to 6-0 or 1-7 to 6-7 (inclusive)
   * (checked elsewhere)>>no chip may be placed in a occupied square
   * a chip may not have more than 2 connections
   */
  public boolean isValid(int color) {
    //if in 0-0, 0-7, 7-0, 7-7
    //  return false
    //else if black and in 0-1 to 0-6 or in 7-1 to 7-6
    //  return false
    //else if white and in 1-0 to 6-0 or in 1-7 to 6-7
    //  return false
    //else if chip.connections() > 2
    //  return false
    //else 
    //  return true
    return true;
  }
}