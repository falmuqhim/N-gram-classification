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
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author falmuqhim
 */
public class DistinctSequence {

    public int removeDuplicate(String sequenceFile, String outputDirectory) {

        LinkedHashMap<String, String> newSeq = new LinkedHashMap<>();
        BufferedReader reader = null;
        BufferedWriter writer = null;
        List<String> lines = null;

        int duplicateCounter = 0;

        int start = sequenceFile.lastIndexOf("/");
        String file = "";
        if (start != -1) {
            file = sequenceFile.substring(start + 1);
        }

        try {
            reader = new BufferedReader(new FileReader(sequenceFile));
            writer = new BufferedWriter(new FileWriter(outputDirectory + "distinct_" + file));
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
        System.out.println("size: " + lines.size());
        //to check the lines are read correctly
        if (lines != null) {

            /* 
             * Go throgh each line and find duplicate in the sequence file.
             */
            for (int i = 0; i < lines.size() - 1; i += 2) {
                if (newSeq.containsKey(lines.get(i + 1))) {
                    duplicateCounter++;
                } else {
                    newSeq.put(lines.get(i + 1), lines.get(i));
                }
            }
            for (Map.Entry<String, String> entry : newSeq.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                try {
                    if (writer != null) {
                        writer.append(value);
                        writer.newLine();
                        writer.append(key);
                        writer.newLine();
                    }
                } catch (IOException e) {
                    System.err.println("Couldn't open the file in directory: " + outputDirectory);
                    System.err.println(e.toString());
                    // couldn't open the file
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

        return duplicateCounter;
    }

    public int acceptByRange(String sequenceFile, String outputDirectory, int start, int end) {

        LinkedHashMap<String, String> newSeq = new LinkedHashMap<>();
        BufferedReader reader = null;
        BufferedWriter writer = null;
        List<String> lines = null;

        int removedCounter = 0;

        int s = sequenceFile.lastIndexOf("/");
        String file = "";
        if (s != -1) {
            file = sequenceFile.substring(s + 1);
        }

        try {
            reader = new BufferedReader(new FileReader(sequenceFile));
            writer = new BufferedWriter(new FileWriter(outputDirectory + "range_" + start + "_" + end + "_" + file));
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
        System.out.println("size: " + lines.size());
        //to check the lines are read correctly
        if (lines != null) {
            /* 
             * Go throgh each line and accept protein in the given range.
             */
            for (int i = 1; i < lines.size() - 1; i += 2) {
                if (lines.get(i).length() >= start && lines.get(i).length() <= end) {
                    try {
                        if (writer != null) {
                            writer.append(lines.get(i - 1));
                            writer.newLine();
                            writer.append(lines.get(i));
                            writer.newLine();
                        }
                    } catch (IOException e) {
                        System.err.println("Couldn't open the file in directory: " + outputDirectory);
                        System.err.println(e.toString());
                        // couldn't open the file
                    }
                } else {
                    removedCounter++;
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

        return removedCounter;
    }
}
