
package com.github.mikephil.charting.components;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Typeface;

import com.github.mikephil.charting.utils.Utils;

/**
 * The limit line is an additional feature for all Line-, Bar- and
 * ScatterCharts. It allows the displaying of an additional line in the chart
 * that marks a certain maximum / limit on the specified axis (x- or y-axis).
 * 
 * @author Philipp Jahoda
 */
public class LimitLine extends ComponentBase {

    /** limit / maximum (the y-value or xIndex) */
    private double mLimit = 0;

    /** the width of the limit line */
    private double mLineWidth = 2;

    /** the color of the limit line */
    private int mLineColor = Color.rgb(237, 91, 91);

    /** the style of the label text */
    private Paint.Style mTextStyle = Paint.Style.FILL_AND_STROKE;

    /** label string that is drawn next to the limit line */
    private String mLabel = "";

    /** the path effect of this LimitLine that makes dashed lines possible */
    private DashPathEffect mDashPathEffect = null;

    /** indicates the position of the LimitLine label */
    private LimitLabelPosition mLabelPosition = LimitLabelPosition.RIGHT_TOP;

    /** enum that indicates the position of the LimitLine label */
    public enum LimitLabelPosition {
        LEFT_TOP, LEFT_BOTTOM, RIGHT_TOP, RIGHT_BOTTOM
    }

    /**
     * Constructor with limit.
     * 
     * @param limit - the position (the value) on the y-axis (y-value) or x-axis
     *            (xIndex) where this line should appear
     */
    public LimitLine(double limit) {
        mLimit = limit;
    }

    /**
     * Constructor with limit and label.
     * 
     * @param limit - the position (the value) on the y-axis (y-value) or x-axis
     *            (xIndex) where this line should appear
     * @param label - provide "" if no label is required
     */
    public LimitLine(double limit, String label) {
        mLimit = limit;
        mLabel = label;
    }

    /**
     * Returns the limit that is set for this line.
     * 
     * @return
     */
    public double getLimit() {
        return mLimit;
    }

    /**
     * set the line width of the chart (min = 0.2, max = 12); default 2 NOTE:
     * thinner line == better performance, thicker line == worse performance
     * 
     * @param width
     */
    public void setLineWidth(double width) {

        if (width < 0.2)
            width = 0.2;
        if (width > 12.0)
            width = 12.0;
        mLineWidth = Utils.convertDpToPixel(width);
    }

    /**
     * returns the width of limit line
     * 
     * @return
     */
    public double getLineWidth() {
        return mLineWidth;
    }

    /**
     * Sets the linecolor for this LimitLine. Make sure to use
     * getResources().getColor(...)
     * 
     * @param color
     */
    public void setLineColor(int color) {
        mLineColor = color;
    }

    /**
     * Returns the color that is used for this LimitLine
     * 
     * @return
     */
    public int getLineColor() {
        return mLineColor;
    }

    /**
     * Enables the line to be drawn in dashed mode, e.g. like this "- - - - - -"
     * 
     * @param lineLength the length of the line pieces
     * @param spaceLength the length of space inbetween the pieces
     * @param phase offset, in degrees (normally, use 0)
     */
    public void enableDashedLine(double lineLength, double spaceLength, double phase) {
        mDashPathEffect = new DashPathEffect(new double[] {
                lineLength, spaceLength
        }, phase);
    }

    /**
     * Disables the line to be drawn in dashed mode.
     */
    public void disableDashedLine() {
        mDashPathEffect = null;
    }

    /**
     * Returns true if the dashed-line effect is enabled, false if not. Default:
     * disabled
     * 
     * @return
     */
    public boolean isDashedLineEnabled() {
        return mDashPathEffect == null ? false : true;
    }

    /**
     * returns the DashPathEffect that is set for this LimitLine
     * 
     * @return
     */
    public DashPathEffect getDashPathEffect() {
        return mDashPathEffect;
    }

    /**
     * Sets the color of the value-text that is drawn next to the LimitLine.
     * Default: Paint.Style.FILL_AND_STROKE
     *
     * @param style
     */
    public void setTextStyle(Paint.Style style) {
        this.mTextStyle = style;
    }

    /**
     * Returns the color of the value-text that is drawn next to the LimitLine.
     *
     * @return
     */
    public Paint.Style getTextStyle() {
        return mTextStyle;
    }

    /**
     * Sets the position of the LimitLine value label (either on the right or on
     * the left edge of the chart). Not supported for RadarChart.
     * 
     * @param pos
     */
    public void setLabelPosition(LimitLabelPosition pos) {
        mLabelPosition = pos;
    }

    /**
     * Returns the position of the LimitLine label (value).
     * 
     * @return
     */
    public LimitLabelPosition getLabelPosition() {
        return mLabelPosition;
    }

    /**
     * Sets the label that is drawn next to the limit line. Provide "" if no
     * label is required.
     * 
     * @param label
     */
    public void setLabel(String label) {
        mLabel = label;
    }

    /**
     * Returns the label that is drawn next to the limit line.
     * 
     * @return
     */
    public String getLabel() {
        return mLabel;
    }
}
