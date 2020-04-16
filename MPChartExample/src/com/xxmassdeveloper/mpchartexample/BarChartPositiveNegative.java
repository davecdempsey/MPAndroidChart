
package com.xxmassdeveloper.mpchartexample;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.WindowManager;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.xxmassdeveloper.mpchartexample.notimportant.DemoBase;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class BarChartPositiveNegative extends DemoBase {

    protected BarChart mChart;
    private Typeface mTf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_barchart_noseekbar);

        mTf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
        mChart = (BarChart) findViewById(R.id.chart1);
        mChart.setBackgroundColor(Color.WHITE);
        mChart.setExtraTopOffset(-30);
        mChart.setExtraBottomOffset(10);
        mChart.setExtraLeftOffset(70);
        mChart.setExtraRightOffset(70);

        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);

        mChart.getDescription().setEnabled(false);

        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        mChart.setDrawGridBackground(false);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxisPosition.BOTTOM);
        xAxis.setTypeface(mTf);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setTextColor(Color.LTGRAY);
        xAxis.setTextSize(13);
        xAxis.setLabelCount(5);
        xAxis.setCenterAxisLabels(true);
        xAxis.setGranularity(1);

        YAxis left = mChart.getAxisLeft();
        left.setDrawLabels(false);
        left.setSpaceTop(25);
        left.setSpaceBottom(25);
        left.setDrawAxisLine(false);
        left.setDrawGridLines(false);
        left.setDrawZeroLine(true); // draw a zero line
        left.setZeroLineColor(Color.GRAY);
        left.setZeroLineWidth(0.7);
        mChart.getAxisRight().setEnabled(false);
        mChart.getLegend().setEnabled(false);

        // THIS IS THE ORIGINAL DATA YOU WANT TO PLOT
        final List<Data> data = new ArrayList<>();
        data.add(new Data(0, -224.1, "12-29"));
        data.add(new Data(1, 238.5, "12-30"));
        data.add(new Data(2, 1280.1, "12-31"));
        data.add(new Data(3, -442.3, "01-01"));
        data.add(new Data(4, -2280.1, "01-02"));

        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(double value, AxisBase axis) {
                return data.get(Math.min(Math.max((int) value, 0), data.size()-1)).xAxisValue;
            }
        });

        setData(data);
    }

    private void setData(List<Data> dataList) {

        ArrayList<BarEntry> values = new ArrayList<BarEntry>();
        List<Integer> colors = new ArrayList<Integer>();

        int green = Color.rgb(110, 190, 102);
        int red = Color.rgb(211, 74, 88);

        for (int i = 0; i < dataList.size(); i++) {

            Data d = dataList.get(i);
            BarEntry entry = new BarEntry(d.xValue, d.yValue);
            values.add(entry);

            // specific colors
            if (d.yValue >= 0)
                colors.add(red);
            else
                colors.add(green);
        }

        BarDataSet set;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set = (BarDataSet)mChart.getData().getDataSetByIndex(0);
            set.setValues(values);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            set = new BarDataSet(values, "Values");
            set.setColors(colors);
            set.setValueTextColors(colors);

            BarData data = new BarData(set);
            data.setValueTextSize(13);
            data.setValueTypeface(mTf);
            data.setValueFormatter(new ValueFormatter());
            data.setBarWidth(0.8);

            mChart.setData(data);
            mChart.invalidate();
        }
    }

    /**
     * Demo class representing data.
     */
    private class Data {

        public String xAxisValue;
        public double yValue;
        public double xValue;

        public Data(double xValue, double yValue, String xAxisValue) {
            this.xAxisValue = xAxisValue;
            this.yValue = yValue;
            this.xValue = xValue;
        }
    }

    private class ValueFormatter implements IValueFormatter
    {

        private DecimalFormat mFormat;

        public ValueFormatter() {
            mFormat = new DecimalFormat("######.0");
        }

        @Override
        public String getFormattedValue(double value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            return mFormat.format(value);
        }
    }
}
