
package com.github.mikephil.charting.buffer;

import java.util.List;

/**
 * Buffer class to boost performance while drawing. Concept: Replace instead of
 * recreate.
 * 
 * @author Philipp Jahoda
 * @param <T> The data the buffer accepts to be fed with.
 */
public abstract class AbstractBuffer<T> {

    /** index in the buffer */
    protected int index = 0;

    /** double-buffer that holds the data points to draw, order: x,y,x,y,... */
    public final double[] buffer;

    /** animation phase x-axis */
    protected double phaseX = 1;

    /** animation phase y-axis */
    protected double phaseY = 1;

    /** indicates from which x-index the visible data begins */
    protected int mFrom = 0;

    /** indicates to which x-index the visible data ranges */
    protected int mTo = 0;

    /**
     * Initialization with buffer-size.
     * 
     * @param size
     */
    public AbstractBuffer(int size) {
        index = 0;
        buffer = new double[size];
    }

    /** limits the drawing on the x-axis */
    public void limitFrom(int from) {
        if (from < 0)
            from = 0;
        mFrom = from;
    }

    /** limits the drawing on the x-axis */
    public void limitTo(int to) {
        if (to < 0)
            to = 0;
        mTo = to;
    }

    /**
     * Resets the buffer index to 0 and makes the buffer reusable.
     */
    public void reset() {
        index = 0;
    }

    /**
     * Returns the size (length) of the buffer array.
     * 
     * @return
     */
    public int size() {
        return buffer.length;
    }

    /**
     * Set the phases used for animations.
     * 
     * @param phaseX
     * @param phaseY
     */
    public void setPhases(double phaseX, double phaseY) {
        this.phaseX = phaseX;
        this.phaseY = phaseY;
    }

    /**
     * Builds up the buffer with the provided data and resets the buffer-index
     * after feed-completion. This needs to run FAST.
     * 
     * @param data
     */
    public abstract void feed(T data);
}
