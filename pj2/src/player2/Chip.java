/* Chip.java */

package player;

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
}
