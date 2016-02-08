package com.example.venomaddiction.cameratest;

/**
 * Created by batsal on 2/7/16.
 */
public class MeaningList {
    private int id;
    private String meaning;
    private String word_type;
    private String similar;
    private String antonym;
    private String see_also;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public String getWord_type() {
        return word_type;
    }

    public void setWord_type(String word_type) {
        this.word_type = word_type;
    }

    public String getSimilar() {
        return similar;
    }

    public void setSimilar(String similar) {
        this.similar = similar;
    }

    public String getAntonym() {
        return antonym;
    }

    public void setAntonym(String antonym) {
        this.antonym = antonym;
    }

    public String getSee_also() {
        return see_also;
    }

    public void setSee_also(String see_also) {
        this.see_also = see_also;
    }
}
