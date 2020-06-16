package com.network.countriesinformation.Entity;

public class Country {
    private String mName;
    private double mPopulation;
    private double mArea;
    private String mCode;

    public Country(String mName, double mPopulation, double mArea,String mCode) {
        this.mName = mName;
        this.mPopulation = mPopulation;
        this.mArea = mArea;
        this.mCode = mCode;
    }

    @Override
    public String toString() {
        return "Country{" +
                "mName='" + mName + '\'' +
                ", mPopulation=" + mPopulation +
                ", mArea=" + mArea +
                ", mCode=" + mCode +
                '}';
    }

    public String getmCode() {
        return mCode;
    }

    public void setmCode(String mCode) {
        this.mCode = mCode;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public double getmPopulation() {
        return mPopulation;
    }

    public void setmPopulation(double mPopulation) {
        this.mPopulation = mPopulation;
    }

    public double getmArea() {
        return mArea;
    }

    public void setmArea(double mArea) {
        this.mArea = mArea;
    }

    public String toJSON() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"geonames\":[{\"population\":\"");
        sb.append(mPopulation);
        sb.append("\",\"countryCode\":\"");
        sb.append(mCode);
        sb.append("\",\"countryName\":\"");
        sb.append(mName);
        sb.append("\",\"countryArea\":\"");
        sb.append(mArea);
        sb.append("\"}]}");
        return sb.toString();
    }
}
