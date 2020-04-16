package com.github.mikephil.charting.data;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;

import com.github.mikephil.charting.highlight.Range;

/**
 * Entry class for the BarChart. (especially stacked bars)
 *
 * @author Philipp Jahoda
 */
@SuppressLint("ParcelCreator")
public class BarEntry extends Entry {

    /**
     * the values the stacked barchart holds
     */
    private double[] mYVals;

    /**
     * the ranges for the individual stack values - automatically calculated
     */
    private Range[] mRanges;

    /**
     * the sum of all negative values this entry (if stacked) contains
     */
    private double mNegativeSum;

    /**
     * the sum of all positive values this entry (if stacked) contains
     */
    private double mPositiveSum;

    /**
     * Constructor for normal bars (not stacked).
     *
     * @param x
     * @param y
     */
    public BarEntry(double x, double y) {
        super(x, y);
    }

    /**
     * Constructor for normal bars (not stacked).
     *
     * @param x
     * @param y
     * @param data - Spot for additional data this Entry represents.
     */
    public BarEntry(double x, double y, Object data) {
        super(x, y, data);
    }

    /**
     * Constructor for normal bars (not stacked).
     *
     * @param x
     * @param y
     * @param icon - icon image
     */
    public BarEntry(double x, double y, Drawable icon) {
        super(x, y, icon);
    }

    /**
     * Constructor for normal bars (not stacked).
     *
     * @param x
     * @param y
     * @param icon - icon image
     * @param data - Spot for additional data this Entry represents.
     */
    public BarEntry(double x, double y, Drawable icon, Object data) {
        super(x, y, icon, data);
    }

    /**
     * Constructor for stacked bar entries. One data object for whole stack
     *
     * @param x
     * @param vals - the stack values, use at least 2
     */
    public BarEntry(double x, double[] vals) {
        super(x, calcSum(vals));

        this.mYVals = vals;
        calcPosNegSum();
        calcRanges();
    }

    /**
     * Constructor for stacked bar entries. One data object for whole stack
     *
     * @param x
     * @param vals - the stack values, use at least 2
     * @param data - Spot for additional data this Entry represents.
     */
    public BarEntry(double x, double[] vals, Object data) {
        super(x, calcSum(vals), data);

        this.mYVals = vals;
        calcPosNegSum();
        calcRanges();
    }

    /**
     * Constructor for stacked bar entries. One data object for whole stack
     *
     * @param x
     * @param vals - the stack values, use at least 2
     * @param icon - icon image
     */
    public BarEntry(double x, double[] vals, Drawable icon) {
        super(x, calcSum(vals), icon);

        this.mYVals = vals;
        calcPosNegSum();
        calcRanges();
    }

    /**
     * Constructor for stacked bar entries. One data object for whole stack
     *
     * @param x
     * @param vals - the stack values, use at least 2
     * @param icon - icon image
     * @param data - Spot for additional data this Entry represents.
     */
    public BarEntry(double x, double[] vals, Drawable icon, Object data) {
        super(x, calcSum(vals), icon, data);

        this.mYVals = vals;
        calcPosNegSum();
        calcRanges();
    }

    /**
     * Returns an exact copy of the BarEntry.
     */
    public BarEntry copy() {

        BarEntry copied = new BarEntry(getX(), getY(), getData());
        copied.setVals(mYVals);
        return copied;
    }

    /**
     * Returns the stacked values this BarEntry represents, or null, if only a single value is represented (then, use
     * getY()).
     *
     * @return
     */
    public double[] getYVals() {
        return mYVals;
    }

    /**
     * Set the array of values this BarEntry should represent.
     *
     * @param vals
     */
    public void setVals(double[] vals) {
        setY(calcSum(vals));
        mYVals = vals;
        calcPosNegSum();
        calcRanges();
    }

    /**
     * Returns the value of this BarEntry. If the entry is stacked, it returns the positive sum of all values.
     *
     * @return
     */
    @Override
    public double getY() {
        return super.getY();
    }

    /**
     * Returns the ranges of the individual stack-entries. Will return null if this entry is not stacked.
     *
     * @return
     */
    public Range[] getRanges() {
        return mRanges;
    }

    /**
     * Returns true if this BarEntry is stacked (has a values array), false if not.
     *
     * @return
     */
    public boolean isStacked() {
        return mYVals != null;
    }

    /**
     * Use `getSumBelow(stackIndex)` instead.
     */
    @Deprecated
    public double getBelowSum(int stackIndex) {
        return getSumBelow(stackIndex);
    }

    public double getSumBelow(int stackIndex) {

        if (mYVals == null)
            return 0;

        double remainder = 0;
        int index = mYVals.length - 1;

        while (index > stackIndex && index >= 0) {
            remainder += mYVals[index];
            index--;
        }

        return remainder;
    }

    /**
     * Reuturns the sum of all positive values this entry (if stacked) contains.
     *
     * @return
     */
    public double getPositiveSum() {
        return mPositiveSum;
    }

    /**
     * Returns the sum of all negative values this entry (if stacked) contains. (this is a positive number)
     *
     * @return
     */
    public double getNegativeSum() {
        return mNegativeSum;
    }

    private void calcPosNegSum() {

        if (mYVals == null) {
            mNegativeSum = 0;
            mPositiveSum = 0;
            return;
        }

        double sumNeg = 0;
        double sumPos = 0;

        for (double f : mYVals) {
            if (f <= 0)
                sumNeg += Math.abs(f);
            else
                sumPos += f;
        }

        mNegativeSum = sumNeg;
        mPositiveSum = sumPos;
    }

    /**
     * Calculates the sum across all values of the given stack.
     *
     * @param vals
     * @return
     */
    private static double calcSum(double[] vals) {

        if (vals == null)
            return 0;

        double sum = 0;

        for (double f : vals)
            sum += f;

        return sum;
    }

    protected void calcRanges() {

        double[] values = getYVals();

        if (values == null || values.length == 0)
            return;

        mRanges = new Range[values.length];

        double negRemain = -getNegativeSum();
        double posRemain = 0;

        for (int i = 0; i < mRanges.length; i++) {

            double value = values[i];

            if (value < 0) {
                mRanges[i] = new Range(negRemain, negRemain - value);
                negRemain -= value;
            } else {
                mRanges[i] = new Range(posRemain, posRemain + value);
                posRemain += value;
            }
        }
    }
}


