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
     */
    public void remove() {
        x = -1;
        y = -1;
        color = -1;
        //Add code that goes through all nodes in networkedTo and deletes this chip from their lists.
    }

    /**
     * adds c to the chip's inSight 
     * also adds self to c's inSight
     */
    public void addC(Chip c) {
        boolean added1 = false;
        boolean added2 = false;
        for (int i = 0; i < inSight.length; i++) {
            if (!added1 && inSight[i] == null) {
                inSight[i] = c;
                added1 = true;
            }
            if (!added2 && c.inSight[i] == null) {
                c.inSight[i] = this;
                added2 = true;
            }
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
