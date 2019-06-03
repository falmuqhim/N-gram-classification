/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NGramClassification;

/**
 *
 * @author falmuqhim
 */
public class Sequence {
    String id;
    String value;

    public Sequence() {
    }

    public Sequence(String id, String value) {
        this.id = id;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if ( obj instanceof Sequence) {
            return this.value.equals(((Sequence)obj).value);
        } else 
            return false;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "ID: " + id + "\t Value: " + value;
    }
    
}
