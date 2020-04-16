package com.xxmassdeveloper.mpchartexample.custom;


import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Demo class that encapsulates data stored in realm.io database.
 * This class represents data suitable for all chart-types.
 */
public class RealmDemoData extends RealmObject {

    private double yValue;
    private double xValue;

    private double open, close, high, low;

    private double bubbleSize;

    private RealmList<RealmDouble> stackValues;

    private String someStringField;

    /**
     * label for pie entries
     */
    private String label;

    // ofc there could me more fields here...

    public RealmDemoData() {

    }

    public RealmDemoData(double yValue) {
        this.yValue = yValue;
    }

    public RealmDemoData(double xValue, double yValue) {
        this.xValue = xValue;
        this.yValue = yValue;
    }

    /**
     * Constructor for stacked bars.
     *
     * @param xValue
     * @param stackValues
     */
    public RealmDemoData(double xValue, double[] stackValues) {
        this.xValue = xValue;
        this.stackValues = new RealmList<RealmDouble>();

        for (double val : stackValues) {
            this.stackValues.add(new RealmDouble(val));
        }
    }

    /**
     * Constructor for candles.
     *
     * @param xValue
     * @param high
     * @param low
     * @param open
     * @param close
     */
    public RealmDemoData(double xValue, double high, double low, double open, double close) {
        this.yValue = (high + low) / 2;
        this.high = high;
        this.low = low;
        this.open = open;
        this.close = close;
        this.xValue = xValue;
    }

    /**
     * Constructor for bubbles.
     *
     * @param xValue
     * @param yValue
     * @param bubbleSize
     */
    public RealmDemoData(double xValue, double yValue, double bubbleSize) {
        this.xValue = xValue;
        this.yValue = yValue;
        this.bubbleSize = bubbleSize;
    }

    /**
     * Constructor for pie chart.
     *
     * @param yValue
     * @param label
     */
    public RealmDemoData(double yValue, String label) {
        this.yValue = yValue;
        this.label = label;
    }

    public double getyValue() {
        return yValue;
    }

    public void setyValue(double yValue) {
        this.yValue = yValue;
    }

    public double getxValue() {
        return xValue;
    }

    public void setxValue(double xValue) {
        this.xValue = xValue;
    }

    public RealmList<RealmDouble> getStackValues() {
        return stackValues;
    }

    public void setStackValues(RealmList<RealmDouble> stackValues) {
        this.stackValues = stackValues;
    }

    public double getOpen() {
        return open;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public double getClose() {
        return close;
    }

    public void setClose(double close) {
        this.close = close;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public double getBubbleSize() {
        return bubbleSize;
    }

    public void setBubbleSize(double bubbleSize) {
        this.bubbleSize = bubbleSize;
    }

    public String getSomeStringField() {
        return someStringField;
    }

    public void setSomeStringField(String someStringField) {
        this.someStringField = someStringField;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}