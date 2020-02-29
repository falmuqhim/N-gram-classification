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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author falmuqhim
 */
public class MatchingSequence {

    public int generateMatchingFile(String oriSeqFile, String dividedSeqFile, String predictedFile, String outputDirectory) {

        BufferedReader reader = null;
        int size = 0;
        BufferedWriter writer = null;
        List<String> lines = null;
        List<String> predicted = new ArrayList<>();
        List<String> oriSeq = new ArrayList<>();

        //read after @data
        try {
            reader = new BufferedReader(new FileReader(predictedFile));
            if (reader != null) {
                lines = reader.lines().collect(Collectors.toList());
            }
        } catch (FileNotFoundException e) {
            System.err.println("Couldn't read the file: " + predictedFile);
            System.err.println(e.toString());
            // couldn't read the file
        }
        int begin = -1;
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).equals("@data")) {
                begin = i + 1;
                break;
            }
        }
        for (int i = begin; i < lines.size(); i++) {
            predicted.add(lines.get(i));
        }

        try {
            if (reader != null) {
                reader.close();
            }
        } catch (IOException e) {
            System.err.println(e.toString());
        }
        reader = null;
        lines = null;

        try {
            reader = new BufferedReader(new FileReader(oriSeqFile));
            if (reader != null) {
                lines = reader.lines().collect(Collectors.toList());
            }
        } catch (FileNotFoundException e) {
            System.err.println("Couldn't read the file: " + oriSeqFile);
            System.err.println(e.toString());
            // couldn't read the file
        }

        for (int i = 1; i < lines.size(); i += 2) {
            oriSeq.add(lines.get(i));
        }
        try {
            if (reader != null) {
                reader.close();
            }
        } catch (IOException e) {
            System.err.println(e.toString());
        }
        reader = null;
        lines = null;
        try {
            reader = new BufferedReader(new FileReader(dividedSeqFile));
            if (reader != null) {
                lines = reader.lines().collect(Collectors.toList());
            }
        } catch (FileNotFoundException e) {
            System.err.println("Couldn't read the file: " + dividedSeqFile);
            System.err.println(e.toString());
            // couldn't read the file
        }
        try {
            if (reader != null) {
                reader.close();
            }
        } catch (IOException e) {
            System.err.println(e.toString());
        }

        try {
            writer = new BufferedWriter(new FileWriter(outputDirectory + "matching_output" + ".csv"));
        } catch (IOException e) {
            System.err.println("Couldn't open the file in directory: " + outputDirectory);
            System.err.println(e.toString());
            // couldn't open the file
        }

        int prevIndex = -1;
        //to check the lines are read correctly
        if (lines != null) {
            size = lines.size();
            for (int i = 1; i < lines.size(); i += 2) {
                int seqIndex = Integer.parseInt(lines.get(i - 1).substring(lines.get(i - 1).indexOf(">") + 1, lines.get(i - 1).indexOf("."))) - 1;
                if (prevIndex != seqIndex) {
                    try {
                        if (writer != null) {
                            writer.append(oriSeq.get(seqIndex));
                            writer.newLine();
                        }
                    } catch (IOException e) {
                        System.err.println("Couldn't open the file in directory: " + outputDirectory);
                        System.err.println(e.toString());
                        // couldn't open the file
                    }
                }
                String[] result = predicted.get(i/2).split(",");
                try {
                    if (writer != null) {
                        writer.append(lines.get(i));
                        writer.append(",");
                        writer.append(result[result.length-1]);
                        writer.append(",");
                        writer.append(result[result.length-2]);
                        writer.append(",");
                        writer.append(lines.get(i-1).split("@")[1]);
                        writer.newLine();
                    }
                } catch (IOException e) {
                    System.err.println("Couldn't open the file in directory: " + outputDirectory);
                    System.err.println(e.toString());
                    // couldn't open the file
                }
                prevIndex = seqIndex;
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

        return size;
    }

}
