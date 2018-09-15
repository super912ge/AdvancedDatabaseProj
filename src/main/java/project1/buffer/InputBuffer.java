package project1.buffer;

import project1.utils.Tuple;

import java.io.BufferedReader;
import java.io.IOException;

public class InputBuffer extends Buffer {
    private int index = 0;

    private BufferedReader bufferedReader;

    public InputBuffer(long size){
        super(size);
    }

    public void setBufferedReader(BufferedReader bufferedReader) throws IOException {
        this.bufferedReader = bufferedReader;
    }

    public boolean isReady(){
        if(bufferedReader == null)
            return false;
        else
            return true;
    }


    public void fillBuffer() throws IOException {
        index = 0;

        for(size=0; size<buffer.length; size++){
            String line = bufferedReader.readLine();
            if(line == null)
                return;

            String[] substring = line.split(" ");

            Tuple tuple = new Tuple(Integer.parseInt(substring[0]), Integer.parseInt(substring[1]), Integer.parseInt(substring[2]));
            buffer[size] = tuple;
        }
    }

    public void reset(){
        super.reset();
        index = 0;
    }

    public void closeBufferedReader() throws IOException {
        if(this.bufferedReader != null)
            this.bufferedReader.close();
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
