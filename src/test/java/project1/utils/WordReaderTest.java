package project1.utils;

import org.junit.Test;

import java.io.FileNotFoundException;

import static org.junit.Assert.*;

public class WordReaderTest {
    @Test
    public void nextWord() throws FileNotFoundException {
        WordReader wordReader = new WordReader("dataset/test1000000.txt");

        System.out.println(wordReader.nextWord());
        System.out.println(wordReader.nextWord());
        System.out.println(wordReader.nextWord());
        System.out.println(wordReader.nextWord());
        System.out.println(wordReader.nextWord());
        System.out.println(wordReader.nextWord());
        System.out.println(wordReader.nextWord());
    }
}