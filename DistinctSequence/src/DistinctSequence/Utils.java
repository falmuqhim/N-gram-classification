/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DistinctSequence;

import java.io.File;

/**
 *
 * @author falmuqhim
 */
class Utils {
    public final static String txt = "txt";
    public final static String fasta = "fasta";
    /*
     * Get the extension of a file.
     */
    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');
 
        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }
    
   public static String getAllowed() {
       return txt + " - " + fasta;
   }
    
}
