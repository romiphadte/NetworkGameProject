/* Board.java */

package player;

import list.*;

class Board {

	public static final int WHITE = 1;
	public static final int BLACK = 0;
	public static final int LOWESTVAL = -100;
	public static final int HIGHESTVAL = 100;
	private Chip[][] gameboard;
	private int numPieces;

	/**
	 * makes a blank board
	 */
	public Board() {
		gameboard = new Chip[8][8]; // ROMI changed 8 to 7.

	}

	/**
	 * makes hypothetical variations to a certain Board
	 */
	public Board(Board b, int color, Move m) { // TODO is color useful?
		this();
		for (int x = 0; x < gameboard.length; x++) {
			for (int y = 0; y < gameboard[0].length; y++) {
				gameboard[x][y] = b.gameboard[x][y];
			}
		}
		makeMove(color, m);

	}

	public int numPieces() {
		return numPieces;
	}

	public DList pieces() {
		DList pieces = new DList();
		for (int x = 0; x < gameboard.length; x++) {
			for (int y = 0; y < gameboard[0].length; y++) {
				if (gameboard[x][y] != null) {
					pieces.insertBack(gameboard[x][y]);
				}
			}
		}
		return pieces;
	}

	/**
	 * if moveKind is ADD return addChip if moveKind is Step return moveChip
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
	 * checks if valid returns false if it isn't adds chip to board return true
	 */
	private boolean addChip(int color, Move m) {
		// checks if valid
		if (isValid(color, m)) {
			Chip c = new Chip(m.x1, m.y1, color);
			gameboard[m.x1][m.y1] = c;
			Chip[] chips = lineOfSight(c);
			// for each chip in the new chip's line of sight
			// recaluate the seen chip's sight
			for (int i = 0; i < chips.length; i++) {
				chips[i].clear();
				Chip[] tmp = lineOfSight(chips[i]);
				for (int j = 0; j < tmp.length; j++) {
					chips[i].addC(tmp[j]);
				}
			}
			numPieces++;
			return true;
		}
		// returns false if not valid
		return false;
	}

	/**
	 * returns all valid moves for given color and move
	 */
	public DList validMoves(int color) {

		DList allmoves = new DList();
		for (int i = 0; i < gameboard.length; i++) {
			for (int j = 0; j < gameboard[0].length; j++) {

				if (numPieces >= 20) { // try moving if more than 20 pieces on
										// board
					if (gameboard[i][j] != null) { // and contains and also
													// contains piece

						for (int a = 0; a < gameboard.length; a++) {
							for (int b = 0; b < gameboard[0].length; b++) {
								// to every other possible space
								Move amove = new Move(a, b, i, j);
								if (isValid(color, amove)) {
									allmoves.insertBack(amove);
								} // TODO number of indentations is scary. can
									// this change?
							}
						}
					}
				} else {
					Move trymove = new Move(i, j); // else try adding
					if (isValid(color, trymove)) {
						allmoves.insertBack(trymove);
					}

				}
			}
		}
		return allmoves;
	}

	/**
	 * store and remove old chip from board checks if valid (must check over
	 * here so isValid doesn't count in the old chip) returns false if it isn't
	 * valid and readd the chip add a new chip at location x,y return true
	 */
	private boolean moveChip(int color, Move m) {
		// store and remove old chip from board
		Chip c = new Chip(m.x2, m.y2, color);
		// checks if valid
		if (isValid(color, m)) {
			removeChip(gameboard[m.x2][m.y2]);

			// add a new chip at location x,y
			addChip(color, new Move(m.x1, m.y1));
			// return true
			return true;

		}
		return false;
	}

	private void removeChip(Chip c) {
		gameboard[c.getX()][c.getY()] = null;
		numPieces--;
		/*
		 * for (int i = 0; i < chips.length; i++) { chips[i].clear(); Chip[] tmp
		 * = lineOfSight(chips[i]); for (int j = 0; j < tmp.length; j++) {
		 * chips[i].addC(tmp[j]); } }
		 */
	}

	/**
	 * returns a score from -100 to 100 100 is a win for self
	 */
	public int value(int color) {
		return 0;
	}

	/**
	 * returns a bool to tell you if match is finished. Needed for min max. Use
	 * in value code
	 */
	public boolean isFinished() {
		return false;
	}

	/**
	 * returns true if Move for given color is valid
	 * 
	 * chip not in four corners black pieces are not in 0-1 to 0-6 or 7-1 to 7-6
	 * (inclusive) white pieces are not in 1-0 to 6-0 or 1-7 to 6-7 (inclusive)
	 * no chip may be placed in a occupied square a chip may not be a cluster(2+
	 * adjacent chips)
	 */
	private boolean isValid(int color, Move m) {
		System.out.print("\nChecking ");
		if (color == 1)
			System.out.print("white moves:");
		else
			System.out.print("black moves:");
		System.out.print(m);
		// if move is QUIT
		// return false
		if (m.moveKind == Move.QUIT) {
			return false;
			// if square is occupied
			// return false
		} else if (m.moveKind == Move.ADD && numPieces() >= 20) {
			return false;
		} else if (m.moveKind == Move.STEP && numPieces() < 20) {
			return false;
		} else if (gameboard[m.x1][m.y1] != null) {
			return false;
			// if in 0-0, 0-7, 7-0, 7-7
			// return false
		} else if ((m.x1 == 0 || m.x1 == 7) && (m.y1 == 0 || m.y1 == 7)) {
			return false;
			// else if black and in 0-1 to 0-6 or in 7-1 to 7-6
			// return false
		} else if (color == BLACK && (m.x1 == 0 || m.x1 == 7)
				&& (m.y1 >= 1 && m.y1 <= 6)) {
			return false;
			// else if white and in 1-0 to 6-0 or in 1-7 to 6-7
			// return false
		} else if (color == WHITE && (m.x1 >= 1 && m.x1 <= 6)
				&& (m.y1 == 0 || m.y1 == 7)) {
			return false;
			// else if is a cluster(2+ adjacent chips)
			// return false
		} else if (isCluster(new Chip(m.x1, m.y1, color), new Chip(m.x2, m.y2,
				color), 0)) {
			return false;
			// Romi's addition
			// else if moving nonexistant chips return false
		} else if (m.moveKind == Move.STEP && gameboard[m.x2][m.y2] == null) {
			return false;
			// else if moving a different color than self
			// return false
		} else if (m.moveKind == Move.STEP
				&& gameboard[m.x2][m.y2].color() != color) {
			return false;
			// else
			// return true
		}
		System.out.println("is valid");
		return true;
	}

	/**
	 * checks all neighboring spaces on the gameboard around chip c do not count
	 * the space to be occupied by chip c returns true if the chip has more than
	 * 1 neighbor check if a neighbor has more than 1 neighbor returns false
	 * otherwise
	 */
	private boolean isCluster(Chip c, Chip o, int n) { // ignores o chip in the
														// case of a move.
		for (int x = c.getX() - 1; x <= c.getX() + 1; x++) {
			for (int y = c.getY() - 1; y <= c.getY() + 1; y++) {
				if (x >= 0 && x <= 7 && y >= 0 && y <= 7
						&& !(x == c.getX() && y == c.getY())
						&& !(x == o.getX() && y == o.getY())) {
					if (gameboard[x][y] != null
							&& gameboard[x][y].color() == c.color()) {
						n++;
						if (n > 1) {
							return true;
						}

						if (isCluster(new Chip(x, y, gameboard[x][y].color()),
								o, n)) {
							return true;
						}

					}
				}
			}
		}
		return false;
	}

	/**
	 * finds and returns an array of chips that the chip c has direct line of
	 * sight to
	 */
	private Chip[] lineOfSight(Chip c) {
		int count = 0;
		Chip[] inLine = new Chip[8];
		Chip[] result;
		// searches in 8 directions
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (!(i == 0 && j == 0)) {
					Chip tmp = search(i, j, c);
					if (tmp != null) {
						// adds the first chip to an array
						inLine[count] = tmp;
						count++;
					}
				}
			}
		}
		// copies array into an array with length equal to the number of chips
		// in line of sight
		result = new Chip[count];
		for (int i = 0; i < count; i++) {
			result[i] = inLine[i];
		}
		return result;
	}

	/**
	 * checks gameboard at coordinates x and y if its a chip, return chip else
	 * return null
	 */
	private Chip search(int dx, int dy, Chip c) {
		int x = c.getX() + dx;
		int y = c.getY() + dy;
		while (x >= 0 && x < gameboard.length && y >= 0
				&& y < gameboard[0].length) {
			if (gameboard[x][y] != null) {
				return gameboard[x][y];
			}
			x += dx;
			y += dy;
		}
		return null;
	}

	/**
	 * A DList of networks(represented by DLists) Can only go from similarly
	 * colored chips of direct line of sight Cannot pass through the same chip
	 * twice cannot have more than 1 chip in each goal *minimum of 6 to form
	 * network(checked elsewhere) cannot pass through chip without changing
	 * direction
	 */
	public DList findNetworks(int color) {
		// Dlist of networks(DLists)
		DList networks = new DList();
		Chip chip;
		for (int x = 0; x < gameboard.length; x++) {
			for (int y = 0; y < gameboard[x].length; y++) {
				chip = gameboard[x][y];
				if (chip != null && chip.color() == color) {
					DList net = chip.network();
					DList validNet = validNetworks(net,color);
					mergeNetworks(networks, net);
				}
			}
		}
		return networks;
	}

	/**
	 * returns all valid networks in DList list *minus the 6 to end game network
	 * rule
	 */
	private DList validNetworks(DList list, int color) {
		DList valid = new DList();
		DList network;
		DListNode curr = list.front();
		// for each element in list
		while (curr != null) {
			network = (DList) curr.item;
			// cannot have more than 1 chip in each goal
			// Cannot pass through the same chip twice
			// cannot pass through chip without changing direction
			if (checkGoals(network, color) && !repeats(network)
					&& !aligned(network)) {
				valid.insertBack(network);
			}
			curr = list.next(curr);
		}
		return valid;
	}

	/**
	 * used in validNetworks
	 */
	private boolean checkGoals(DList list, int color) {
		int goal1 = 0;
		int goal2 = 0;
		DListNode curr = list.front();
		while (curr != null) {
			if (color == MachinePlayer.WHITE) {
				if (((Chip) curr.item).getX() == 0) {
					goal1++;
				} else if (((Chip) curr.item).getX() == 7) {
					goal2++;
				}
			} else {
				if (((Chip) curr.item).getY() == 0) {
					goal1++;
				} else if (((Chip) curr.item).getY() == 7) {
					goal2++;
				}
			}
			curr = list.next(curr);
		}
		return goal1 <= 1 && goal2 <= 1;
	}

	/**
	 * used in validNetworks
	 */
	private boolean repeats(DList list) {
		DListNode curr = list.front();
		DListNode node = list.front();
		while (curr != null) {
			while (node != null) {
				if (curr.item.equals(node.item)) {
					return true;
				}
				node = list.next(node);
			}
			curr = list.next(curr);
		}
		return false;
	}

	/**
	 * used in validNetworks
	 */
	private boolean aligned(DList list) {
		/*DListNode curr = list.front();
		int x = -1;
		int y = -1;
		int linex = 0;
		int liney = 0;
		while (curr != null) {
			if (linex >= 3 || liney >= 3) {
				return true;
			}
			if (x != ((Chip) curr.item).getX()) {
				x = ((Chip) curr.item).getX();
				linex = 1;
			} else {
				linex++;
			}
			if (y != ((Chip) curr.item).getY()) {
				y = ((Chip) curr.item).getY();
				liney = 1;
			} else {
				liney++;
			}
			curr = list.next(curr);
		}
		return false;
        */

        //this way account for DIAGONALS as well
        DListNode curr = list.front();
		int x = -1;
		int y = -1;
		int diffx = -1;
		int diffy = -1;
        int vercount = 1;
        int horcount = 1;
        int diacount = 1;
		while (curr != null) {
            int tx = ((Chip) curr.item).getX();
            int ty = ((Chip) curr.item).getY();
			if (tx - x == diffx && ty - y == diffy) {
                diacount++;
			} else {
                diacount = 1;
			}
			if (tx - x == diffx) {
                vercount++;
			} else {
                vercount = 1;
			}
            if (ty - y == diffy) {
                horcount++;
            } else {
                horcount = 1;
            }
			if (vercount >= 2 || horcount >= 2 || diacount >= 2) {
				return true;
			}
            diffx = tx - x;
            diffy = ty - y;
            x = tx;
            y = ty;
			curr = list.next(curr);
		}
		return false;
	}

	/**
	 * adds all elements of list2 that are not in list1 to list1
	 */
	private void mergeNetworks(DList list1, DList list2) {
		DListNode n1 = list1.front();
		DListNode n2 = list2.front();
		boolean in = false;
		while (n2 != null) {
			while (n1 != null && !in) {
				if (n1.equals(n2)) {
					in = true;
				}
				n1 = list1.next(n1);
			}
			if (!in) {
				list1.insertBack(n2);
				in = false;
			}
			n2 = list2.next(n2);
		}
	}

	/**
	 * /** tester method to test private methods
	 */
	public void tester() {
        Board board = new Board();
        Move m1 = new Move(1, 1);
        Move m2 = new Move(1, 3);
        printboard(board);
        System.out.println("adding m1");
        board.addChip(WHITE, m1);
        printboard(board);
        System.out.println("adding m2");
        board.addChip(WHITE, m2);
        printboard(board);

        Chip c1 = board.gameboard[1][1];
        Chip c2 = board.gameboard[1][3];
        System.out.println("printing c1");
        c1.visualChip(c1);
        System.out.println("printing c2");
        c2.visualChip(c2);
	}
    public void printboard(Board board) {
        System.out.println(" 01234567");
		for (int x = 0; x < board.gameboard.length; x++) {
            System.out.print(x);
			for (int y = 0; y < board.gameboard[0].length; y++) {
				if (board.gameboard[x][y] == null) {
                    System.out.print("_");
                } else if (board.gameboard[x][y].color() == WHITE) {
                    System.out.print(WHITE);
                } else {
                    System.out.print(BLACK);
                }
			}
            System.out.println();
        }
    }
}
