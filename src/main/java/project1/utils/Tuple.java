package project1.utils;

import java.io.Serializable;

public class Tuple implements Serializable, Comparable<Tuple> {
    private int first;
    private int second;
    private int third;

    public Tuple(int first, int second, int third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public int getFirst() {
        return first;
    }

    public int getSecond() {
        return second;
    }

    public int getThird() {
        return third;
    }


    @Override
    public int compareTo(Tuple o) {
        if(o == this)
            return 0;

        if(this.first > o.first)
            return 1;
        else if(this.first < o.first)
            return -1;
        else{
            if(this.second > o.second)
                return 1;
            else if(this.second < o.second)
                return -1;
            else{
                if(this.third > o.third )
                    return 1;
                else if(this.third < o.third)
                    return -1;
                else
                    return 0;
            }
        }
    }

    static private Tuple largestTuple = new Tuple(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
    static public Tuple getLargestValue(){
        return largestTuple;
    }
}
