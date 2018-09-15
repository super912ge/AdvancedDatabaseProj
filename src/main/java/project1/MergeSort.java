package project1;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MergeSort {
    private Config config;

    public MergeSort(Config config) {
        this.config = config;
    }


    public Integer Merge(List<InputBuffer> buffers, OutputBuffer outputBuffer) throws IOException {
        while(true){
            Tuple smallest = Tuple.getLargestValue();
            for(InputBuffer inputBuffer : buffers){
                if(inputBuffer.getIndex() < inputBuffer.size){
                    if(inputBuffer.buffer[inputBuffer.getIndex()].compareTo(smallest) <= 0){
                        smallest = inputBuffer.buffer[inputBuffer.getIndex()];
                        inputBuffer.setIndex(inputBuffer.getIndex()+1);
                    }
                }
            }

            if(smallest == Tuple.getLargestValue())
                break;
            outputBuffer.append(smallest);
        }

        if(outputBuffer.size > 0){
            outputBuffer.writeBufferToFile();
            outputBuffer.reset();
        }
        for(InputBuffer buffer : buffers)
            buffer.reset();

        return outputBuffer.getCurrent_OutputDocID();
    }


    private ArrayList<Integer> phase1(BufferedReader bufferedReader) throws IOException {
        ArrayList<Integer> outputDocIDSet = new ArrayList<>();

        String line;
        Buffer buffer = new Buffer(config.getTotalBuffSize()/12);         // 4M memory use
        while((line = bufferedReader.readLine()) != null){
            String substring[] = line.split(" ");
            Tuple tuple = new Tuple(Integer.parseInt(substring[0]), Integer.parseInt(substring[1]),Integer.parseInt(substring[2]));

            if(buffer.isFull()){
                buffer.sort();
                outputDocIDSet.add(config.getOutDocID());
                buffer.writeBufferToFile(String.format(Config.fname_format, config.getAndIncrementOutDocID()));
                buffer.reset();
            }
            buffer.append(tuple);
        }
        if(!buffer.isEmpty()){
            buffer.sort();
            outputDocIDSet.add(config.getOutDocID());
            buffer.writeBufferToFile(String.format(Config.fname_format, config.getAndIncrementOutDocID()));
        }
        bufferedReader.close();

        buffer = null;
        System.gc();        // do garbage collection for unused memory

        return outputDocIDSet;
    }


    private List<Integer> pass(List<Integer> inputDocIDs) throws IOException {
        OutputBuffer outputBuffer = new OutputBuffer(config.getOutputBufferSize()/12);
        ArrayList<InputBuffer> inputBuffers = new ArrayList<>();
        for(int i=0; i<config.getInputBufferCnt(); i++)
            inputBuffers.add(new InputBuffer(config.getInputBufferSize()/12));         // each input buffer takes 1M memory

        List<Integer> outputDocIDs = new ArrayList<>();
        for(int i=0; i<inputDocIDs.size(); i+=inputBuffers.size()){
            for(int j=0; j<inputBuffers.size() && i+j<inputDocIDs.size(); j++){
                InputBuffer currentInputBuff = inputBuffers.get(j);

                FileInputStream fileInputStream = new FileInputStream(String.format(Config.fname_format, inputDocIDs.get(i+j)));
                currentInputBuff.setBufferedReader(new BufferedReader(new InputStreamReader(fileInputStream)));
            }

            for(InputBuffer inputBuffer : inputBuffers){
                if(inputBuffer.isReady()){
                    inputBuffer.fillBuffer();
                    inputBuffer.closeBufferedReader();
                    inputBuffer.setBufferedReader(null);
                }
            }

            List<InputBuffer> validInputBuffers = inputBuffers.stream().filter(inputBuffer -> inputBuffer.size > 0).collect(Collectors.toList());
            Integer outputDocID  = Merge(validInputBuffers, outputBuffer);
            outputDocIDs.add(outputDocID);
            outputBuffer.startNextFile();
        }

        return outputDocIDs;
    }


    public static void main(String[] args) throws IOException {
        String fname = "dataset/test1000.txt";
        int sizeOfTuple = 12;

        FileInputStream fis = new FileInputStream(fname);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fis));

        String[] firstLine =  bufferedReader.readLine().split(" ");
        int numOfTuples = Integer.parseInt(firstLine[0]);
        String memorySize = firstLine[1];
        bufferedReader.readLine();      // empty line

        // Configuration for input buffers and output buffers
        Config config = Config.getRecommendedConfig(numOfTuples*sizeOfTuple, memorySize, 3, 1);   // will be used in future
        MergeSort mergeSort = new MergeSort(config);

        // Phase 1
        List<Integer> outputDocIDs = mergeSort.phase1(bufferedReader);

        // Phase 2
        while(outputDocIDs.size() > 1)
            outputDocIDs = mergeSort.pass(outputDocIDs);

        System.out.println(String.format("The final input is in output_%d.txt file", outputDocIDs.get(0)) );
    }


}
