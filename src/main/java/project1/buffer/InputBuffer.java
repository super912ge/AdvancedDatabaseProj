package project1.buffer;

import project1.utils.WordReader;

import java.io.IOException;

public class InputBuffer extends Buffer {
    private int index = 0;

    private WordReader wordReader;

    public InputBuffer(long size){
        super(size);
    }

    public void setBufferedReader(WordReader wordReader) throws IOException {
        this.wordReader = wordReader;
    }

    public boolean isReady(){
        if(wordReader == null)
            return false;
        else
            return true;
    }


    public void fillBuffer() {
        index = 0;

        String word;
        for(size=0; size<buffer.length; size++){
             word = wordReader.nextWord();
             if(word == null)
                 return;
             int tuple = Integer.parseInt(word);
             buffer[size] = tuple;
        }

    }

    public void reset(){
        super.reset();
        index = 0;
    }

    public void closeBufferedReader() {
        if(this.wordReader != null)
            this.wordReader.close();
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
