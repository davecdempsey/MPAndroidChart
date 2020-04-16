package com.github.mikephil.charting.data;

import android.annotation.SuppressLint;

/**
 * Created by philipp on 13/06/16.
 */
@SuppressLint("ParcelCreator")
public class RadarEntry extends Entry {

    public RadarEntry(double value) {
        super(0, value);
    }

    public RadarEntry(double value, Object data) {
        super(0, value, data);
    }

    /**
     * This is the same as getY(). Returns the value of the RadarEntry.
     *
     * @return
     */
    public double getValue() {
        return getY();
    }

    public RadarEntry copy() {
        RadarEntry e = new RadarEntry(getY(), getData());
        return e;
    }

    @Deprecated
    @Override
    public void setX(double x) {
        super.setX(x);
    }

    @Deprecated
    @Override
    public double getX() {
        return super.getX();
    }
}
