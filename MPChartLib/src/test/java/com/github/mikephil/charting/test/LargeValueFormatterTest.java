package com.github.mikephil.charting.test;

import com.github.mikephil.charting.formatter.LargeValueFormatter;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by philipp on 06/06/16.
 */
public class LargeValueFormatterTest {

    @Test
    public void test() {

        LargeValueFormatter formatter = new LargeValueFormatter();

        String result = formatter.getFormattedValue(5, null);
        assertEquals("5", result);

        result = formatter.getFormattedValue(5.5, null);
        assertEquals("5.5", result);

        result = formatter.getFormattedValue(50, null);
        assertEquals("50", result);

        result = formatter.getFormattedValue(50.5, null);
        assertEquals("50.5", result);

        result = formatter.getFormattedValue(500, null);
        assertEquals("500", result);

        result = formatter.getFormattedValue(1100, null);
        assertEquals("1.1k", result);

        result = formatter.getFormattedValue(10000, null);
        assertEquals("10k", result);

        result = formatter.getFormattedValue(10500, null);
        assertEquals("10.5k", result);

        result = formatter.getFormattedValue(100000, null);
        assertEquals("100k", result);

        result = formatter.getFormattedValue(1000000, null);
        assertEquals("1m", result);

        result = formatter.getFormattedValue(1500000, null);
        assertEquals("1.5m", result);

        result = formatter.getFormattedValue(9500000, null);
        assertEquals("9.5m", result);

        result = formatter.getFormattedValue(22200000, null);
        assertEquals("22.2m", result);

        result = formatter.getFormattedValue(222000000, null);
        assertEquals("222m", result);

        result = formatter.getFormattedValue(1000000000, null);
        assertEquals("1b", result);

        result = formatter.getFormattedValue(9900000000, null);
        assertEquals("9.9b", result);

        result = formatter.getFormattedValue(99000000000, null);
        assertEquals("99b", result);

        result = formatter.getFormattedValue(99500000000, null);
        assertEquals("99.5b", result);

        result = formatter.getFormattedValue(999000000000, null);
        assertEquals("999b", result);

        result = formatter.getFormattedValue(1000000000000, null);
        assertEquals("1t", result);

        formatter.setSuffix(new String[]{"", "k", "m", "b", "t", "q"}); // quadrillion support
        result = formatter.getFormattedValue(1000000000000000, null);
        assertEquals("1q", result);

        result = formatter.getFormattedValue(1100000000000000, null);
        assertEquals("1.1q", result);

        result = formatter.getFormattedValue(10000000000000000, null);
        assertEquals("10q", result);

        result = formatter.getFormattedValue(13300000000000000, null);
        assertEquals("13.3q", result);

        result = formatter.getFormattedValue(100000000000000000, null);
        assertEquals("100q", result);
    }
}
