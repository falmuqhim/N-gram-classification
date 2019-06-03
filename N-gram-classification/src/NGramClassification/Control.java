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
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author falmuqhim
 */
public class Control {

    public int doControl(String postiveFile, String negativeFile, String outputDirectory) {

        //read the two files and insert them in an arrayList.
        ArrayList<Sequence> sequences = new ArrayList();

        LinkedHashMap<String, String> newSeq = new LinkedHashMap<>();
        BufferedReader pReader = null;
        BufferedReader nReader = null;
        BufferedWriter writer = null;
        List<String> pLines = null;
        List<String> nLines = null;

        int s = postiveFile.lastIndexOf(".");
        String ext = "";
        if (s != -1) {
            ext = postiveFile.substring(s + 1);
        }

        int pSize = 0;
        int nSize = 0;

        try {
            pReader = new BufferedReader(new FileReader(postiveFile));
            nReader = new BufferedReader(new FileReader(negativeFile));

            if (pReader != null) {
                pLines = pReader.lines().collect(Collectors.toList());
            }
            if (nReader != null) {
                nLines = nReader.lines().collect(Collectors.toList());
            }
        } catch (FileNotFoundException e) {
            System.err.println("Couldn't read the file: " + postiveFile);
            System.err.println("or Couldn't read the file: " + negativeFile);
            System.err.println(e.toString());
            // couldn't read the file
        }
        nSize = nLines.size() / 2;
        pSize = pLines.size() / 2;

        if (pLines != null && nLines != null) {

            /* 
             * Go throgh each line of the two files and add the sequences to 
             * the arrayList
             */
            for (int i = 1; i < pLines.size(); i += 2) {
                sequences.add(new Sequence(pLines.get(i - 1), pLines.get(i)));
            }
            for (int i = 1; i < nLines.size(); i += 2) {
                sequences.add(new Sequence(nLines.get(i - 1), nLines.get(i)));
            }
            System.out.println("pSize: " + pSize);
            System.out.println("nSize: " + nSize);
            System.out.println("ArrayList size: " + sequences.size());

            //shufle the arrayList randomly
            Collections.shuffle(sequences);

            //open writer for controled postiveFile
            try {
                writer = new BufferedWriter(new FileWriter(outputDirectory + "control_postiveFile." + ext));
            } catch (IOException e) {
                System.err.println("Couldn't open the file in directory: " + outputDirectory);
                System.err.println(e.toString());
                // couldn't open the file
            }
            //take the first pSize sequences for postiveFile
            for (int i = 0; i < pSize; i++) {
                try {
                    if (writer != null) {
                        writer.append(sequences.get(i).getId());
                        writer.newLine();
                        writer.append(sequences.get(i).getValue());
                        writer.newLine();
                    }
                } catch (IOException e) {
                    System.err.println("Couldn't open the file in directory: " + outputDirectory);
                    System.err.println(e.toString());
                    // couldn't open the file
                }
            }
            try {
                if (writer != null) {
                    writer.flush();
                    writer.close();
                }
            } catch (IOException e) {
                System.err.println(e.toString());
            }
            
            //open writer for controled negativeFile
            try {
                writer = new BufferedWriter(new FileWriter(outputDirectory + "control_negativeFile." + ext));
            } catch (IOException e) {
                System.err.println("Couldn't open the file in directory: " + outputDirectory);
                System.err.println(e.toString());
                // couldn't open the file
            }
            //take sequences from pSize and the rest for negativeFile
            for (int i = pSize; i < sequences.size(); i++) {
                try {
                    if (writer != null) {
                        writer.append(sequences.get(i).getId());
                        writer.newLine();
                        writer.append(sequences.get(i).getValue());
                        writer.newLine();
                    }
                } catch (IOException e) {
                    System.err.println("Couldn't open the file in directory: " + outputDirectory);
                    System.err.println(e.toString());
                    // couldn't open the file
                }
            }
            try {
                if (writer != null) {
                    writer.flush();
                    writer.close();
                }
            } catch (IOException e) {
                System.err.println(e.toString());
            }
        }
        
        return sequences.size();
    }
    
    public int doControlOneFile(String sequenceFile, String outputDirectory) {

        //read the two files and insert them in an arrayList.
        ArrayList<Sequence> sequences = new ArrayList();

        BufferedReader reader = null;
        BufferedWriter writer = null;
        List<String> lines = null;

        int s = sequenceFile.lastIndexOf("/");
        String file = "";
        if (s != -1) {
            file = sequenceFile.substring(s + 1);
        }

        try {
            reader = new BufferedReader(new FileReader(sequenceFile));

            if (reader != null) {
                lines = reader.lines().collect(Collectors.toList());
            }
        } catch (FileNotFoundException e) {
            System.err.println("Couldn't read the file: " + sequenceFile);
            System.err.println(e.toString());
            // couldn't read the file
        }

        if (lines != null) {

            /* 
             * Go throgh each line of the the sequence file and add the sequences to 
             * the arrayList
             */
            for (int i = 1; i < lines.size(); i += 2) {
                sequences.add(new Sequence(lines.get(i - 1), lines.get(i)));
            }
            System.out.println("ArrayList size: " + sequences.size());

            //shufle the arrayList randomly
            Collections.shuffle(sequences);

            //open writer for controled postiveFile
            try {
                writer = new BufferedWriter(new FileWriter(outputDirectory + "control_" + file));
            } catch (IOException e) {
                System.err.println("Couldn't open the file in directory: " + outputDirectory);
                System.err.println(e.toString());
                // couldn't open the file
            }
            //take the first pSize sequences for postiveFile
            for (int i = 0; i < sequences.size(); i++) {
                try {
                    if (writer != null) {
                        writer.append(sequences.get(i).getId());
                        writer.newLine();
                        writer.append(sequences.get(i).getValue());
                        writer.newLine();
                    }
                } catch (IOException e) {
                    System.err.println("Couldn't open the file in directory: " + outputDirectory);
                    System.err.println(e.toString());
                    // couldn't open the file
                }
            }
            try {
                if (writer != null) {
                    writer.flush();
                    writer.close();
                }
            } catch (IOException e) {
                System.err.println(e.toString());
            }
        }
        
        return sequences.size();
    }
}
