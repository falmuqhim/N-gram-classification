/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DistinctSequence;

import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.*;
/**
 *
 * @author falmuqhim
 */
public class Filter extends FileFilter {
 
    //Accept all directories and all gif, jpg, tiff, or png files.
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }
 
        String extension =  Utils.getExtension(f);
        if (extension != null) {
            if (extension.equals(Utils.fasta) ||
                extension.equals(Utils.txt)) {
                    return true;
            } else {
                return false;
            }
        }
 
        return false;
    }
 
    //The description of this filter
    public String getDescription() {
        return "Allowed: " + Utils.getAllowed();
    }
}
