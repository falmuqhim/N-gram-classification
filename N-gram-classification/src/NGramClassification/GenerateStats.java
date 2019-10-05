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
import java.util.stream.Collectors;

/**
 *
 * @author falmuqhim
 */
public class GenerateStats {

    public int generateCountsStats(String sequenceFile, String outputDirectory) {

        //length and its counts
        LinkedHashMap<Integer, Integer> newSeq = new LinkedHashMap<>();
        BufferedReader reader = null;
        BufferedWriter writer = null;
        List<String> lines = null;

        int size = 0;
        int start = sequenceFile.lastIndexOf("/");
        int end = sequenceFile.lastIndexOf(".");
        String file = "";
        if (start != -1) {
            file = sequenceFile.substring(start + 1, end);
        }

        try {
            reader = new BufferedReader(new FileReader(sequenceFile));
            writer = new BufferedWriter(new FileWriter(outputDirectory + "counts_" + file + ".csv"));
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
            size = lines.size();
            /* 
             * Go throgh each line and find duplicate in the sequence file.
             */
            for (int i = 0; i < lines.size() - 1; i += 2) {
                if (newSeq.containsKey(lines.get(i + 1).length())) {
                    newSeq.put(lines.get(i + 1).length(), newSeq.get(lines.get(i + 1).length()) + 1);
                } else {
                    newSeq.put(lines.get(i + 1).length(), 1);
                }
            }
            try {
                if (writer != null) {
                    writer.append("length");
                    writer.append(",");
                    writer.append("count");
                    writer.newLine();
                }
            } catch (IOException e) {
                System.err.println("Couldn't open the file in directory: " + outputDirectory);
                System.err.println(e.toString());
                // couldn't open the file
            }
            for (Map.Entry<Integer, Integer> entry : newSeq.entrySet()) {
                String key = entry.getKey().toString();
                String value = entry.getValue().toString();
                try {
                    if (writer != null) {
                        writer.append(key);
                        writer.append(",");
                        writer.append(value);
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

        return size;
    }

    public int generateSeqWithLength(String sequenceFile, String outputDirectory) {

        //length and its counts
        LinkedHashMap<Integer, Integer> newSeq = new LinkedHashMap<>();
        BufferedReader reader = null;
        BufferedWriter writer = null;
        List<String> lines = null;

        int size = 0;
        int start = sequenceFile.lastIndexOf("/");
        int end = sequenceFile.lastIndexOf(".");
        String file = "";
        if (start != -1) {
            file = sequenceFile.substring(start + 1, end);
        }

        try {
            reader = new BufferedReader(new FileReader(sequenceFile));
            writer = new BufferedWriter(new FileWriter(outputDirectory + "seq_with_length" + file + ".csv"));
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
            try {
                if (writer != null) {
                    writer.append("sequence");
                    writer.append(",");
                    writer.append("length");
                    writer.newLine();
                }
            } catch (IOException e) {
                System.err.println("Couldn't open the file in directory: " + outputDirectory);
                System.err.println(e.toString());
                // couldn't open the file
            }
            size = lines.size();
            /* 
             * Go throgh each line and find duplicate in the sequence file.
             */
            for (int i = 0; i < lines.size() - 1; i += 2) {
                try {
                    if (writer != null) {
                        writer.append(lines.get(i + 1));
                        writer.append(",");
                        Integer l = lines.get(i + 1).length();
                        writer.append(l.toString());
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

        return size;
    }

    public int generateCountsStats2(String sequenceFile, String outputDirectory) {

        //length and its counts
        LinkedHashMap<Integer, Integer> newSeq = new LinkedHashMap<>();
        BufferedReader reader = null;
        BufferedWriter writer = null;
        List<String> lines = null;

        int size = 0;
        int start = sequenceFile.lastIndexOf("/");
        int end = sequenceFile.lastIndexOf(".");
        String file = "";
        if (start != -1) {
            file = sequenceFile.substring(start + 1, end);
        }

        try {
            reader = new BufferedReader(new FileReader(sequenceFile));
            writer = new BufferedWriter(new FileWriter(outputDirectory + "counts_" + file + ".csv"));
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
            size = lines.size();
            /* 
             * Go throgh each line and find duplicate in the sequence file.
             */
            for (int i = 0; i < lines.size(); i++) {
                if (newSeq.containsKey(lines.get(i).length())) {
                    newSeq.put(lines.get(i).length(), newSeq.get(lines.get(i).length()) + 1);
                } else {
                    newSeq.put(lines.get(i).length(), 1);
                }
            }
            try {
                if (writer != null) {
                    writer.append("length");
                    writer.append(",");
                    writer.append("count");
                    writer.newLine();
                }
            } catch (IOException e) {
                System.err.println("Couldn't open the file in directory: " + outputDirectory);
                System.err.println(e.toString());
                // couldn't open the file
            }
            for (Map.Entry<Integer, Integer> entry : newSeq.entrySet()) {
                String key = entry.getKey().toString();
                String value = entry.getValue().toString();
                try {
                    if (writer != null) {
                        writer.append(value);
                        writer.append(",");
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

        return size;
    }
}
