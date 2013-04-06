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
	private DList networks;

	/**
	 * Board() constructor for an empty board.
	 */
	public Board() {
		gameboard = new Chip[8][8];

	}

	/**
	 * numPieces() returns the number of game pieces/chips on the board
	 * 
	 * @return the number of pieces on the board.
	 */

	public int numPieces() {
		return numPieces;
	}

	/**
	 * pieces() returns a DList containing all chips on the board (Chips hold positions within themselves).
	 * 
	 * @return a DList of all chips on the board.
	 */
	DList pieces() {
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
	 * makeMove() takes in a color and a move, determines the move type, then executes the corresponding method (addChip() or moveChip()).
	 * 
	 * @param color
	 * 		The side who is executing a move
	 * @param m
	 * 		The move that is being executed
	 * @return true if move successfully completes, false otherwise.
	 */
	boolean makeMove(int color, Move m) {
		if (m.moveKind == Move.ADD) {
			return addChip(color, m);
		} else if (m.moveKind == Move.STEP) {
			return moveChip(color, m);
		}
		return false;
	}

	/**
	 * addChip() executes an add move "m" for the side "color". Each chip in the new chip's line 
	 * of sight has its visibility of other chips recalculated.
	 * 
	 * @param color
	 * 		The side whose turn it is
	 * @param m
	 * 		The move that is being executed
	 * @return true if the move is valid, false otherwise
	 */
	private boolean addChip(int color, Move m) {
		// checks if valid
		if (isValid(color, m)) {
			Chip c = new Chip(m.x1, m.y1, color);
			gameboard[m.x1][m.y1] = c;
			Chip[] chips = lineOfSight(c);
			// for each chip in the new chip's line of sight
			// recalculate the seen chip's sight
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
	 * validMoves() returns all moves available to a given color
	 * 
	 * @param color
	 * 		The side whose valid moves are being calculated
	 * @return a DList of all valid moves available to a color
	 */
	DList validMoves(int color) {

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
								} 
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
	 * moveChip() checks if a step move "m" is valid. If it is, the chip is moved. If not, it is left alone.
	 * 
	 * @param color
	 * 		The side whose chip is being moved
	 * @param m 
	 * 		The step move being executed
	 * @return true if the move is valid, false otherwise
	 */
	private boolean moveChip(int color, Move m) {
		// checks if valid
		if (isValid(color, m)) {
			removeChip(gameboard[m.x2][m.y2]);
			// add a new chip at location x,y
			addChip(color, new Move(m.x1, m.y1));
			return true;
		}
		return false;
	}

	/**
	 * removeChip() removes a chip from the gameboard. Each chip in its line of sight has its visible chips recalculated.
	 * 
	 * @param c
	 * 		The chip that is being removed
	 */
	private void removeChip(Chip c) {
		Chip[] chips = lineOfSight(c);
		gameboard[c.getX()][c.getY()] = null;
		numPieces--;
		for (int i = 0; i < chips.length; i++) {
			chips[i].clear();
			Chip[] tmp = lineOfSight(chips[i]);
			for (int j = 0; j < tmp.length; j++) {
				chips[i].addC(tmp[j]);
			}
		}
	}

	/**
	 * undo() does the necessary steps to undo any given move. This is used in the
	 * minmax algorithm after leaving a given searchDepth.
	 * 
	 * @param m
	 * 		The move that is being undone
	 */
	void undo(Move m) {
		if (m.moveKind == Move.ADD) {
			removeChip(gameboard[m.x1][m.y1]);
		} else if (m.moveKind == Move.STEP) {
			makeMove(gameboard[m.x1][m.y1].color(), new Move(m.x2, m.y2, m.x1,
					m.y1));
		}
	}

	/**
	 * value() calls the two parameter value() and returns a score from -100 to 100 for the board. 100 is a win for "color".
	 *
	 *@param color
	 *		The side who board values are being calculated for.
	 *@return a score from -100 to 100.
	 */
	double value(int color) {
		DList allNetworks = this.findNetworks(color);
		return value(allNetworks, color);
	}
	
	/**
	 * value() returns a score from -100 to 100 for the board. 100 is a win for "color".
	 * Avoids the expensive operation of findNetworks in the one parameter value() by passing that in as allNetworks.
	 * 
	 * @param allNetworks
	 * 		A DList of allNetworks a board has
	 * @param color
	 * 		The side who board values are being calculated for
	 * @return a score from -100 to 100.
	 */
	double value(DList allNetworks, int color) {
		if (isFinished(allNetworks, color)) {
			return 100;
		} else if (isFinished(allNetworks, MachinePlayer.otherPlayer(color))) {
			return -100;
		}
		DListNode aNode = allNetworks.front();
		double total = 0;
		for (int i = 0; i < allNetworks.length(); i++) {
		//	System.out.println(total);
			/*
			 * if (((Chip) ((DList) aNode.item).front().item).color()==color) {
			 * total+=((DList) aNode.item).length();
			 * 
			 * } else if (((Chip) ((DList)
			 * aNode.item).front().item).color()==MachinePlayer
			 * .otherPlayer(color)) { total-=((DList) aNode.item).length(); }
			 */
			DList aList = (DList) aNode.item;
/*			if (inEndGoal((Chip) aList.front().item, color) == 1
					|| inEndGoal((Chip) aList.back().item, color) == 2) {
				if (((Chip) ((DList) aNode.item).front().item).color() == color) {
					total += ((DList) aNode.item).length();
					//	System.out.println(((DList) aNode.item).length());

				} else if (((Chip) ((DList) aNode.item).front().item).color() == MachinePlayer
						.otherPlayer(color)) {
					total -= ((DList) aNode.item).length();
					//	System.out.println(-1*((DList) aNode.item).length());
				}
			}
			if (inEndGoal((Chip) aList.front().item, color) == 2
					|| inEndGoal((Chip) aList.back().item, color) == 1) {
				if (((Chip) ((DList) aNode.item).front().item).color() == color) {
					total += ((DList) aNode.item).length();
					//	System.out.println(((DList) aNode.item).length());

				} else if (((Chip) ((DList) aNode.item).front().item).color() == MachinePlayer
						.otherPlayer(color)) {
					total -= ((DList) aNode.item).length();
					//	System.out.println(-1*((DList) aNode.item).length());
				}
			}
*/
            if (inEndGoal((Chip) aList.front().item, color) == 1) {
                total += ((DList) aNode.item).length();
            }
            if (inEndGoal((Chip) aList.back().item, color) == 2) {
                total += ((DList) aNode.item).length();
			}
			if (inEndGoal((Chip) aList.front().item, MachinePlayer.otherPlayer(color)) == 2) {
                total -= (1.2)((DList) aNode.item).length();
            }
            if (inEndGoal((Chip) aList.back().item, MachinePlayer.otherPlayer(color)) == 1) {
                total -= (1.2)((DList) aNode.item).length();
			}
			aNode = allNetworks.next(aNode);
		}

		if (total < 0) {
			total = -1 * Math.sqrt(Math.abs(total));
		} else {
			total = Math.sqrt(total);
		}
        total = Math.sqrt(total);

		//System.out.println(total+"\n");

		if (total >= 100) {
			System.out.println("A");
			return 99;
		} else if (total <= -100) {
			System.out.println("B");
			return -99;
		}

		//System.out.println("\nVALUE IS" + total + "\n");
		return total;

	}

	/**
	 * isFinished() determines whether or not "color" has made a finished network
	 * 
	 * @param color
	 * 		The side who is being checked for a successful network
	 * @return true if a winning network has been made for "color", false otherwise
	 */
	boolean isFinished(int color) {
		networks = findNetworks(color);
		return isFinished(networks, color);
	}
	/**
	 * isFinished() determines whether or not "color has made a finished network.
	 * Avoids the expensive operation of findNetworks present in the one parameter isFinsihed() 
	 * by passing in the desired network in the function prototype. 
	 * 
	 * @param networks
	 * 		A DList of all networks on the board
	 * @param color
	 * 		The side who is being checked for a successful network
	 * @return true if a winning network has been made for "color", false otherwise
	 */
	boolean isFinished(DList networks, int color) {
		DListNode aNode = networks.front();
		for (int i = 0; i < networks.length(); i++) {
			DList aList = (DList) aNode.item;
			if (aList.length() >= 6
					&& inEndGoal((Chip) aList.front().item, color) == 1) {
				if (inEndGoal((Chip) aList.back().item, color) == 2) {
					DListNode aListNode = aList.front();
					for (int ii = 0; ii < aList.length(); ii++) {
						aListNode = aList.next(aListNode);
					}
					return true;
				}
			}
			if (aList.length() >= 6
					&& inEndGoal((Chip) aList.front().item, color) == 2) {
				if (inEndGoal((Chip) aList.back().item, color) == 1) {
					DListNode aListNode = aList.front();
					for (int ii = 0; ii < aList.length(); ii++) {
						aListNode = aList.next(aListNode);
					}
					return true;
				}
			}

			aNode = networks.next(aNode);
		}

		return false;
	}

	/**
	 * isValid() returns true if move "m" for the given side "color" is valid.
	 * 
	 * @param color
	 * 		The side for which a move is being made.
	 * @param m
	 * 		The move being considered
	 * @return true if the move is valid for the given side, false otherwise.
	 */
	private boolean isValid(int color, Move m) {
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
		return true;
	}

	/**
	 * isCluster() checks all neighboring spaces on the gameboard around chip "c",not counting
	 * the space to be occupied by chip "c". Returns true if the chip has more than
	 * 1 neighbor, and that neighbor has s more than 1 neighbor. Returns false
	 * otherwise.
	 * 
	 * @param c
	 * 		The chip you begin checking from.
	 * @param o
	 * 		Ignores this chip if it is a step move.
	 * @param n
	 * 		The recursive depth to which neighbors are being checked for
	 * @return true if the chip has more than 1 neighbor, false otherwise.
	 */
	private boolean isCluster(Chip c, Chip o, int n) { 												
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
	 * lineOfSight() finds and returns an array of chips that the chip "c" has direct line of
	 * sight to.
	 * 
	 * @param c
	 * 		The chip whose visible chips are being checked for
	 * @return an array of Chip objects that can be seen by chip "c" as determined by the game rules
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
	 * search() checks the gameboard at coordinates dx and dy relative to Chip "c". If it is a chip, it returns that chip.
	 * Otherwise returns null.
	 * 
	 * @param dx
	 * 		The change in x position from the x position of chip "c" that is being checked
	 * @param dy
	 * 		The change in y position from the y position of chip "c" that is being checked
	 * @param c
	 * 		The chip who serves as the origin from which the coordinates dx and dy are used
	 * @return the Chip at position dx, dy relative to "c" if a Chip exists there. Null otherwise.
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
	 * findNetworks() returns a DList of networks on the board for side "color", as determined by the game rules.
	 * 
	 * @param color
	 * 		The side for which networks are being looked for
	 * @return a DList of all networks (Not necessarily winning networks or 6 piece long networks).
	 */
	DList findNetworks(int color) {
		// Dlist of networks(DLists)
		DList networks = new DList();
		Chip chip;
		for (int x = 0; x < gameboard.length; x++) {
			for (int y = 0; y < gameboard[x].length; y++) {
				chip = gameboard[x][y];
				if (chip != null && chip.color() == color) {
					DList net = chip.network(color);
					DList validNet = validNetworks(net, color);
					mergeNetworks(networks, validNet);
				}
			}
		}
		// cut out all repetitions
		DList clean = new DList();
		DListNode node = networks.front();
		for (int i = 0; i < networks.length(); i++) {
			if (!clean.has(node.item)) {
				clean.insertFront(node.item);
			}
			node = networks.next(node);
		}
		return clean;
	}

	/**
	 * validNetworks() returns all valid networks in "list", as determined by the game rules.
	 * 
	 * @param list
	 * 		The DList of all networks for "color", valid or invalid.
	 * @param color
	 * 		The side whose networks are being passed in in "list"
	 * @return a DList of all valid networks in "list"
	 */
	private DList validNetworks(DList list, int color) {
		DList valid = new DList();
		DList network;
		DListNode curr = list.front();
		while (curr != null) {
			network = (DList) curr.item;
			if (checkGoals(network, color) && !aligned(network)) {
				valid.insertBack(network);
			}
			curr = list.next(curr);
		}
		return valid;
	}

	/**
	 * checkGoals() checks if there is a valid number of chips in each goal for a given side.
	 * 
	 * @param list
	 * 		A DList of chip positions
	 * @param color
	 * 		The side whose goals are being checked.
	 * @return true if no more than 1 chip in each goal, false otherwise
	 */
    private boolean checkGoals(DList list, int color) {
        int goal1 = 0;
        int goal2 = 0;
        DListNode curr = list.front();
        while (curr != null) {
            if (color == MachinePlayer.WHITE) {
                if (((Chip) curr.item).getX() == 0) {
                    goal1++;
                }
                if (((Chip) curr.item).getX() == 7) {
                    goal2++;
                }
            } else {
                if (((Chip) curr.item).getY() == 0) {
                    goal1++;
                }
                if (((Chip) curr.item).getY() == 7) {
                    goal2++;
                }
            }
            curr = list.next(curr);
        }
        return !((goal1 > 1) || (goal2 > 1));
    }

    /** 
     * inEndGoal() returns 1 if in end goal 1, 2 if in eng goal 2, and zero if in not in an end goal.
     *
     *@param curr
     *		The Chip being considered
     *@param color
     *		The side of the chip, necessary to determine which goals are being considered
     *@return 1 if in goal 1, 2 if in goal 2, 0 otherwise.
     * 
     */
	private int inEndGoal(Chip curr, int color) {
		if (color == MachinePlayer.WHITE) {
			if ((curr).getX() == 0) {
				return 1; //"Goal 1"
			} else if ((curr).getX() == 7) {
				return 2; //"Goal 2"
			}
		} else {
			if ((curr).getY() == 0) {
				return 1; // "Goal 1"
			} else if ((curr).getY() == 7) {
				return 2; //"Goal 2"
			}
		}
		return 0;
	}


	/**
	 * aligned() returns true if there are any 3 chips aligned in a network
	 * 
	 * @param list
	 * 		A DList containing all networks
	 * @return true if there are 3 chips in a row in a network, false otherwise.
	 */
	private boolean aligned(DList list) {
		//a shortcut, since you cant have 3 in a row unless there are 3 chips
		if (list.length() < 3) {
			return false;
		}
		DListNode curr = list.front();
		int x = -1;
		int y = -1;
		int diffx = 0;
		int diffy = 0;
		int vercount = 0;
		int horcount = 0;
		int diacount = 0;
		while (curr != null) {
			int tx = ((Chip) curr.item).getX();
			int ty = ((Chip) curr.item).getY();
			if (!(diffx == 0 && diffy == 0)
					&& ((tx - x) == (ty - y) || (tx - x) == -(ty - y))
					&& ((tx - x) > 0) == (diffx > 0)
					&& ((ty - y) > 0) == (diffy > 0)) {
				diacount++;
			} else {
				diacount = 0;
			}
			if (tx == x) {
				vercount++;
			} else {
				vercount = 0;
			}
			if (ty == y) {
				horcount++;
			} else {
				horcount = 0;
			}
			if (vercount >= 2 || horcount >= 2 || diacount >= 1) {
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
	 * mergeNetworks() adds all elements of list2 that are not in list1 to list1.
	 * 
	 * @param list1
	 * 		The list that will be added to
	 * @param list2
	 * 		The list that will have its extra elements added to list1
	 */
	private void mergeNetworks(DList list1, DList list2) {
		DListNode n2 = list2.front();
		while (n2 != null) {
			if (!list1.has(n2.item)) {
				list1.insertBack(n2.item);
			}
			n2 = list2.next(n2);
		}
	}
}
