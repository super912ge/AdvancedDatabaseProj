package project1.buffer;

import project1.utils.Config;
import project1.utils.Tuple;

import java.io.*;

public class OutputBuffer extends Buffer implements Serializable {
    private int current_OutputDocID;
    public OutputBuffer(long size) {
        super(size);

        current_OutputDocID = Config.getAndIncrementOutDocID();
    }

    public boolean append(Tuple value) {
        try{
            if(size == buffer.length){
                writeBufferToFile();
                size = 0;
            }

            buffer[size++] = value;
            return true;
        }catch (IOException ex){
            System.out.println(ex.getCause());
            return false;
        }
    }

    public void writeBufferToFile() throws IOException {
        File file = new File(String.format(Config.fname_format, current_OutputDocID));
        FileWriter fileWriter = new FileWriter(file, true);
        BufferedWriter bf = new BufferedWriter(fileWriter);
        PrintWriter printWriter = new PrintWriter(bf);

        for(int i=0; i<size; i++)
            printWriter.println(String.format("%d", buffer[i].getKey()));
        printWriter.close();
    }

    public int getCurrent_OutputDocID() {
        return current_OutputDocID;
    }

    public void startNextFile(){
        super.reset();
        current_OutputDocID = Config.getAndIncrementOutDocID();
    }

}
