package project1.utils;

public class Config {
    public static String fname_format = "dataset/output_%d.txt";
    private static int outDocID = 0;
    private static final int RESERVED_SIZE = 512*1024;   // 1M memery for temporary variable

    private long memoery_size;
    private long reserve_size;
    private long totalBuffSize;

    private int outputBufferCnt;
    private long outputBufferSize;

    private int inputBufferCnt;
    private long inputBufferSize;

    public long getTotalBuffSize() {
        return totalBuffSize;
    }

    public void setTotalBuffSize(long totalBuffSize) {
        this.totalBuffSize = totalBuffSize;
    }

    public long getMemoery_size() {
        return memoery_size;
    }

    public long getReserve_size() {
        return reserve_size;
    }

    public int getInputBufferCnt() {
        return inputBufferCnt;
    }

    public long getInputBufferSize() {
        return inputBufferSize;
    }

    public int getOutputBufferCnt() {
        return outputBufferCnt;
    }

    public long getOutputBufferSize() {
        return outputBufferSize;
    }


    public static int getAndIncrementOutDocID(){
        return outDocID++;
    }

    public  static int getOutDocID() {
        return outDocID;
    }

    public static Config getRecommendedConfig(int relationsSize, String memorySize, int inputBufferCnt, int outputBufferCnt){
        Config config = new Config();

        config.memoery_size = convertToByte(memorySize);

        config.reserve_size = RESERVED_SIZE;
        config.totalBuffSize = config.memoery_size - RESERVED_SIZE;

        config.inputBufferCnt = inputBufferCnt;
        config.inputBufferSize = config.totalBuffSize/ (inputBufferCnt+outputBufferCnt);

        config.outputBufferCnt = outputBufferCnt;
        config.outputBufferSize = config.totalBuffSize/ (inputBufferCnt+outputBufferCnt);



        return config;
    }

    private static int convertToByte(String memorySize){
        int i=0;
        while(Character.isDigit(memorySize.charAt(i)))
            i++;

        int size = Integer.parseInt(memorySize.substring(0, i));
        char unit = memorySize.charAt(i);

        if(Character.toUpperCase(unit) == 'M')
            return size* 1024*1024;
        else if(Character.toUpperCase(unit) == 'K')
            return size * 1024;
        else
            return size;
    }


}
