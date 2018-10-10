package project1;

import project1.buffer.Buffer;
import project1.buffer.InputBuffer;
import project1.buffer.OutputBuffer;
import project1.utils.Config;
import project1.utils.WordReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MergeSort {
    private Config config;

    private MergeSort(Config config) {
        this.config = config;
    }

    private Integer Merge(List<InputBuffer> buffers, OutputBuffer outputBuffer) throws IOException {

        for(InputBuffer inputBuffer: buffers) inputBuffer.fillBuffer();

        while(true){
            int smallest = Integer.MAX_VALUE;
            InputBuffer whichBuffer = null;

            for(InputBuffer buffer : buffers){
                if(buffer.getIndex() < buffer.getSize()){
                    if(buffer.getBuffer()[buffer.getIndex()] <= smallest){
                        smallest = buffer.getBuffer()[buffer.getIndex()];
                        whichBuffer = buffer;
                    }
                }
            }

            if(whichBuffer == null) break;

            outputBuffer.append(smallest);

            whichBuffer.setIndex(whichBuffer.getIndex()+1);

            if(whichBuffer.getIndex() == whichBuffer.getSize()) whichBuffer.fillBuffer();
        }

        if(outputBuffer.getSize() > 0){

            outputBuffer.writeBufferToFile();

            outputBuffer.reset();
        }
        for(InputBuffer buffer : buffers) buffer.reset();

        return outputBuffer.getCurrent_OutputDocID();
    }


    private ArrayList<Integer> phase1(WordReader wordReader) throws IOException {

        ArrayList<Integer> outputDocIDSet = new ArrayList<>();

        String word;

        Buffer buffer = new Buffer(config.getTotalBuffSize()/Config.OBJECT_SIZE);

        while((word = wordReader.nextWord()) != null){

            int tuple =  Integer.parseInt(word);

            if(buffer.isFull()){
                buffer.sort();
                outputDocIDSet.add(Config.getOutDocID());
                buffer.writeBufferToFile(String.format(Config.fname_format, Config.getAndIncrementOutDocID()));
                buffer.reset();
            }
            buffer.append(tuple);
        }

        // write the last group of tuples into disk
        // buff may not be full
        if(!buffer.isEmpty()){
            buffer.sort();
            outputDocIDSet.add(Config.getOutDocID());
            buffer.writeBufferToFile(String.format(Config.fname_format, Config.getAndIncrementOutDocID()));
        }
        wordReader.close();

        System.gc();        // do garbage collection for unused memory

        return outputDocIDSet;
    }


    private List<Integer> pass(List<Integer> inputDocIDs) throws IOException {

        OutputBuffer outputBuffer = new OutputBuffer(config.getOutputBufferSize()/Config.OBJECT_SIZE);

        ArrayList<InputBuffer> inputBuffers = new ArrayList<>();

        for(int i=0; i<config.getInputBufferCnt(); i++)
            inputBuffers.add(new InputBuffer(config.getInputBufferSize()/Config.OBJECT_SIZE));

        List<Integer> outputDocIDs = new ArrayList<>();

        for(int i=0; i<inputDocIDs.size(); i+=inputBuffers.size()){
            // each iteration merge items from N input buffers

            // initialize input buffer
            for(int j=0; j<inputBuffers.size() && i+j<inputDocIDs.size(); j++){

                InputBuffer currentInputBuff = inputBuffers.get(j);

                currentInputBuff.setBufferedReader(new WordReader(String.format(Config.fname_format, inputDocIDs.get(i+j))));
            }

            List<InputBuffer> validInputBuffers = inputBuffers.stream().filter(InputBuffer::isReady)
                    .collect(Collectors.toList());

            Integer outputDocID  = Merge(validInputBuffers, outputBuffer);      // merge tuples from input buffer to output buffer

            outputDocIDs.add(outputDocID);

            outputBuffer.startNextFile();

            for(InputBuffer inputBuffer: validInputBuffers){

                inputBuffer.closeBufferedReader();

                inputBuffer.setBufferedReader(null);
            }
        }

        return outputDocIDs;
    }


    public static void main(String[] args) throws IOException {

        String fname = "dataset/test1000000.txt";

        WordReader wordReader = new WordReader(fname);

        int numOfTuples = Integer.parseInt(wordReader.nextWord());
        String memorySize = wordReader.nextWord();

        long startTime  = System.nanoTime();

        // Configuration for input buffers and output buffers
        Config config = Config.getRecommendedConfig(numOfTuples*Config.OBJECT_SIZE, memorySize, 3, 1);

        MergeSort mergeSort = new MergeSort(config);

        // Phase 1
        List<Integer> outputDocIDs = mergeSort.phase1(wordReader);

        // Phase 2
        while(outputDocIDs.size() > 1)
            outputDocIDs = mergeSort.pass(outputDocIDs);

        long endTime = System.nanoTime();

        System.out.println(String.format("The final input is in output_%d.txt file", outputDocIDs.get(0)));

        System.out.println("Cost of merge sort: " + (endTime-startTime)/1000000 + "ms");
    }
}
