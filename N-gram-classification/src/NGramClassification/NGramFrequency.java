/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NGramClassification;

/**
 * This class is to reduce the alphabet and calculate the frequence of the
 * possible grams
 *
 * @author Fahad Almuqhim and Bader Alharbi
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;

class NGramFrequency {

    /**
     * The name of the file for positive dataset (AMP)
     */
    private String positiveFile;

    /**
     * The name of the file for negative dataset (non-AMP)
     */
    private String negativeFile;

    /**
     * The directory of output files
     */
    private String directory;

    /**
     * The name of the file that contains the clusters
     */
    private String alphabetReductionFile;

    /**
     * The name of the allowed string that we can use for reducing the sequence
     */
    private String allowedLetters;

    /**
     * The number of clusters that we have in alphabetReductionFile
     */
    int numberOfCluster;

    /**
     * The list of possible letters to be used for reduction
     */
    private ArrayList<String> possibleLetters;

    /**
     * A hashmap that stores each letter with its reduced letter for each
     * cluster
     */
    private LinkedHashMap<String, String> clusterLookupTable;

    /**
     * The number of letters of each gram
     */
    private final int N;

    /**
     * The number of letters of first chunk of the gram
     */
    private final int firstChunk;

    /**
     * The number of letters of second chunk of the gram
     */
    private final int secondChunk;

    /**
     * The number of letters of to be disregarded from the protein
     */
    private final int disregard;

    /**
     * Indicator to know it is reduced by one-letter gram or two-letter gram
     */
    private boolean twoLetter;

    /**
     * LinkedHashMap to store all the possible grams with length of N
     */
    private static LinkedHashMap<String, Integer> possibleGrams;

    /**
     * The name of generated statistics file positive (AMP)
     */
    private String positiveStatisticsFile;

    /**
     * The name of generated statistics file negative (non-AMP)
     */
    private String negativeStatisticsFile;

    /**
     * Constructor to build the object with the required information
     *
     * @param positiveFile
     * @param negativeFile
     * @param alphabetReductionFile
     * @param allowedLetters
     * @param N
     */
    public NGramFrequency(String positiveFile, String negativeFile, String alphabetReductionFile, String directory, String allowedLetters, int N, int firstChunk, int secondChunk, int disregard) {
        this.positiveFile = positiveFile;
        this.negativeFile = negativeFile;
        this.alphabetReductionFile = alphabetReductionFile;
        this.allowedLetters = allowedLetters;
        this.directory = directory;
        this.N = N;
        this.firstChunk = firstChunk;
        this.secondChunk = secondChunk;
        this.disregard = disregard;

        //It is first assigned to -1, since this value is based on reading the cluster file
        this.numberOfCluster = -1;

        this.twoLetter = false;

        // insitilize our data structure
        this.possibleLetters = new ArrayList<>();
        this.clusterLookupTable = new LinkedHashMap<String, String>();
        possibleGrams = new LinkedHashMap<String, Integer>();
    }

    public static LinkedHashMap<String, Integer> getPossibleGrams() {
        return possibleGrams;
    }

    public LinkedHashMap<String, String> getClusterLookupTable() {
        return this.clusterLookupTable;
    }

    public int getN() {
        return this.N;
    }

    /**
     * This method will return the number of clusters that will be used for the
     * reduction If the number have not been calculated yet, it will open the
     * file and count the number, if not, it will just return the value
     *
     * @return the number of clusters
     */
    private int getNumberOfCluster() {
        if (this.numberOfCluster != -1) {
            return this.numberOfCluster;
        }

        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(alphabetReductionFile));
            this.numberOfCluster = 0;
            this.numberOfCluster = reader.lines().collect(Collectors.toList()).size();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return this.numberOfCluster;
    }

    private ArrayList<String> getPossibleLetters() {
        if (this.possibleLetters.size() > 0) {
            return this.possibleLetters;
        }
        int numberOfCluster = this.getNumberOfCluster();
        int lengthOfAllowedLetters = this.allowedLetters.length();
        /**
         * Convert the allowed letters to array of characters to be easily
         * manipulated
         */
        char[] letters = this.allowedLetters.toCharArray();

        /**
         * The digit to be used in our combination
         */
        int digit = 0;

        /**
         * The allowed letters can not be empty to generate the possible letters
         */
        if (lengthOfAllowedLetters == 0) {
            System.err.println("Allowed letters cannot be empty!");
            this.possibleLetters.clear();
            return this.possibleLetters;
        }

        /**
         * If the number of clusters is more than the number of allowed letters
         * A combination of a letter and a digit will be used used for each
         * letter of each cluster
         *
         */
        if (numberOfCluster > lengthOfAllowedLetters) {
            for (int i = 0; i < numberOfCluster; i++) {
                int index = i % lengthOfAllowedLetters;
                if (index == 0) {
                    digit++;
                }
                this.possibleLetters.add("" + letters[index] + digit);
            }
            this.twoLetter = true;
        } else {
            for (int i = 0; i < numberOfCluster; i++) {
                this.possibleLetters.add("" + letters[i]);
            }
            this.twoLetter = false;
        }
        return this.possibleLetters;
    }

    /**
     * Assign each possible letter with each line in the cluster file
     *
     * @return LinkedHashMap of each letter and its cluster (Ex: 'A': 'B', 'Y':
     * 'B', 'D': 'U', ... etc)
     */
    private LinkedHashMap<String, String> assignEachCluster() {
        this.possibleLetters = getPossibleLetters();

        if (possibleLetters.isEmpty()) {
            System.err.println("Possible letters cannot be empty!");
            return null;
        }
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(alphabetReductionFile));
            String text = null;
            this.numberOfCluster = 0;
            int index = 0; // to get from possible leteers for each line

            while ((text = reader.readLine()) != null) {
                for (char character : text.toCharArray()) {
                    this.clusterLookupTable.put("" + character, this.possibleLetters.get(index));
                }
                index++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {

            }
        }
        return this.clusterLookupTable;
    }

    private static void generatePossibleGrams(final int n, final ArrayList<String> syllables, final String currentWord) {
        if (n == 0) {
            possibleGrams.put(currentWord, 0);
        } else {
            for (int i = 0; i < syllables.size(); i++) {
                generatePossibleGrams(n - 1, syllables, currentWord + syllables.get(i));
            }
        }
    }

    private static void resetCounterForPossibleGrams() {
        for (Map.Entry<String, Integer> entry : possibleGrams.entrySet()) {
            entry.setValue(0);
        }
    }

    /**
     * Reduce the file for the given type (positive or negative), then save the
     * reduced file in the desired directory
     *
     * @param type (positive or negative)
     *
     * @return (status code which are: 1 - success, 2 - can not open protein
     * file, 3 - can not open directory)
     */
    private int reduceFile(String type) {

        int returnCode = 1;
        BufferedReader reader = null;
        BufferedWriter writer = null;
        List<String> lines = null;
        try {
            if (type.equals("positive")) {
                reader = new BufferedReader(new FileReader(positiveFile));
                writer = new BufferedWriter(new FileWriter(directory + "positive_reduced.txt"));
            }
            if (type.equals("negative")) {
                reader = new BufferedReader(new FileReader(negativeFile));
                writer = new BufferedWriter(new FileWriter(directory + "negative_reduced.txt"));
            }
            if (reader != null) {
                lines = reader.lines().collect(Collectors.toList());
            }
        } catch (FileNotFoundException e) {
            System.err.println("Couldn't read the file for type: " + type);
            System.err.println(e.toString());
            // couldn't read the file
            returnCode = 2;
        } catch (IOException e) {
            System.err.println("Couldn't open the file for type: " + type);
            System.err.println(e.toString());
            // couldn't open the file
            returnCode = 3;
        }

        //to check the lines are read correctly
        if (lines != null) {

            /* 
             * Go throgh each line and reduce each protein. The even lines are 
             * ignored because they contain the protiens ids
             */
            for (int i = 1; i < lines.size(); i = i + 2) {
                if (i != 1) {
                    try {
                        if (writer != null) {
                            writer.newLine();
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(NGramFrequency.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                char[] toCharArray = lines.get(i).toCharArray();
                String newProtein = "";
                for (char c : toCharArray) {
                    if (clusterLookupTable.containsKey("" + c)) {
                        newProtein = newProtein + clusterLookupTable.get("" + c);
                    }
                }
                try {
                    if (writer != null) {
                        writer.append(newProtein);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(NGramFrequency.class.getName()).log(Level.SEVERE, null, ex);
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
        return returnCode;
    }

    /**
     * Calculate sequence statistics for the given type (positive or negative),
     * then save the statistics file in the desired directory
     *
     * @param type (positive or negative)
     *
     * @return (status code which are: 1 - success, 2 - can not open reduced
     * file, 3 - can not open directory)
     */
    private int calculateFrequency(String type) {
        int returnCode = 1;

        DecimalFormat decimalFormat = new DecimalFormat("0.####");

        BufferedReader reader = null;
        BufferedWriter writer = null;
        List<String> lines = null;
        String relation = "";
        try {

            /*
             * This is to open the reduced file, and open a new file to write the stats
             * The name for the new file will be based on the name of source file and alphabetReductionFile 
             */
            if (type.equals("positive")) {
                reader = new BufferedReader(new FileReader(directory + "positive_reduced.txt"));
                relation = positiveFile.substring(positiveFile.lastIndexOf('/') + 1, positiveFile.lastIndexOf("."));
                relation = relation + alphabetReductionFile.substring(alphabetReductionFile.lastIndexOf("/") + 1, alphabetReductionFile.lastIndexOf("."));
                writer = new BufferedWriter(new FileWriter(directory + relation + ".csv"));
                positiveStatisticsFile = relation + ".csv";
            }
            if (type.equals("negative")) {
                reader = new BufferedReader(new FileReader(directory + "negative_reduced.txt"));
                relation = negativeFile.substring(negativeFile.lastIndexOf('/') + 1, negativeFile.lastIndexOf("."));
                relation = relation + alphabetReductionFile.substring(alphabetReductionFile.lastIndexOf("/") + 1, alphabetReductionFile.lastIndexOf("."));
                writer = new BufferedWriter(new FileWriter(directory + relation + ".csv"));
                negativeStatisticsFile = relation + ".csv";
            }
            if (reader != null) {
                lines = reader.lines().collect(Collectors.toList());
            }
        } catch (FileNotFoundException e) {
            System.err.println("Couldn't read the file for type: " + type);
            System.err.println(e.toString());
            // couldn't read the file
            returnCode = 2;
        } catch (IOException e) {
            System.err.println("Couldn't open the file for type: " + type);
            System.err.println(e.toString());
            // couldn't open the file
            returnCode = 3;
        }
        /**
         * These lines to writer the header of the CSV file which consists of
         * relation, and attributes
         */
        try {
            if (writer != null) {
                writer.append("@relation " + relation);
                writer.newLine();
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Couldn't open the file for type: " + type);
            System.err.println(e.toString());
            // couldn't open the file
            returnCode = 3;
        }
        for (Map.Entry<String, Integer> entry : possibleGrams.entrySet()) {
            String key = entry.getKey();
            try {
                if (writer != null) {
                    writer.append("@attribute " + key + " real");
                    writer.newLine();
                }
            } catch (IOException e) {
                System.err.println("Couldn't open the file for type: " + type);
                System.err.println(e.toString());
                // couldn't open the file
                returnCode = 3;
            }
        }

        try {
            if (writer != null) {
                writer.append("@attribute isAMP {AMP, Non-AMP}");
                writer.newLine();
                writer.newLine();
                writer.append("@data");
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Couldn't open the file for type: " + type);
            System.err.println(e.toString());
            // couldn't open the file
            returnCode = 3;
        }

        if (lines != null) {
            for (String line : lines) {
                int badCount = 0;
                if (firstChunk == 0) {
                    if (twoLetter) {
                        for (int i = 0; i < line.length() - (N * 2) + 1; i += 2) {
                            String gram = line.substring(i, i + (N * 2));
                            if (!possibleGrams.containsKey(gram)) {
                                badCount++;
                            } else {
                                int count = possibleGrams.get(gram);
                                possibleGrams.put(gram, count + 1);
                            }
                        }
                    } else {
                        for (int i = 0; i < line.length() - N + 1; i++) {
                            String gram = line.substring(i, i + N);
                            if (!possibleGrams.containsKey(gram)) {
                                badCount++;
                            } else {
                                int count = possibleGrams.get(gram);
                                possibleGrams.put(gram, count + 1);
                            }
                        }
                    }
                } else {
                    if (twoLetter) {
                        for (int i = 0; i < line.length() - ((firstChunk + secondChunk + disregard) * 2) + 1; i += 2) {
                            String gram1 = line.substring(i, i + (firstChunk * 2));
                            String dis = line.substring(i + (firstChunk * 2), i + (firstChunk * 2) + (disregard * 2));
                            String gram2 = line.substring(i + (firstChunk * 2) + (disregard * 2), i + (firstChunk * 2) + (disregard * 2) + (secondChunk * 2));

                            String gram = gram1 + gram2;

                            if (!possibleGrams.containsKey(gram)) {
                                badCount++;
                            } else {
                                int count = possibleGrams.get(gram);
                                possibleGrams.put(gram, count + 1);
                            }
                        }
                    } else {
                        for (int i = 0; i < line.length() - (firstChunk + secondChunk + disregard) + 1; i++) {
                            String gram1 = line.substring(i, i + firstChunk);
                            String dis = line.substring(i + firstChunk, i + firstChunk + disregard);
                            String gram2 = line.substring(i + firstChunk + disregard, i + firstChunk + disregard + secondChunk);

                            String gram = gram1 + gram2;
                            if (!possibleGrams.containsKey(gram)) {
                                badCount++;
                            } else {
                                int count = possibleGrams.get(gram);
                                possibleGrams.put(gram, count + 1);
                            }
                        }
                    }
                }

                for (Map.Entry<String, Integer> entry : possibleGrams.entrySet()) {
                    String key = entry.getKey();
                    int value = entry.getValue();
                    try {
                        if (twoLetter) {
                            writer.append(decimalFormat.format(
                                    value / Double.valueOf((line.length() / 2) - badCount - N + 1)
                            ));
                            writer.append(",");
                        } else {
                            writer.append(decimalFormat.format(
                                    value / Double.valueOf(line.length() - badCount - N + 1)
                            ));
                            writer.append(",");
                        }
                    } catch (IOException e) {
                        System.err.println("Couldn't open the file for type: " + type);
                        System.err.println(e.toString());
                        // couldn't open the file
                        returnCode = 3;
                    }
                }

                if (type.equals("positive")) {
                    try {
                        writer.append("AMP");
                        writer.newLine();
                    } catch (IOException e) {
                        System.err.println("Couldn't open the file for type: " + type);
                        System.err.println(e.toString());
                        // couldn't open the file
                        returnCode = 3;
                    }
                }
                if (type.equals("negative")) {
                    try {
                        writer.append("Non-AMP");
                        writer.newLine();
                    } catch (IOException e) {
                        System.err.println("Couldn't open the file for type: " + type);
                        System.err.println(e.toString());
                        // couldn't open the file
                        returnCode = 3;
                    }
                }
                resetCounterForPossibleGrams();
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
        return returnCode;
    }

    /**
     * Calculate sequence statistics for the given type (positive or negative),
     * then save the statistics file in the desired directory This is an advance
     * calculation which you can specify what to cary and what to leave.
     *
     * @param type (positive or negative)
     * @param disregardList sizes of disregard chunks
     * @param takeList sizes of take chunks
     *
     * @return (status code which are: 1 - success, 2 - can not open reduced
     * file, 3 - can not open directory)
     */
    private int calculateFrequencyAdvance(String type, ArrayList<Integer> disregardList, ArrayList<Integer> takeList) {
        int returnCode = 1;

        DecimalFormat decimalFormat = new DecimalFormat("0.####");

        BufferedReader reader = null;
        BufferedWriter writer = null;
        List<String> lines = null;
        String relation = "";
        try {

            /*
             * This is to open the reduced file, and open a new file to write the stats
             * The name for the new file will be based on the name of source file and alphabetReductionFile 
             */
            if (type.equals("positive")) {
                reader = new BufferedReader(new FileReader(directory + "positive_reduced.txt"));
                relation = positiveFile.substring(positiveFile.lastIndexOf('/') + 1, positiveFile.lastIndexOf("."));
                relation = relation + alphabetReductionFile.substring(alphabetReductionFile.lastIndexOf("/") + 1, alphabetReductionFile.lastIndexOf("."));
                writer = new BufferedWriter(new FileWriter(directory + relation + ".csv"));
                positiveStatisticsFile = relation + ".csv";
            }
            if (type.equals("negative")) {
                reader = new BufferedReader(new FileReader(directory + "negative_reduced.txt"));
                relation = negativeFile.substring(negativeFile.lastIndexOf('/') + 1, negativeFile.lastIndexOf("."));
                relation = relation + alphabetReductionFile.substring(alphabetReductionFile.lastIndexOf("/") + 1, alphabetReductionFile.lastIndexOf("."));
                writer = new BufferedWriter(new FileWriter(directory + relation + ".csv"));
                negativeStatisticsFile = relation + ".csv";
            }
            if (reader != null) {
                lines = reader.lines().collect(Collectors.toList());
            }
        } catch (FileNotFoundException e) {
            System.err.println("Couldn't read the file for type: " + type);
            System.err.println(e.toString());
            // couldn't read the file
            returnCode = 2;
        } catch (IOException e) {
            System.err.println("Couldn't open the file for type: " + type);
            System.err.println(e.toString());
            // couldn't open the file
            returnCode = 3;
        }
        /**
         * These lines to writer the header of the CSV file which consists of
         * relation, and attributes
         */
        try {
            if (writer != null) {
                writer.append("@relation " + relation);
                writer.newLine();
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Couldn't open the file for type: " + type);
            System.err.println(e.toString());
            // couldn't open the file
            returnCode = 3;
        }
        for (Map.Entry<String, Integer> entry : possibleGrams.entrySet()) {
            String key = entry.getKey();
            try {
                if (writer != null) {
                    writer.append("@attribute " + key + " real");
                    writer.newLine();
                }
            } catch (IOException e) {
                System.err.println("Couldn't open the file for type: " + type);
                System.err.println(e.toString());
                // couldn't open the file
                returnCode = 3;
            }
        }

        try {
            if (writer != null) {
                writer.append("@attribute isAMP {AMP, Non-AMP}");
                writer.newLine();
                writer.newLine();
                writer.append("@data");
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Couldn't open the file for type: " + type);
            System.err.println(e.toString());
            // couldn't open the file
            returnCode = 3;
        }

        if (lines != null) {
            for (String line : lines) {
                int badCount = 0;
                int disregardSum = disregardList.stream().mapToInt(Integer::intValue).sum();
                int takeSum = takeList.stream().mapToInt(Integer::intValue).sum();
                if (twoLetter) {
                    for (int i = 0; i < line.length() - ((firstChunk + takeSum + disregardSum) * 2) + 1; i += 2) {
                        String gram = "";
                        gram = line.substring(i, i + (firstChunk * 2));
                        int soFar = i + firstChunk*2;
                        for(int j=0; j<takeList.size(); j++) {
                            String dis = line.substring(soFar, soFar + disregardList.get(j)*2);
                            soFar+= disregardList.get(j)*2;
                            gram += line.substring(soFar, soFar + takeList.get(j)*2);
                            soFar += takeList.get(j)*2;
                        }
                        if (!possibleGrams.containsKey(gram)) {
                            badCount++;
                        } else {
                            int count = possibleGrams.get(gram);
                            possibleGrams.put(gram, count + 1);
                        }
                    }
                } else {
                    for (int i = 0; i < line.length() - (firstChunk + takeSum + disregardSum) + 1; i++) {
                        String gram = "";
                        gram = line.substring(i, i + firstChunk);
                        int soFar = i + firstChunk;
                        for(int j=0; j<takeList.size(); j++) {
                            String dis = line.substring(soFar, soFar + disregardList.get(j));
                            soFar+= disregardList.get(j);
                            gram += line.substring(soFar, soFar + takeList.get(j));
                            soFar += takeList.get(j);
                        }
                        if (!possibleGrams.containsKey(gram)) {
                            badCount++;
                        } else {
                            int count = possibleGrams.get(gram);
                            possibleGrams.put(gram, count + 1);
                        }
                    }
                }
                for (Map.Entry<String, Integer> entry : possibleGrams.entrySet()) {
                    String key = entry.getKey();
                    int value = entry.getValue();
                    try {
                        if (twoLetter) {
                            writer.append(decimalFormat.format(
                                    value / Double.valueOf((line.length() / 2) - badCount - N + 1)
                            ));
                            writer.append(",");
                        } else {
                            writer.append(decimalFormat.format(
                                    value / Double.valueOf(line.length() - badCount - N + 1)
                            ));
                            writer.append(",");
                        }
                    } catch (IOException e) {
                        System.err.println("Couldn't open the file for type: " + type);
                        System.err.println(e.toString());
                        // couldn't open the file
                        returnCode = 3;
                    }
                }

                if (type.equals("positive")) {
                    try {
                        writer.append("AMP");
                        writer.newLine();
                    } catch (IOException e) {
                        System.err.println("Couldn't open the file for type: " + type);
                        System.err.println(e.toString());
                        // couldn't open the file
                        returnCode = 3;
                    }
                }
                if (type.equals("negative")) {
                    try {
                        writer.append("Non-AMP");
                        writer.newLine();
                    } catch (IOException e) {
                        System.err.println("Couldn't open the file for type: " + type);
                        System.err.println(e.toString());
                        // couldn't open the file
                        returnCode = 3;
                    }
                }
                resetCounterForPossibleGrams();
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
        return returnCode;
    }

    /**
     * Combine the two output files of sequence statistics in one arff file
     *
     * @return (status code which are: 1 - success, 2 - can not open statistics
     * files, 3 - can not open directory)
     */
    private int combineTwoTypes() {
        int returnCode = 1;

        BufferedReader positiveReader = null;
        BufferedReader negativeReader = null;
        BufferedWriter writer = null;
        List<String> positiveLines = null;
        List<String> negativeLines = null;

        try {
            positiveReader = new BufferedReader(new FileReader(directory + positiveStatisticsFile));
            negativeReader = new BufferedReader(new FileReader(directory + negativeStatisticsFile));

            if (positiveReader != null) {
                positiveLines = positiveReader.lines().collect(Collectors.toList());
            }
            if (negativeReader != null) {
                negativeLines = negativeReader.lines().collect(Collectors.toList());
            }

            String outputFileName = positiveStatisticsFile.substring(0, positiveStatisticsFile.lastIndexOf("."));
            outputFileName = outputFileName + "_" + negativeStatisticsFile.substring(0, negativeStatisticsFile.lastIndexOf("."));

            writer = new BufferedWriter(new FileWriter(directory + outputFileName + ".arff"));

            if (positiveLines != null) {
                for (String line : positiveLines) {
                    writer.append(line);
                    writer.newLine();
                }
            }

            boolean start = false;
            if (negativeLines != null) {
                for (String line : negativeLines) {
                    if (line.startsWith("@data")) {
                        start = true;
                        continue;
                    }
                    if (start) {
                        writer.append(line);
                        writer.newLine();
                    }
                }
            }

        } catch (FileNotFoundException e) {
            System.err.println("Couldn't read the file");
            System.err.println(e.toString());
            // couldn't read the file
            returnCode = 2;
        } catch (IOException e) {
            System.err.println("Couldn't open the file");
            System.err.println(e.toString());
            // couldn't open the file
            returnCode = 3;
        }
        try {
            if (positiveReader != null) {
                positiveReader.close();
            }
            if (negativeReader != null) {
                negativeReader.close();
            }
            if (writer != null) {
                writer.flush();
                writer.close();
            }
        } catch (IOException e) {
            System.err.println(e.toString());
        }
        return returnCode;
    }

    /**
     * This method is to combine all of our work in one call to make it clear
     * for debugging
     *
     * @param progressBar used to output the progress of the process in GUI
     * @param outputStatus used to output the status of the process in GUI
     *
     * @return
     */
    public int doProcess(JProgressBar progressBar, JTextArea outputStatus) {
        int returnCode = 1;
        int all = 7;
        int p = 0;

        //Assign each letter in the cluster file to one of the possible letters
        assignEachCluster();
        if (progressBar != null && outputStatus != null) {
            progressBar.setValue((++p / all) * 100);
            outputStatus.append("Each letter has been assign to a possible letter\n");
        }

        //generate possible combinations of N grams
        generatePossibleGrams(N, possibleLetters, "");

        if (progressBar != null && outputStatus != null) {
            progressBar.setValue((++p / all) * 100);
            outputStatus.append("Possible combinations of N Grams have been generated\n");
        }

        //reduce protine file for positive
        reduceFile("positive");
        if (progressBar != null && outputStatus != null) {
            progressBar.setValue((++p / all) * 100);
            outputStatus.append("Positive file has been reduced and saved in the given directory\n");
        }

        //reduce protine file for negative
        reduceFile("negative");
        if (progressBar != null && outputStatus != null) {
            progressBar.setValue((++p / all) * 100);
            outputStatus.append("Negative file has been reduced and saved in the given directory\n");
        }

        //find statistics for postive protine
        calculateFrequency("positive");
        if (progressBar != null && outputStatus != null) {
            progressBar.setValue((++p / all) * 100);
            outputStatus.append("Positive file statistics have been calculated and saved in the given directory\n");
        }

        //find statistics for negative protine
        calculateFrequency("negative");
        if (progressBar != null && outputStatus != null) {
            progressBar.setValue((++p / all) * 100);
            outputStatus.append("Negative file statistics have been calculated and saved in the given directory\n");
        }

        //combine the two csv files
        combineTwoTypes();
        if (progressBar != null && outputStatus != null) {
            progressBar.setValue((++p / all) * 100);
            outputStatus.append("Two statictics files have been combined in one arff file and saved in the given directory\n");
        }
        return returnCode;
    }
    
    /**
     * This method is to combine all of our work in one call to make it clear
     * for debugging. This is for the advance process
     *
     * @param progressBar used to output the progress of the process in GUI
     * @param outputStatus used to output the status of the process in GUI
     * @param disregardList sizes of disregard chunks
     * @param takeList sizes of take chunks
     *
     * @return
     */
    public int doProcessAdvance(JProgressBar progressBar, JTextArea outputStatus, ArrayList<Integer> disregardList, ArrayList<Integer> takeList) {
        int returnCode = 1;
        int all = 7;
        int p = 0;

        //Assign each letter in the cluster file to one of the possible letters
        assignEachCluster();
        if (progressBar != null && outputStatus != null) {
            progressBar.setValue((++p / all) * 100);
            outputStatus.append("Each letter has been assign to a possible letter\n");
        }

        //generate possible combinations of N grams
        generatePossibleGrams(N, possibleLetters, "");

        if (progressBar != null && outputStatus != null) {
            progressBar.setValue((++p / all) * 100);
            outputStatus.append("Possible combinations of N Grams have been generated\n");
        }

        //reduce protine file for positive
        reduceFile("positive");
        if (progressBar != null && outputStatus != null) {
            progressBar.setValue((++p / all) * 100);
            outputStatus.append("Positive file has been reduced and saved in the given directory\n");
        }

        //reduce protine file for negative
        reduceFile("negative");
        if (progressBar != null && outputStatus != null) {
            progressBar.setValue((++p / all) * 100);
            outputStatus.append("Negative file has been reduced and saved in the given directory\n");
        }

        //find statistics for postive protine
        calculateFrequencyAdvance("positive", disregardList, takeList);
        if (progressBar != null && outputStatus != null) {
            progressBar.setValue((++p / all) * 100);
            outputStatus.append("Positive file statistics have been calculated and saved in the given directory\n");
        }

        //find statistics for negative protine
        calculateFrequencyAdvance("negative", disregardList, takeList);
        if (progressBar != null && outputStatus != null) {
            progressBar.setValue((++p / all) * 100);
            outputStatus.append("Negative file statistics have been calculated and saved in the given directory\n");
        }

        //combine the two csv files
        combineTwoTypes();
        if (progressBar != null && outputStatus != null) {
            progressBar.setValue((++p / all) * 100);
            outputStatus.append("Two statictics files have been combined in one arff file and saved in the given directory\n");
        }
        return returnCode;
    }
}
