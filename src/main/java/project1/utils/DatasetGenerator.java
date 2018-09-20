package project1.utils;

import java.io.*;
import java.util.Random;

public class DatasetGenerator {

    public static void main(String[] args) throws IOException {
        generateDataset(1000000, "5M", "dataset/test1000000.txt");
//        generateDataset(1000, "5M", "dataset/test1000.txt");
    }

    public static void generateDataset(int tuple_size, String memory_size, String fname) throws IOException {
        File file = new File(fname);
        FileWriter fileWriter = new FileWriter(file);
        BufferedWriter bf = new BufferedWriter(fileWriter);
        PrintWriter printWriter = new PrintWriter(bf);

        printWriter.println(String.format("%d %s", tuple_size, memory_size));
        printWriter.println();

        Random rm = new Random();
        for(int i=0; i<tuple_size; i++){
            int val1 = rm.nextInt(tuple_size);

            printWriter.print(val1);
            printWriter.print('\t');
        }

        printWriter.close();
    }
}
