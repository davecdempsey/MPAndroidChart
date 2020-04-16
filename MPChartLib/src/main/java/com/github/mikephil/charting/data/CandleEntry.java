
package com.github.mikephil.charting.data;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;

/**
 * Subclass of Entry that holds all values for one entry in a CandleStickChart.
 * 
 * @author Philipp Jahoda
 */
@SuppressLint("ParcelCreator")
public class CandleEntry extends Entry {

    /** shadow-high value */
    private double mShadowHigh = 0;

    /** shadow-low value */
    private double mShadowLow = 0;

    /** close value */
    private double mClose = 0;

    /** open value */
    private double mOpen = 0;

    /**
     * Constructor.
     * 
     * @param x The value on the x-axis
     * @param shadowH The (shadow) high value
     * @param shadowL The (shadow) low value
     * @param open The open value
     * @param close The close value
     */
    public CandleEntry(double x, double shadowH, double shadowL, double open, double close) {
        super(x, (shadowH + shadowL) / 2);

        this.mShadowHigh = shadowH;
        this.mShadowLow = shadowL;
        this.mOpen = open;
        this.mClose = close;
    }

    /**
     * Constructor.
     *
     * @param x The value on the x-axis
     * @param shadowH The (shadow) high value
     * @param shadowL The (shadow) low value
     * @param open
     * @param close
     * @param data Spot for additional data this Entry represents
     */
    public CandleEntry(double x, double shadowH, double shadowL, double open, double close,
                       Object data) {
        super(x, (shadowH + shadowL) / 2, data);

        this.mShadowHigh = shadowH;
        this.mShadowLow = shadowL;
        this.mOpen = open;
        this.mClose = close;
    }

    /**
     * Constructor.
     *
     * @param x The value on the x-axis
     * @param shadowH The (shadow) high value
     * @param shadowL The (shadow) low value
     * @param open
     * @param close
     * @param icon Icon image
     */
    public CandleEntry(double x, double shadowH, double shadowL, double open, double close,
                       Drawable icon) {
        super(x, (shadowH + shadowL) / 2, icon);

        this.mShadowHigh = shadowH;
        this.mShadowLow = shadowL;
        this.mOpen = open;
        this.mClose = close;
    }

    /**
     * Constructor.
     *
     * @param x The value on the x-axis
     * @param shadowH The (shadow) high value
     * @param shadowL The (shadow) low value
     * @param open
     * @param close
     * @param icon Icon image
     * @param data Spot for additional data this Entry represents
     */
    public CandleEntry(double x, double shadowH, double shadowL, double open, double close,
                       Drawable icon, Object data) {
        super(x, (shadowH + shadowL) / 2, icon, data);

        this.mShadowHigh = shadowH;
        this.mShadowLow = shadowL;
        this.mOpen = open;
        this.mClose = close;
    }

    /**
     * Returns the overall range (difference) between shadow-high and
     * shadow-low.
     * 
     * @return
     */
    public double getShadowRange() {
        return Math.abs(mShadowHigh - mShadowLow);
    }

    /**
     * Returns the body size (difference between open and close).
     * 
     * @return
     */
    public double getBodyRange() {
        return Math.abs(mOpen - mClose);
    }

    /**
     * Returns the center value of the candle. (Middle value between high and
     * low)
     */
    @Override
    public double getY() {
        return super.getY();
    }

    public CandleEntry copy() {

        CandleEntry c = new CandleEntry(getX(), mShadowHigh, mShadowLow, mOpen,
                mClose, getData());

        return c;
    }

    /**
     * Returns the upper shadows highest value.
     * 
     * @return
     */
    public double getHigh() {
        return mShadowHigh;
    }

    public void setHigh(double mShadowHigh) {
        this.mShadowHigh = mShadowHigh;
    }

    /**
     * Returns the lower shadows lowest value.
     * 
     * @return
     */
    public double getLow() {
        return mShadowLow;
    }

    public void setLow(double mShadowLow) {
        this.mShadowLow = mShadowLow;
    }

    /**
     * Returns the bodys close value.
     * 
     * @return
     */
    public double getClose() {
        return mClose;
    }

    public void setClose(double mClose) {
        this.mClose = mClose;
    }

    /**
     * Returns the bodys open value.
     * 
     * @return
     */
    public double getOpen() {
        return mOpen;
    }

    public void setOpen(double mOpen) {
        this.mOpen = mOpen;
    }
}
