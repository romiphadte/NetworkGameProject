/* Board.java */

package player;

class Board {

    private Chip[][] gameboard;
    private int numPieces;

    /**
    * makes a blank board
    */
    public Board() {
    //gameboard = new Chip[8][8];
    }

    /**
    * makes hypothetical variations to a certain Board
    */
    public Board(Board b, int color, Move m) {
    //for (int i = 0; i <
    }

    /**
    * if moveKind is ADD
    * return addChip
    * if moveKind is Step
    * return moveChip
    */
    public boolean makeMove(int color, Move m) {
        if (m.moveKind == Move.ADD) {
            return addChip(color, m);
        } else if (m.moveKind == Move.STEP) {
            return moveChip(color, m);
        }
        return false;
    }

    /**
    * checks if valid
    * returns false if it isn't
    * adds chip to board
    * return true
    */
    private boolean addChip(int color, Move m) {
        //checks if valid
        if (isValid(color, m)) {
            Chip c = new Chip(m.x1, m.y1, color);
            gameboard[m.x1][m.y1] = c;
            Chip[] chips = lineOfSight(c);
            for (int i = 0; i < chips.length; i++) {
                c.addC(chips[i]);
            }
            return true;
        }
        //returns false if not valid
        return false;
    }

    /**
    * store and remove old chip from board
    * checks if valid (must check over here so isValid doesn't count in the old chip)
    * returns false if it isn't valid and readd the chip
    * add a new chip at location x,y
    * return true
    */
    private boolean moveChip(int color, Move m) {
        //store and remove old chip from board
        Chip c = new Chip(m.x2, m.y2, color);
        gameboard[m.x2][m.y2].remove();
        //checks if valid (must check over here so isValid doesn't count in the old chip)
        if (isValid(color, m)) {
            //add a new chip at location x,y
            addChip(color, m);
            //return true
            return true;
        }
        //readd the chip if it isn't valid and return false
        addChip(color, new Move(m.x2, m.y2));
        return false;
    }

    /**
    * returns a score from -100 to 100
    * 100 is a win for self
    */
    public int value(int color) {
        return 0;
    }

    /**
    * returns all valid moves for given color and move
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
    * no chip may be placed in a occupied square
    * a chip may not be a cluster(2+ adjacent chips)
    */
    private boolean isValid(int color, Move m) {
        //if move is QUIT
        //  return false
        if (m.moveKind == Move.QUIT) {
            return false;
        //if square is occuped
        //  return false
        } else if (gameboard[m.x1][m.y1] != null) {
            return false;
        //if in 0-0, 0-7, 7-0, 7-7
        //  return false
        } else if ((m.x1 == 0 || m.x1 == 7) && (m.y1 == 0 || m.y1 == 7)) {
            return false;
        //else if black and in 0-1 to 0-6 or in 7-1 to 7-6
        //  return false
        } else if (color == 0) {
            if ((m.x1 == 0 || m.x1 == 7) && (m.y1 >= 1 && m.y1 <= 6)) {
                return false;
            }
        //else if white and in 1-0 to 6-0 or in 1-7 to 6-7
        //  return false
        } else if (color == 1) {
            if ((m.x1 >= 1 && m.x1 <= 6) && (m.y1 == 0 || m.y1 == 7)) {
                return false;
            }
        //else if is a cluster(2+ adjacent chips)
        //  return false
        } else if (isCluster(new Chip(m.x1, m.y1, color), 0)) {
            return false;
        //else 
        //  return true
        } 
        return true;
    }

    /**
    * checks all neighboring spaces on the gameboard around chip c
    * do not count the space to be occupied by chip c
    * returns true if the chip has more than 1 neighbor
    * check if a neighbor has more than 1 neighbor
    * returns false otherwise
    */
    private boolean isCluster(Chip c, int n) {
        for (int x = c.getX() - 1; x <= c.getX() + 1; x++) {
            for (int y = c.getY() - 1; y <= c.getY() + 1; y++) {
                if (x >= 0 && x <= 7 && y >= 0 && y <=7 && !(x == c.getX() && y == c.getY())) {
                    if (gameboard[x][y] != null && gameboard[x][y].color() == c.color()) {
                        n++;
                        if (n > 1) {
                            return true;
                        }
                        return isCluster(new Chip(x, y, gameboard[x][y].color()), n);
                    }
                }
            }
        }
        return false;
    }

    /**
    * finds and returns an array of chips that the
    * chip c has direct line of sight to
    */
    private Chip[] lineOfSight(Chip c) {
        int count = 0;
        Chip[] inLine = new Chip[8]; 
        Chip[] result;
        //searches in 8 directions
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i != 0 && j != 0) {
                    Chip tmp = search(i, j, c);
                    if (tmp != null) {
                        //adds the first chip to an array
                        inLine[count] = tmp;
                        count++;
                    }
                }
            }
        }
        //copies array into an array with length equal to the number of chips in line of sight
        result = new Chip[count];
        for (int i = 0; i < count; i++) {
            result[i] = inLine[i];
        }
        return result;
    }
    /**
    * checks gameboard at coordinates x and y
    * if its a chip, return chip
    * else return null
    */
    private Chip search(int dx, int dy, Chip c) {
        int x = c.getX() + dx;
        int y = c.getY() + dy;
        while (x >= 0 && x <= 7 && y >= 0 && y <= 7 && gameboard[x][y] == null) {
            if (gameboard[x][y] != null) {
                return gameboard[x][y];
            }
            x += dx;
            y += dy;
        }
        return null;
    }
    /**
    * tester method to test private methods
    */
    public void tester(Board b) {

    }
    public static void main(String[] args) {
        Board board = new Board();
    }
}
