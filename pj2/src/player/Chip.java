/* Chip.java */

package player;

import list.*;

class Chip {
    private int color;
    private int x;
    private int y;
    private Chip[] inSight;
   
    /**
     * takes in no arguments and contructs an empty, invalid chip
     * there are only 8 possible chips within ones sight but the 9th slot is to
     * allow for a temporary slot to be used in min/max
     */
    Chip() {
        x = -1;
        y = -1;
        color = -1;
        inSight = new Chip[9];
    }
    
    /**
     * contructs a new chip with x, y coordinates and a color
     * there are only 8 possible chips within ones sight but the 9th slot is to
     * allow for a temporary slot to be used in min/max
     *
     * @param x
     * the x coordinate of this chip
     * @param y
     * the y coordinate of this chip
     * @param color
     * the color of this chip
     */
    Chip(int x, int y, int color) {
        this.x = x;
        this.y = y;
        this.color = color;
        inSight = new Chip[9];
    }
    
    /**
     * takes in no arguments and returns "this" chips x value
     */
    public int getX() {
        return x;
    }

    /**
     * takes in no arguments and returns "this" chip's y value
     */
    public int getY() {
        return y;
    }

    /**
     * takes in no arguments and returns "this" chip's color
     */
    public int color() {
        return color;
    }

    /**
     * clears the chip's inSight to be repopulated by Board.addChip
     */
    void clear() {
        inSight = new Chip[9];
    }

    /**
     * checks if each chip is already in each others list
     * adds c to the chip's inSight if not already in
     * also adds self to c's inSight if not already in
     *
     * @param c
     * mutually add each other to each other's inSight's ("this" and "c")
     */
    void addC(Chip c) {
        int add1 = -1;
        int add2 = -1;
        boolean added1 = false;
        boolean added2 = false;
        for (int i = 0; i < inSight.length; i++) {
            if (inSight[i] == c) {
                added1 = true;
            }
            if (c.inSight[i] == this) {
                added2 = true;
            }
            if (inSight[i] == null) {
                add1 = i;
            }
            if (c.inSight[i] == null) {
                add2 = i;
            }
        }
        if (!added1) {
            inSight[add1] = c;
        }
        if (!added2) {
            c.inSight[add2] = this;
        }
    }

    /**
     * removes all instances of c from chip's inSight 
     * also removes all instances of self from c's inSight
     *
     * @param c
     * mutually remove each other from each other's inSight's ("this" and "c")
     */
    void noC(Chip c) {
        for (int i = 0; i < inSight.length; i++) {
            if (inSight[i] == c) {
                inSight[i] = null;
            }
            if (c.inSight[i] == this) {
                c.inSight[i] = null;
            }
        }
    }

    /**
     * return a DList of all possible networks(legal or illegal)
     * that this chip is connected to(also represented as a Dlist)
     *
     * utilizes Chip.findTails and Chip.build
     *
     * @param color
     * the color of pieces that the network is to be formed from
     */
    DList network(int color) {
        DList network = new DList();
        DList list = new DList();
        list.insertFront(this);
        DList blacklist = new DList();
        findTails(color, network, list, blacklist);
        build(network);
        return network;
    }

    /**
     * This is called findTails because this method is implemented as a ripple, it starts
     * from the center (the "head") and branches outwards,
     * searching and building DLists using inSight
     *
     * @param color
     * gives the color of the chips in these networks
     * @param network
     * is a DList that holds all of the networks found which are also represented as DLists
     * @param list
     * is the current network that is being used in the current recursive iteration
     * it is copied and branched off into further networks
     * @param blacklist
     * holds the previous chips, so search doesn't go looking back
     * each iteration of findTails holds its own blacklist, because if the blacklist is
     * simply mutated then you will end up choking off possible networks
     */
    private void findTails(int color, DList network, DList list, DList blacklist) {
        //for every non-null value in inSight except for what in the blacklist
        for (int i = 0; i < inSight.length; i++) {
            //copying lists for the next iteration so that the copy of the list that
            //remains in this recursive call to findTails is unchanged
            DList blist = blacklist.copy();
            DList tmplist = list.copy();
            if (inSight[i] != null && inSight[i].color() == color && !blist.has(inSight[i])) {
                //add self to blacklist
                blist.insertBack(this);
                //add that to the back of list
                tmplist.insertBack(inSight[i]);
                //make a copy of this and add it to the network
                network.insertBack(tmplist.copy());
                //call findTails on chip from inSight
                inSight[i].findTails(color, network, tmplist, blist);
            }
        }
    }

    /**
     * takes in the load of all possible tails from findTails as network
     * knowing that all tails start with "this" chip,
     * piece them all together in every combination with tails pointing
     * "outwards" and head pointing "in"
     * 
     * utilizes Chip.link
     *
     * @param network
     * The output of findTails, a dump of every network stemming from the head of
     * findTails
     */
    private void build(DList network) {
        int length = network.length();
        DListNode n1 = network.front();
        DListNode n2;
        for (int i = 0; i < length; i++) {
            //resetting n2
            n2 = network.front();
            //moving n2 to the correct node
            for (int k = 0; k <= i; k++) {
                n2 = network.next(n2);
            }
            //link-mutate copies and add to the end
            for (int j = i + 1; j < length; j++) {
                DList copy = ((DList) n1.item).copy();
                link(copy, (DList) n2.item);
                if (!copy.hasRepeats()) {
                    network.insertBack(copy);
                }
                n2 = network.next(n2);
            }
            n1 = network.next(n1);
        }
    }

    /**
     * takes 2 lists and mutates list1 by adding list2 onto it
     * they are merged with their tails outwards and the heads next
     * to each other but with the head from list1 removed so that
     * there aren't duplicate nodes
     *
     * @param list1
     * the list to be mutated
     * @param list2
     * the list that is the mutaee(not sure if thats a word but its the
     * list that will provide the items to be used in the mutating of list1)
     */
    private void link(DList list1, DList list2) {
        DListNode curr = list2.front();
        list1.remove(list1.front());
        while (curr != null) {
            list1.insertFront(curr.item);
            curr = list2.next(curr);
        }
    }

    /**
     * returns true if "this" and chip have equal x, y, and color fields
     * returns false otherwise
     *
     * @param chip
     * the that that is to be compared to "this"
     */
    public boolean equals(Chip chip) {
        return (x == chip.x && y == chip.y && color == chip.color);
    } 
    
    /**
     * returns a string representation of "this" Chip
     */
    public String toString() {
    	String s;
    	if (color==Board.BLACK)
    	{
    		s="-BLACK";
    	}
    	else
    	{
    		s="-WHITE";
    	}
    	
    	s=s+" at ["+getX()+"]["+getY()+"]-";
    	
    	return s;
	}
} 
