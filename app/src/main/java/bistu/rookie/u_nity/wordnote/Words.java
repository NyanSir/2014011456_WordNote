package bistu.rookie.u_nity.wordnote;

import java.io.Serializable;

public class Words implements Serializable {
    private String word = "";
    private String meaning = "";
    private String sample = "";

    public Words(String word, String meaning, String sample){
        this.word = word;
        this.meaning = meaning;
        this.sample = sample;
    }

    public String getWord() {
        return word;
    }

    public String getMeaning() {
        return meaning;
    }

    public String getSample() {
        return sample;
    }

}