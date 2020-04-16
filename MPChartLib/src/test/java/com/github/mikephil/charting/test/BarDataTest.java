package com.github.mikephil.charting.test;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;

/**
 * Created by philipp on 06/06/16.
 */
public class BarDataTest {

    @Test
    public void testGroupBars() {

        double groupSpace = 5;
        double barSpace = 1;

        List<BarEntry> values1 = new ArrayList<>();
        List<BarEntry> values2 = new ArrayList<>();

        for(int i = 0; i < 5; i++) {
            values1.add(new BarEntry(i, 50));
            values2.add(new BarEntry(i, 60));
        }

        BarDataSet barDataSet1 = new BarDataSet(values1, "Set1");
        BarDataSet barDataSet2 = new BarDataSet(values2, "Set2");

        BarData data = new BarData(barDataSet1, barDataSet2);
        data.setBarWidth(10);

        double groupWidth = data.getGroupWidth(groupSpace, barSpace);
        assertEquals(27, groupWidth, 0.01);

        assertEquals(0, values1.get(0).getX(), 0.01);
        assertEquals(1, values1.get(1).getX(), 0.01);

        data.groupBars(1000, groupSpace, barSpace);

        // 1000 + 2.5 + 0.5 + 5
        assertEquals(1008, values1.get(0).getX(), 0.01);
        assertEquals(1019, values2.get(0).getX(), 0.01);
        assertEquals(1035, values1.get(1).getX(), 0.01);
        assertEquals(1046, values2.get(1).getX(), 0.01);

        data.groupBars(-1000, groupSpace, barSpace);

        assertEquals(-992, values1.get(0).getX(), 0.01);
        assertEquals(-981, values2.get(0).getX(), 0.01);
        assertEquals(-965, values1.get(1).getX(), 0.01);
        assertEquals(-954, values2.get(1).getX(), 0.01);

        data.setBarWidth(20);
        groupWidth = data.getGroupWidth(groupSpace, barSpace);
        assertEquals(47, groupWidth, 0.01);

        data.setBarWidth(10);
        data.groupBars(-20, groupSpace, barSpace);

        assertEquals(-12, values1.get(0).getX(), 0.01);
        assertEquals(-1, values2.get(0).getX(), 0.01);
        assertEquals(15, values1.get(1).getX(), 0.01);
        assertEquals(26, values2.get(1).getX(), 0.01);
    }
}
