/* Chip.java */

package player;

import list.*;

class Chip {
    private int color;
    private int x;
    private int y;
    public Chip[] inSight;
   
    //makes a chip with no location
    public Chip() {
        x = -1;
        y = -1;
        color = -1;
        inSight = new Chip[8];
    }
    
    public Chip(int X, int Y, int Color) {
        x = X;
        y = Y;
        color = Color;
        //cannot have more than 8 chips in line of sight
        inSight = new Chip[8];
    }
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int color() {
        return color;
    }

    /**
     * makes self invalid
     * deletes itself off everything its networked to
     * cleans out own networkedto
     * returns a copy of its inSight(that no longer exists)
     */
    public Chip[] dissapear() {
        x = -1;
        y = -1;
        color = -1;
        Chip[] tmp = new Chip[inSight.length];
        for (int i = 0; i < inSight.length; i++) {
            tmp[i] = inSight[i];
            noC(inSight[i]);
        }
        inSight = null;
        return tmp;
    }

    /**
     * clears the chip's inSight
     */
    public void clear() {
        inSight = new Chip[8];
    }

    /**
     * checks if each chip is already in each others list
     * adds c to the chip's inSight 
     * also adds self to c's inSight
     */
    public void addC(Chip c) {
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
     * removes c from chip's inSight 
     * also removes self from c's inSight
     */
    public void noC(Chip c) {
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
     * that this chip is connected to(as a Dlist)
     */
    public DList network() {
        DList network = new DList();
        DList list = new DList();
        list.insertFront(this);
        DList blacklist = new DList();
        findTails(network, list, blacklist);
        build(network);
        return network;
    }

    /**
     * searches and builds DLists using inSight
     * blacklist are the previous chips, so search doesn't go looking back
     */
    private void findTails(DList network, DList list, DList blacklist) {
        //if there is nothing in inSight that isn't in the blacklist then return;
        boolean nothing = true;
        //for every non-null value in inSight except for what in the blacklist
        for (int i = 0; i < inSight.length; i++) {
            if (inSight[i] != null && !blacklist.has(inSight[i])) {
                nothing = false;
                //add self to blacklist
                blacklist.insertBack(this);
                //add that to the back of list
                list.insertBack(inSight[i]);
                //make a copy of this and add it to the network
                network.insertBack(list.copy());
                //call findTails on chip from inSight
                inSight[i].findTails(network, list, blacklist);
            }
        }
        if (nothing) {
            return;
        }
    }

    /**
     * knowing that all tails start with this chip,
     * piece them all together in every combination
     */
    private void build(DList network) {
        int length = network.length();
        DListNode n1 = network.front();
        DListNode n2 = n1;
        for (int i = 0; i < length; i++) {
            //resetting n2
            n2 = network.front();
            //moving n2 to the right node
            for (int k = 0; k <= i; k++) {
                n2 = network.next(n2);
            }
            if (i == length - 1) {
                //remove this node
                network.remove(n2);
                return;
            }
            DListNode save = n2;
            //link-mutate copies and add to the end
            for (int j = i + 1; j < length; j++) {
                DList copy = ((DList) n1.item).copy();
                link(copy, (DList) n2.item);
                network.insertBack(copy);
                n2 = network.next(n2);
            }
            //link-mutate the original
            link((DList) n1.item, (DList) save.item);
            n1 = network.next(n1);
        }
    }

    private void link(DList list1, DList list2) {
        DListNode curr = list2.front();
        list1.remove(list1.front());
        while (curr != null) {
            list1.insertFront(curr.item);
            curr = list2.next(curr);
        }
    }

    public boolean equals(Chip chip) {
        return (x == chip.x && y == chip.y && color == chip.color);
    } 
    
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
    
    public String inSightString(){
    	String s=new String("[");
    	for (int i=0; i<inSight.length;i++)
    	{
    		s=s+" "+inSight[i];
    	}
    	return s+" ]";
    }

    public void tester() {
        Chip chip = new Chip();
        Chip c1 = new Chip();
        Chip c2 = new Chip();
        System.out.println("adding c1");
        chip.addC(c1);
        //printinSight(chip);
        visualChip(chip);
        System.out.println("adding c1 again");
        chip.addC(c1);
        //printinSight(chip);
        visualChip(chip);
        System.out.println("adding c2");
        chip.addC(c2);
        //printinSight(chip);
        visualChip(chip);
        System.out.println("removing c1");
        chip.noC(c1);
        //printinSight(chip);
        visualChip(chip);
        System.out.println("removing c1 again");
        chip.noC(c1);
        //printinSight(chip);
        visualChip(chip);
        System.out.println("removing c2");
        chip.noC(c2);
        //printinSight(chip);
        visualChip(chip);
    }

    public void printinSight(Chip chip) {
        for (int i = 0; i < chip.inSight.length; i++) {
            System.out.println(chip.inSight[i]);
        }
    }

    public void visualChip(Chip chip) {
        System.out.print("x: " + x + " y: " + y + " color: " + color + " [");
        for (int i = 0; i < chip.inSight.length; i++) {
            if (chip.inSight[i] == null) {
                System.out.print("_");
            } else {
                System.out.print("X");
            }
        }
        System.out.println("]");
    }
}
