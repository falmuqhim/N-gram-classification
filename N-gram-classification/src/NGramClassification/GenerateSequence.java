/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NGramClassification;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author falmuqhim
 */
public class GenerateSequence {
    
    /**
     * This method reads a given sequence file, and generates sequences with a 
     * given length with the following format:
     * For example we have: BUJJASHSGAHS, and length is 5, it divides the 
     * sequence with length of 5 starting from index 0 and goes forward
     *
     * @param sequenceFile      sequence file location
     * @param outputDirectory   directory where the generated file will be saved
     * @param length            length of each sequence
     * @return                  index 0 will have generated sequence number, 
     *                          and index 1 will have how many sequences are 
     *                          less than desired length
     */
    public int[] generateSequences(String sequenceFile, String outputDirectory, int length) {
        BufferedReader reader = null;
        BufferedWriter writer = null;
        List<String> lines = null;
        int sequenceCount = 0;
        int lessThanLength = 0;
        int s = sequenceFile.lastIndexOf("/");
        String file = "";
        if (s != -1) {
            file = sequenceFile.substring(s + 1);
        }

        try {
            reader = new BufferedReader(new FileReader(sequenceFile));
            writer = new BufferedWriter(new FileWriter(outputDirectory + "generated_sequnces_length_" + length + "_for_" + file));
            if (reader != null) {
                lines = reader.lines().collect(Collectors.toList());
            }
        } catch (FileNotFoundException e) {
            System.err.println("Couldn't read the file: " + sequenceFile);
            System.err.println(e.toString());
            // couldn't read the file
        } catch (IOException e) {
            System.err.println("Couldn't open the file in directory: " + outputDirectory);
            System.err.println(e.toString());
            // couldn't open the file
        }
        //to check the lines are read correctly
        if (lines != null) {
            String line = "";
            /* 
             * Go throgh each line and accept protein in the given range.
             */
            System.out.println("lines: " + lines.size());
            int counter = 0;
            for (int i = 1; i < lines.size(); i += 2) {
                line = lines.get(i);
                System.out.println("size: " + line.length());
                counter++;
                
                //to count how many sequences are less than the desired length
                if(line.length() < length) {
                    lessThanLength++;
                    continue;
                }
                for(int j=0; j<=(line.length()-length); j++) {
                    
                    try {
                        if (writer != null) {
                            /*
                            * naming sequences based on the sequence number in 
                            * the file, and the starting and ending points
                            */
                            writer.append(">" + (counter) + "." + (j+1) + "." + (j+length));
                            writer.newLine();
                            writer.append(line.substring(j, j+length));
                            writer.newLine();
                            sequenceCount++;
                        }
                    } catch (IOException e) {
                        System.err.println("Couldn't open the file in directory: " + outputDirectory);
                        System.err.println(e.toString());
                        // couldn't open the file
                    }
                }
            }
        }
        try {
            if (reader != null) {
                reader.close();
            }
            if (writer != null) {
                writer.flush();
                writer.close();
            }
        } catch (IOException e) {
            System.err.println(e.toString());
        }
        int[] result = new int[2];
        result[0] = sequenceCount;
        result[1] = lessThanLength;
        return result;
    }
}
