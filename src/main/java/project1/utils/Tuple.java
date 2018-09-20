package project1.utils;

import java.io.Serializable;

public class Tuple implements  Comparable<Tuple> {
    private int key;

    public Tuple(int key) {
        this.key = key;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    @Override
    public int compareTo(Tuple o) {
        if(o == this)
            return 0;

        if(this.key > o.key)
            return 1;
        else if(this.key < o.key)
            return -1;
        else
            return 0;
    }

    static private Tuple largestTuple = new Tuple(Integer.MAX_VALUE);
    static public Tuple getLargestValue(){
        return largestTuple;
    }
}
