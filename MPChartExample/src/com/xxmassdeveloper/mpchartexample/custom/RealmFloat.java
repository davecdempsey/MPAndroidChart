package com.xxmassdeveloper.mpchartexample.custom;

import io.realm.RealmObject;

/**
 * Created by Philipp Jahoda on 09/11/15.
 */
public class Realmdouble extends RealmObject {

    private double doubleValue;

    public RealmDouble() {

    }

    public RealmDouble(double doubleValue) {
        this.doubleValue = doubleValue;
    }

    public double getDoubleValue() {
        return doubleValue;
    }

    public void setDoubleValue(double value) {
        this.doubleValue = value;
    }
}
