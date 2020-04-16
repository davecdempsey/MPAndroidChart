
package com.github.mikephil.charting.data;

import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet;

import java.util.List;

public class ScatterData extends BarLineScatterCandleBubbleData<IScatterDataSet> {

    public ScatterData() {
        super();
    }

    public ScatterData(List<IScatterDataSet> dataSets) {
        super(dataSets);
    }

    public ScatterData(IScatterDataSet... dataSets) {
        super(dataSets);
    }

    /**
     * Returns the maximum shape-size across all DataSets.
     *
     * @return
     */
    public double getGreatestShapeSize() {

        double max = 0;

        for (IScatterDataSet set : mDataSets) {
            double size = set.getScatterShapeSize();

            if (size > max)
                max = size;
        }

        return max;
    }
}
