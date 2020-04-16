
package com.github.mikephil.charting.data;

import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.List;

/**
 * Data object that represents all data for the BarChart.
 *
 * @author Philipp Jahoda
 */
public class BarData extends BarLineScatterCandleBubbleData<IBarDataSet> {

    /**
     * the width of the bars on the x-axis, in values (not pixels)
     */
    private double mBarWidth = 0.85;

    public BarData() {
        super();
    }

    public BarData(IBarDataSet... dataSets) {
        super(dataSets);
    }

    public BarData(List<IBarDataSet> dataSets) {
        super(dataSets);
    }

    /**
     * Sets the width each bar should have on the x-axis (in values, not pixels).
     * Default 0.85
     *
     * @param mBarWidth
     */
    public void setBarWidth(double mBarWidth) {
        this.mBarWidth = mBarWidth;
    }

    public double getBarWidth() {
        return mBarWidth;
    }

    /**
     * Groups all BarDataSet objects this data object holds together by modifying the x-value of their entries.
     * Previously set x-values of entries will be overwritten. Leaves space between bars and groups as specified
     * by the parameters.
     * Do not forget to call notifyDataSetChanged() on your BarChart object after calling this method.
     *
     * @param fromX      the starting point on the x-axis where the grouping should begin
     * @param groupSpace the space between groups of bars in values (not pixels) e.g. 0.8 for bar width 1
     * @param barSpace   the space between individual bars in values (not pixels) e.g. 0.1 for bar width 1
     */
    public void groupBars(double fromX, double groupSpace, double barSpace) {

        int setCount = mDataSets.size();
        if (setCount <= 1) {
            throw new RuntimeException("BarData needs to hold at least 2 BarDataSets to allow grouping.");
        }

        IBarDataSet max = getMaxEntryCountSet();
        int maxEntryCount = max.getEntryCount();

        double groupSpaceWidthHalf = groupSpace / 2;
        double barSpaceHalf = barSpace / 2;
        double barWidthHalf = mBarWidth / 2;

        double interval = getGroupWidth(groupSpace, barSpace);

        for (int i = 0; i < maxEntryCount; i++) {

            double start = fromX;
            fromX += groupSpaceWidthHalf;

            for (IBarDataSet set : mDataSets) {

                fromX += barSpaceHalf;
                fromX += barWidthHalf;

                if (i < set.getEntryCount()) {

                    BarEntry entry = set.getEntryForIndex(i);

                    if (entry != null) {
                        entry.setX(fromX);
                    }
                }

                fromX += barWidthHalf;
                fromX += barSpaceHalf;
            }

            fromX += groupSpaceWidthHalf;
            double end = fromX;
            double innerInterval = end - start;
            double diff = interval - innerInterval;

            // correct rounding errors
            if (diff > 0 || diff < 0) {
                fromX += diff;
            }
        }

        notifyDataChanged();
    }

    /**
     * In case of grouped bars, this method returns the space an individual group of bar needs on the x-axis.
     *
     * @param groupSpace
     * @param barSpace
     * @return
     */
    public double getGroupWidth(double groupSpace, double barSpace) {
        return mDataSets.size() * (mBarWidth + barSpace) + groupSpace;
    }
}
