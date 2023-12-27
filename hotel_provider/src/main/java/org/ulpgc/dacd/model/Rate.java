package org.ulpgc.dacd.model;

public class Rate {

    private String code;
    private String rateName;
    private double rate;
    private double tax;

    public Rate(String code, String rateName, double rate, double tax) {
        this.code = code;
        this.rateName = rateName;
        this.rate = rate;
        this.tax = tax;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return rateName;
    }

    public double getRate() {
        return rate;
    }

    public double getTax() {
        return tax;
    }

}
