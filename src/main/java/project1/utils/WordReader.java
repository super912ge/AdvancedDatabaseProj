package project1.utils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class WordReader {

    private FileReader inputStream;

    public WordReader(String fname) throws FileNotFoundException {
        inputStream = new FileReader(fname);
    }

    public String nextWord(){
        if(inputStream == null)
            return null;

        int ch;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            // skip space or tab
            while((ch = inputStream.read()) != -1){
                if(ch != ' ' && ch != '\t' && ch != '\n' && ch != '\r')
                    break;
            }

            // word exists
            if(ch != -1){
                stringBuilder.append((char)ch);
                while(true){
                    ch = inputStream.read();
                    if(ch == -1 || ch == ' ' || ch == '\t'|| ch == '\n' || ch == '\r')
                        break;
                    stringBuilder.append((char)ch);
                }
                return stringBuilder.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void close(){
        return;
    }
}
