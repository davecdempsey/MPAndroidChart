
package com.github.mikephil.charting.data;

import com.github.mikephil.charting.interfaces.datasets.IBubbleDataSet;
import com.github.mikephil.charting.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class BubbleDataSet extends BarLineScatterCandleBubbleDataSet<BubbleEntry> implements IBubbleDataSet {

    protected double mMaxSize;
    protected boolean mNormalizeSize = true;

    private double mHighlightCircleWidth = 2.5;

    public BubbleDataSet(List<BubbleEntry> yVals, String label) {
        super(yVals, label);
    }

    @Override
    public void setHighlightCircleWidth(double width) {
        mHighlightCircleWidth = Utils.convertDpToPixel(width);
    }

    @Override
    public double getHighlightCircleWidth() {
        return mHighlightCircleWidth;
    }

    @Override
    protected void calcMinMax(BubbleEntry e) {
        super.calcMinMax(e);

        final double size = e.getSize();

        if (size > mMaxSize) {
            mMaxSize = size;
        }
    }

    @Override
    public DataSet<BubbleEntry> copy() {

        List<BubbleEntry> yVals = new ArrayList<BubbleEntry>();

        for (int i = 0; i < mValues.size(); i++) {
            yVals.add(mValues.get(i).copy());
        }

        BubbleDataSet copied = new BubbleDataSet(yVals, getLabel());
        copied.mColors = mColors;
        copied.mHighLightColor = mHighLightColor;

        return copied;
    }

    @Override
    public double getMaxSize() {
        return mMaxSize;
    }

    @Override
    public boolean isNormalizeSizeEnabled() {
        return mNormalizeSize;
    }

    public void setNormalizeSizeEnabled(boolean normalizeSize) {
        mNormalizeSize = normalizeSize;
    }
}
