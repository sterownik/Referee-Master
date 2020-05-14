package com.example.refereemaster;

public class UserName {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String name;
    public int punktypierwszagra;
    public int punktydrugagra;
    public int exp;
    public int punktytrzeciagra;
    public int punktyczwartagra;
    public int liczbabonusów;
    public String data;
    public int liczbaileegzamin;
    public int pierwszytest;
    public int drugitest;

    public int getDrugitest() {
        return drugitest;
    }

    public void setDrugitest(int drugitest) {
        this.drugitest = drugitest;
    }


    public int getPierwszytest() {
        return pierwszytest;
    }

    public void setPierwszytest(int pierwszytest) {
        this.pierwszytest = pierwszytest;
    }

    public int getLiczbaileegzamin() {
        return liczbaileegzamin;
    }

    public void setLiczbaileegzamin(int liczbaileegzamin) {
        this.liczbaileegzamin = liczbaileegzamin;
    }

    public String getData()
    {
        return data;
    }
    public void setData(String data)
    {
        this.data = data;
    }

    public int getLiczbabonusów() {
        return liczbabonusów;
    }

    public void setLiczbabonusów(int liczbabonusów) {
        this.liczbabonusów = liczbabonusów;
    }

    public int getPunktyczwartagra() {
        return punktyczwartagra;
    }

    public void setPunktyczwartagra(int punktyczwartagra) {
        this.punktyczwartagra = punktyczwartagra;
    }

    public int getPunktytrzeciagra() {
        return punktytrzeciagra;
    }
    public void setPunktytrzeciagra(int punktytrzeciagra)
    {
        this.punktytrzeciagra=punktytrzeciagra;
    }

    public int getExp()
    {
        return exp;
    }
    public void setExp(int exp)
    {
        this.exp=exp;
    }

    public int getPunktypierwszagra() {
        return punktypierwszagra;
    }

    public void setPunktypierwszagra(int punktypierwszagra) {
        this.punktypierwszagra = punktypierwszagra;
    }

    public  UserName(String name,int punktypierwszagra, int exp,int punktydrugagra,int punktytrzeciagra,int punktyczwartagra,int liczbabonusów,String data,int liczbaileegzamin,int pierwszytest,int drugitest )
    {
        this.punktydrugagra=punktydrugagra;
        this.name=name;
        this.punktypierwszagra=punktypierwszagra;
        this.exp=exp;
        this.punktytrzeciagra=punktytrzeciagra;
        this.punktyczwartagra=punktyczwartagra;
        this.liczbabonusów=liczbabonusów;
        this.data =data;
        this.liczbaileegzamin=liczbaileegzamin;
        this.pierwszytest=pierwszytest;
        this.drugitest=drugitest;
    }
    public  UserName(int punktypierwszagra, int exp )
    {

        this.punktypierwszagra=punktypierwszagra;
        this.exp=exp;
    }
}
