package com.example.android.minmaxagent;

/**
 * Created by siddh on 12/16/2017.
 */

class FruitGridPoint {
    /** The location of this point on the grid. */
    int x, y;
    int value;

    FruitGridPoint(int x, int y, int val) {
        this.x = x;
        this.y = y;
        this.value = val;
    }

    /**
     * Check if another FruitGridPoint has the same x, y coordinates and value.
     */
    @Override
    public boolean equals(Object o) {

        // If the object is compared with itself then return true
        if (o == this) {
            return true;
        }

        // Check if o is an instance of NurseryGridPoint or not
        // "null instanceof [type]" also returns false
        if (!(o instanceof FruitGridPoint)) {
            return false;
        }

        // typecast o to NurseryGridPoint so that we can compare data members
        FruitGridPoint c = (FruitGridPoint) o;

        // Compare the data members and return accordingly
        return (this.x == c.x && this.y == c.y && this.value == c.value);
    }

    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }

    @Override
    public String toString() {
        return String.format("%d(%d, %d)", value, x, y);
    }

    /**
     * Your selected move, represented as two characters: A letter from A to Z
     * representing the column number (where A is the leftmost column, B is the
     * next one to the right, etc), and A number from 1 to 26 representing the
     * row number (where 1 is the top row, 2 is the row below it, etc).
     */
    static String pointToMoveString(int x, int y) {
        StringBuilder sb = new StringBuilder();

        sb.append((char) ('A' + y));
        sb.append(FruitRageNode.n - x);

        return sb.toString();
    }

}