package com.example.currencyconverter;

public class Currency {
    private String name;
    private int nominal;
    private String charCode;
    private double value;

    public Currency(String name, int nominal, String charCode, double value) {
        this.name = name;
        this.nominal = nominal;
        this.charCode = charCode;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNominal() {
        return nominal;
    }

    public void setNominal(int nominal) {
        this.nominal = nominal;
    }

    public String getCharCode() {
        return charCode;
    }

    public void setCharCode(String charCode) {
        this.charCode = charCode;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}