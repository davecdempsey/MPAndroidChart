
package com.github.mikephil.charting.data;

import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;
import com.github.mikephil.charting.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class PieDataSet extends DataSet<PieEntry> implements IPieDataSet {

    /** the space in pixels between the chart-slices, default 0 */
    private double mSliceSpace = 0;
    private boolean mAutomaticallyDisableSliceSpacing;

    /** indicates the selection distance of a pie slice */
    private double mShift = 18;

    private ValuePosition mXValuePosition = ValuePosition.INSIDE_SLICE;
    private ValuePosition mYValuePosition = ValuePosition.INSIDE_SLICE;
    private boolean mUsingSliceColorAsValueLineColor = false;
    private int mValueLineColor = 0xff000000;
    private double mValueLineWidth = 1.0;
    private double mValueLinePart1OffsetPercentage = 75.f;
    private double mValueLinePart1Length = 0.3;
    private double mValueLinePart2Length = 0.4;
    private boolean mValueLineVariableLength = true;

    public PieDataSet(List<PieEntry> yVals, String label) {
        super(yVals, label);
//        mShift = Utils.convertDpToPixel(12);
    }

    @Override
    public DataSet<PieEntry> copy() {

        List<PieEntry> yVals = new ArrayList<>();

        for (int i = 0; i < mValues.size(); i++) {
            yVals.add(mValues.get(i).copy());
        }

        PieDataSet copied = new PieDataSet(yVals, getLabel());
        copied.mColors = mColors;
        copied.mSliceSpace = mSliceSpace;
        copied.mShift = mShift;
        return copied;
    }

    @Override
    protected void calcMinMax(PieEntry e) {

        if (e == null)
            return;

        calcMinMaxY(e);
    }

    /**
     * Sets the space that is left out between the piechart-slices in dp.
     * Default: 0 --> no space, maximum 20
     *
     * @param spaceDp
     */
    public void setSliceSpace(double spaceDp) {

        if (spaceDp > 20)
            spaceDp = 20;
        if (spaceDp < 0)
            spaceDp = 0;

        mSliceSpace = Utils.convertDpToPixel(spaceDp);
    }

    @Override
    public double getSliceSpace() {
        return mSliceSpace;
    }

    /**
     * When enabled, slice spacing will be 0.0 when the smallest value is going to be
     *   smaller than the slice spacing itself.
     *
     * @param autoDisable
     */
    public void setAutomaticallyDisableSliceSpacing(boolean autoDisable) {
        mAutomaticallyDisableSliceSpacing = autoDisable;
    }

    /**
     * When enabled, slice spacing will be 0.0 when the smallest value is going to be
     *   smaller than the slice spacing itself.
     *
     * @return
     */
    @Override
    public boolean isAutomaticallyDisableSliceSpacingEnabled() {
        return mAutomaticallyDisableSliceSpacing;
    }

    /**
     * sets the distance the highlighted piechart-slice of this DataSet is
     * "shifted" away from the center of the chart, default 12
     * 
     * @param shift
     */
    public void setSelectionShift(double shift) {
        mShift = Utils.convertDpToPixel(shift);
    }

    @Override
    public double getSelectionShift() {
        return mShift;
    }

    @Override
    public ValuePosition getXValuePosition()
    {
        return mXValuePosition;
    }

    public void setXValuePosition(ValuePosition xValuePosition)
    {
        this.mXValuePosition = xValuePosition;
    }

    @Override
    public ValuePosition getYValuePosition()
    {
        return mYValuePosition;
    }

    public void setYValuePosition(ValuePosition yValuePosition)
    {
        this.mYValuePosition = yValuePosition;
    }

    /**
     * When valuePosition is OutsideSlice, use slice colors as line color if true
     * */
    @Override
    public boolean isUsingSliceColorAsValueLineColor() {
        return mUsingSliceColorAsValueLineColor;
    }

    public void setUsingSliceColorAsValueLineColor(boolean usingSliceColorAsValueLineColor) {
        this.mUsingSliceColorAsValueLineColor = usingSliceColorAsValueLineColor;
    }

    /** When valuePosition is OutsideSlice, indicates line color */
    @Override
    public int getValueLineColor()
    {
        return mValueLineColor;
    }

    public void setValueLineColor(int valueLineColor) {
        this.mValueLineColor = valueLineColor;
    }

    /** When valuePosition is OutsideSlice, indicates line width */
    @Override
    public double getValueLineWidth()
    {
        return mValueLineWidth;
    }

    public void setValueLineWidth(double valueLineWidth)
    {
        this.mValueLineWidth = valueLineWidth;
    }

    /** When valuePosition is OutsideSlice, indicates offset as percentage out of the slice size */
    @Override
    public double getValueLinePart1OffsetPercentage()
    {
        return mValueLinePart1OffsetPercentage;
    }

    public void setValueLinePart1OffsetPercentage(double valueLinePart1OffsetPercentage)
    {
        this.mValueLinePart1OffsetPercentage = valueLinePart1OffsetPercentage;
    }

    /** When valuePosition is OutsideSlice, indicates length of first half of the line */
    @Override
    public double getValueLinePart1Length()
    {
        return mValueLinePart1Length;
    }

    public void setValueLinePart1Length(double valueLinePart1Length)
    {
        this.mValueLinePart1Length = valueLinePart1Length;
    }

    /** When valuePosition is OutsideSlice, indicates length of second half of the line */
    @Override
    public double getValueLinePart2Length()
    {
        return mValueLinePart2Length;
    }

    public void setValueLinePart2Length(double valueLinePart2Length)
    {
        this.mValueLinePart2Length = valueLinePart2Length;
    }

    /** When valuePosition is OutsideSlice, this allows variable line length */
    @Override
    public boolean isValueLineVariableLength()
    {
        return mValueLineVariableLength;
    }

    public void setValueLineVariableLength(boolean valueLineVariableLength)
    {
        this.mValueLineVariableLength = valueLineVariableLength;
    }

    public enum ValuePosition {
        INSIDE_SLICE,
        OUTSIDE_SLICE
    }
}
