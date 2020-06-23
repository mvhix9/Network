package com.network.countriesinformation.Entity;

import android.os.Parcel;
import android.os.Parcelable;

public class Country implements Comparable<Country>, Parcelable {
    private String mName;
    private double mPopulation;
    private double mArea;
    private String mCode;
    private String mContinentName;
    private String mCurrencyCode;
    private String mCapital;

    public String getmContinentName() {
        return mContinentName;
    }

    public void setmContinentName(String mContinentName) {
        this.mContinentName = mContinentName;
    }

    public String getmCurrencyCode() {
        return mCurrencyCode;
    }

    public void setmCurrencyCode(String mCurrencyCode) {
        this.mCurrencyCode = mCurrencyCode;
    }

    public String getmCapital() {
        return mCapital;
    }

    public void setmCapital(String mCapital) {
        this.mCapital = mCapital;
    }

    public Country(String mName, double mPopulation, double mArea, String mCode, String mContinentName, String mCurrencyCode, String mCapital) {
        this.mName = mName;
        this.mPopulation = mPopulation;
        this.mArea = mArea;
        this.mCode = mCode;
        this.mContinentName = mContinentName;
        this.mCurrencyCode = mCurrencyCode;
        this.mCapital = mCapital;
    }

    @Override
    public String toString() {
        return "Country{" +
                "mName='" + mName + '\'' +
                ", mPopulation=" + mPopulation +
                ", mArea=" + mArea +
                ", mCode='" + mCode + '\'' +
                ", mContinentName='" + mContinentName + '\'' +
                ", mCurrencyCode='" + mCurrencyCode + '\'' +
                ", mCapital='" + mCapital + '\'' +
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
        sb.append("\",\"currencyName\":\"");
        sb.append(mContinentName);
        sb.append("\",\"continentName\":\"");
        sb.append(mContinentName);
        sb.append("\",\"capital\":\"");
        sb.append(mCapital);
        sb.append("\"}]}");
        return sb.toString();
    }

    @Override
    public int compareTo(Country o) {
        return this.getmName().compareTo(o.getmName());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mCapital);
        dest.writeString(mCode);
        dest.writeString(mContinentName);
        dest.writeString(mCurrencyCode);
        dest.writeString(mName);
        dest.writeDouble(mArea);
        dest.writeDouble(mPopulation);
    }

    public Country(Parcel in)
    {
        this.mCapital = in.readString();
        this.mCode = in.readString();
        this.mContinentName = in.readString();
        this.mCurrencyCode = in.readString();
        this.mName = in.readString();
        this.mArea = in.readDouble();
        this.mPopulation = in.readDouble();

    }
}
