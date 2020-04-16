package com.github.mikephil.charting.highlight;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.utils.MPPointD;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Philipp Jahoda on 22/07/15.
 */
public class HorizontalBarHighlighter extends BarHighlighter {

	public HorizontalBarHighlighter(BarDataProvider chart) {
		super(chart);
	}

	@Override
	public Highlight getHighlight(double x, double y) {

		BarData barData = mChart.getBarData();

		MPPointD pos = getValsForTouch(y, x);

		Highlight high = getHighlightForX( pos.y, y, x);
		if (high == null)
			return null;

		IBarDataSet set = barData.getDataSetByIndex(high.getDataSetIndex());
		if (set.isStacked()) {

			return getStackedHighlight(high,
					set,
					 pos.y,
					 pos.x);
		}

		MPPointD.recycleInstance(pos);

		return high;
	}

	@Override
	protected List<Highlight> buildHighlights(IDataSet set, int dataSetIndex, double xVal, DataSet.Rounding rounding) {

		ArrayList<Highlight> highlights = new ArrayList<>();

		//noinspection unchecked
		List<Entry> entries = set.getEntriesForXValue(xVal);
		if (entries.size() == 0) {
			// Try to find closest x-value and take all entries for that x-value
			final Entry closest = set.getEntryForXValue(xVal, Double.NaN, rounding);
			if (closest != null)
			{
				//noinspection unchecked
				entries = set.getEntriesForXValue(closest.getX());
			}
		}

		if (entries.size() == 0)
			return highlights;

		for (Entry e : entries) {
			MPPointD pixels = mChart.getTransformer(
					set.getAxisDependency()).getPixelForValues(e.getY(), e.getX());

			highlights.add(new Highlight(
					e.getX(), e.getY(),
					 pixels.x,  pixels.y,
					dataSetIndex, set.getAxisDependency()));
		}

		return highlights;
	}

	@Override
	protected double getDistance(double x1, double y1, double x2, double y2) {
		return Math.abs(y1 - y2);
	}
}
