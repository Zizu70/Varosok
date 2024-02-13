package com.example.varosok;

public class Varos {

    private int id;
    private String varosNev;
    private String orszag;
    private int lakossag;

    public Varos(int id, String varosNev, String orszag, int lakossag) {
        this.id = id;
        this.varosNev = varosNev;
        this.orszag = orszag;
        this.lakossag = lakossag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVaros() {
        return varosNev;
    }

    public void setVaros(String varos) {
        this.varosNev = varos;
    }
    public String getOrszag() {
        return orszag;
    }

    public void setOrszag(String orszag) {
        this.orszag = orszag;
    }

    public int getLakossag() {
        return lakossag;
    }

    public void setLakossag(int lakossag) {
        this.lakossag = lakossag;
    }

}
