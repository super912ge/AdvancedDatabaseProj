package project1.buffer;

import java.io.*;
import java.util.Arrays;

public class Buffer implements Serializable {
    protected int size = 0;
    protected int[] buffer;

    public Buffer(int[] buffer) {
        this.buffer = buffer;
    }

    public Buffer(long size){
        this.buffer = new int[(int)size];
    }

    public int getSize() {
        return size;
    }

    public int[] getBuffer() {
        return buffer;
    }

    public boolean isFull(){
        if(size < buffer.length)
            return false;
        else
            return true;
    }

    public boolean isEmpty(){
        if(size > 0)
            return false;
        else
            return true;
    }

    public boolean append(int value) {
        if(size < buffer.length){
            buffer[size++] = value;
            return true;
        }
        else
            return false;
    }

    public void sort(){
        Arrays.sort(buffer, 0, size);
    }

    public void reset(){
        size = 0;
    }

    public void writeBufferToFile(String fname) throws IOException {
        File file = new File(fname);
        FileWriter fileWriter = new FileWriter(file);
        BufferedWriter bf = new BufferedWriter(fileWriter);
        PrintWriter printWriter = new PrintWriter(bf);

        for(int i=0; i<size; i++)
            printWriter.println(String.format("%d", buffer[i]));
        printWriter.close();
    }
}
