package project1.utils;

import java.io.*;
import java.util.Random;

public class DatasetGenerator {

    public static void main(String[] args) throws IOException {
        generateDataset(1000, "dataset/test1000.txt");
    }

    public static void generateDataset(int size, String fname) throws IOException {
        File file = new File(fname);
        FileWriter fileWriter = new FileWriter(file);
        BufferedWriter bf = new BufferedWriter(fileWriter);
        PrintWriter printWriter = new PrintWriter(bf);

        printWriter.println(size);
        printWriter.println();

        Random rm = new Random();
        for(int i=0; i<size; i++){
            int val1 = rm.nextInt(10000);
            int val2 = rm.nextInt(10000);
            int val3 = rm.nextInt(10000);

            printWriter.println(String.format("%d %d %d", val1, val2, val3));
        }

        printWriter.close();
    }
}
