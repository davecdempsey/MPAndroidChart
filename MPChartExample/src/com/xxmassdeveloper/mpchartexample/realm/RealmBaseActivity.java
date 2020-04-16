package com.xxmassdeveloper.mpchartexample.realm;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.xxmassdeveloper.mpchartexample.custom.RealmDemoData;
import com.xxmassdeveloper.mpchartexample.notimportant.DemoBase;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Philipp Jahoda on 05/11/15.
 */
public abstract class RealmBaseActivity extends DemoBase {

    protected Realm mRealm;

    protected Typeface mTf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Realm.io Examples");
    }

    protected void setup(Chart<?> chart) {

        mTf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

        // no description text
        chart.getDescription().setEnabled(false);

        // enable touch gestures
        chart.setTouchEnabled(true);

        if (chart instanceof BarLineChartBase) {

            BarLineChartBase mChart = (BarLineChartBase) chart;

            mChart.setDrawGridBackground(false);

            // enable scaling and dragging
            mChart.setDragEnabled(true);
            mChart.setScaleEnabled(true);

            // if disabled, scaling can be done on x- and y-axis separately
            mChart.setPinchZoom(false);

            YAxis leftAxis = mChart.getAxisLeft();
            leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
            leftAxis.setTypeface(mTf);
            leftAxis.setTextSize(8);
            leftAxis.setTextColor(Color.DKGRAY);
            leftAxis.setValueFormatter(new PercentFormatter());

            XAxis xAxis = mChart.getXAxis();
            xAxis.setTypeface(mTf);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setTextSize(8);
            xAxis.setTextColor(Color.DKGRAY);

            mChart.getAxisRight().setEnabled(false);
        }
    }

    protected void styleData(ChartData data) {
        data.setValueTypeface(mTf);
        data.setValueTextSize(8);
        data.setValueTextColor(Color.DKGRAY);
        data.setValueFormatter(new PercentFormatter());
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Create a RealmConfiguration that saves the Realm file in the app's "files" directory.
        RealmConfiguration realmConfig = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfig);

        mRealm = Realm.getDefaultInstance();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mRealm.close();
    }

    protected void writeToDB(int objectCount) {

        mRealm.beginTransaction();

        mRealm.delete(RealmDemoData.class);

        for (int i = 0; i < objectCount; i++) {

            double value = 40.0 + (Math.random() * 60.0);

            RealmDemoData d = new RealmDemoData(i, value);
            mRealm.copyToRealm(d);
        }

        mRealm.commitTransaction();
    }

    protected void writeToDBStack(int objectCount) {

        mRealm.beginTransaction();

        mRealm.delete(RealmDemoData.class);

        for (int i = 0; i < objectCount; i++) {

            double val1 = 34 + (Math.random() * 12.0);
            double val2 = 34 + (Math.random() * 12.0);
            double[] stack = new double[]{val1, val2, 100 - val1 - val2};

            RealmDemoData d = new RealmDemoData(i, stack);
            mRealm.copyToRealm(d);
        }

        mRealm.commitTransaction();
    }

    protected void writeToDBCandle(int objectCount) {

        mRealm.beginTransaction();

        mRealm.delete(RealmDemoData.class);

        for (int i = 0; i < objectCount; i++) {

            double mult = 50;
            double val = (Math.random() * 40) + mult;

            double high = (Math.random() * 9) + 8.0;
            double low = (Math.random() * 9) + 8.0;

            double open = (Math.random() * 6) + 1.0;
            double close = (Math.random() * 6) + 1.0;

            boolean even = i % 2 == 0;

            RealmDemoData d = new RealmDemoData(i, val + high, val - low, even ? val + open : val - open,
                    even ? val - close : val + close);

            mRealm.copyToRealm(d);
        }

        mRealm.commitTransaction();
    }

    protected void writeToDBBubble(int objectCount) {

        mRealm.beginTransaction();

        mRealm.delete(RealmDemoData.class);

        for (int i = 0; i < objectCount; i++) {

            double value = 30.0 + (Math.random() * 100.0);
            double size = 15.0 + (Math.random() * 20.0);

            RealmDemoData d = new RealmDemoData(i, value, size);
            mRealm.copyToRealm(d);
        }

        mRealm.commitTransaction();
    }

    protected void writeToDBPie() {

        mRealm.beginTransaction();

        mRealm.delete(RealmDemoData.class);

        double value1 = 15.0 + (Math.random() * 8.0);
        double value2 = 15.0 + (Math.random() * 8.0);
        double value3 = 15.0 + (Math.random() * 8.0);
        double value4 = 15.0 + (Math.random() * 8.0);
        double value5 = 100.0 - value1 - value2 - value3 - value4;

        double[] values = new double[]{value1, value2, value3, value4, value5};
        String[] labels = new String[]{"iOS", "Android", "WP 10", "BlackBerry", "Other"};

        for (int i = 0; i < values.length; i++) {
            RealmDemoData d = new RealmDemoData(values[i], labels[i]);
            mRealm.copyToRealm(d);
        }

        mRealm.commitTransaction();
    }
}
