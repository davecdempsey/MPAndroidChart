package com.github.mikephil.charting.highlight;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.BarLineScatterCandleBubbleData;
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.MPPointD;

/**
 * Created by Philipp Jahoda on 22/07/15.
 */
public class BarHighlighter extends ChartHighlighter<BarDataProvider> {

    public BarHighlighter(BarDataProvider chart) {
        super(chart);
    }

    @Override
    public Highlight getHighlight(double x, double y) {
        Highlight high = super.getHighlight(x, y);

        if(high == null) {
            return null;
        }

        MPPointD pos = getValsForTouch(x, y);

        BarData barData = mChart.getBarData();

        IBarDataSet set = barData.getDataSetByIndex(high.getDataSetIndex());
        if (set.isStacked()) {

            return getStackedHighlight(high,
                    set,
                     pos.x,
                     pos.y);
        }

        MPPointD.recycleInstance(pos);

        return high;
    }

    /**
     * This method creates the Highlight object that also indicates which value of a stacked BarEntry has been
     * selected.
     *
     * @param high the Highlight to work with looking for stacked values
     * @param set
     * @param xVal
     * @param yVal
     * @return
     */
    public Highlight getStackedHighlight(Highlight high, IBarDataSet set, double xVal, double yVal) {

        BarEntry entry = set.getEntryForXValue(xVal, yVal);

        if (entry == null)
            return null;

        // not stacked
        if (entry.getYVals() == null) {
            return high;
        } else {
            Range[] ranges = entry.getRanges();

            if (ranges.length > 0) {
                int stackIndex = getClosestStackIndex(ranges, yVal);

                MPPointD pixels = mChart.getTransformer(set.getAxisDependency()).getPixelForValues(high.getX(), ranges[stackIndex].to);

                Highlight stackedHigh = new Highlight(
                        entry.getX(),
                        entry.getY(),
                         pixels.x,
                         pixels.y,
                        high.getDataSetIndex(),
                        stackIndex,
                        high.getAxis()
                );

                MPPointD.recycleInstance(pixels);

                return stackedHigh;
            }
        }

        return null;
    }

    /**
     * Returns the index of the closest value inside the values array / ranges (stacked barchart) to the value
     * given as
     * a parameter.
     *
     * @param ranges
     * @param value
     * @return
     */
    protected int getClosestStackIndex(Range[] ranges, double value) {

        if (ranges == null || ranges.length == 0)
            return 0;

        int stackIndex = 0;

        for (Range range : ranges) {
            if (range.contains(value))
                return stackIndex;
            else
                stackIndex++;
        }

        int length = Math.max(ranges.length - 1, 0);

        return (value > ranges[length].to) ? length : 0;
    }

//    /**
//     * Splits up the stack-values of the given bar-entry into Range objects.
//     *
//     * @param entry
//     * @return
//     */
//    protected Range[] getRanges(BarEntry entry) {
//
//        double[] values = entry.getYVals();
//
//        if (values == null || values.length == 0)
//            return new Range[0];
//
//        Range[] ranges = new Range[values.length];
//
//        double negRemain = -entry.getNegativeSum();
//        double posRemain = 0;
//
//        for (int i = 0; i < ranges.length; i++) {
//
//            double value = values[i];
//
//            if (value < 0) {
//                ranges[i] = new Range(negRemain, negRemain + Math.abs(value));
//                negRemain += Math.abs(value);
//            } else {
//                ranges[i] = new Range(posRemain, posRemain + value);
//                posRemain += value;
//            }
//        }
//
//        return ranges;
//    }

    @Override
    protected double getDistance(double x1, double y1, double x2, double y2) {
        return Math.abs(x1 - x2);
    }

    @Override
    protected BarLineScatterCandleBubbleData getData() {
        return mChart.getBarData();
    }
}
