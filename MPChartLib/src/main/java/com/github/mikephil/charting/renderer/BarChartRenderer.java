
package com.github.mikephil.charting.renderer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.buffer.BarBuffer;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.highlight.Range;
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.List;

public class BarChartRenderer extends BarLineScatterCandleBubbleRenderer {

    protected BarDataProvider mChart;

    /**
     * the rect object that is used for drawing the bars
     */
    protected RectF mBarRect = new RectF();

    protected BarBuffer[] mBarBuffers;

    protected Paint mShadowPaint;
    protected Paint mBarBorderPaint;

    public BarChartRenderer(BarDataProvider chart, ChartAnimator animator,
                            ViewPortHandler viewPortHandler) {
        super(animator, viewPortHandler);
        this.mChart = chart;

        mHighlightPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHighlightPaint.setStyle(Paint.Style.FILL);
        mHighlightPaint.setColor(Color.rgb(0, 0, 0));
        // set alpha after color
        mHighlightPaint.setAlpha(120);

        mShadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mShadowPaint.setStyle(Paint.Style.FILL);

        mBarBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBarBorderPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    public void initBuffers() {

        BarData barData = mChart.getBarData();
        mBarBuffers = new BarBuffer[barData.getDataSetCount()];

        for (int i = 0; i < mBarBuffers.length; i++) {
            IBarDataSet set = barData.getDataSetByIndex(i);
            mBarBuffers[i] = new BarBuffer(set.getEntryCount() * 4 * (set.isStacked() ? set.getStackSize() : 1),
                    barData.getDataSetCount(), set.isStacked());
        }
    }

    @Override
    public void drawData(Canvas c) {

        BarData barData = mChart.getBarData();

        for (int i = 0; i < barData.getDataSetCount(); i++) {

            IBarDataSet set = barData.getDataSetByIndex(i);

            if (set.isVisible()) {
                drawDataSet(c, set, i);
            }
        }
    }

    private RectF mBarShadowRectBuffer = new RectF();

    protected void drawDataSet(Canvas c, IBarDataSet dataSet, int index) {

        Transformer trans = mChart.getTransformer(dataSet.getAxisDependency());

        mBarBorderPaint.setColor(dataSet.getBarBorderColor());
        mBarBorderPaint.setStrokeWidth(Utils.convertDpToPixel(dataSet.getBarBorderWidth()));

        final boolean drawBorder = dataSet.getBarBorderWidth() > 0.f;

        double phaseX = mAnimator.getPhaseX();
        double phaseY = mAnimator.getPhaseY();

        // draw the bar shadow before the values
        if (mChart.isDrawBarShadowEnabled()) {
            mShadowPaint.setColor(dataSet.getBarShadowColor());

            BarData barData = mChart.getBarData();

            final double barWidth = barData.getBarWidth();
            final double barWidthHalf = barWidth / 2.0;
            double x;

            for (int i = 0, count = Math.min((int)(Math.ceil((dataSet.getEntryCount()) * phaseX)), dataSet.getEntryCount());
                i < count;
                i++) {

                BarEntry e = dataSet.getEntryForIndex(i);

                x = e.getX();

                mBarShadowRectBuffer.left = x - barWidthHalf;
                mBarShadowRectBuffer.right = x + barWidthHalf;

                trans.rectValueToPixel(mBarShadowRectBuffer);

                if (!mViewPortHandler.isInBoundsLeft(mBarShadowRectBuffer.right))
                    continue;

                if (!mViewPortHandler.isInBoundsRight(mBarShadowRectBuffer.left))
                    break;

                mBarShadowRectBuffer.top = mViewPortHandler.contentTop();
                mBarShadowRectBuffer.bottom = mViewPortHandler.contentBottom();

                c.drawRect(mBarShadowRectBuffer, mShadowPaint);
            }
        }

        // initialize the buffer
        BarBuffer buffer = mBarBuffers[index];
        buffer.setPhases(phaseX, phaseY);
        buffer.setDataSet(index);
        buffer.setInverted(mChart.isInverted(dataSet.getAxisDependency()));
        buffer.setBarWidth(mChart.getBarData().getBarWidth());

        buffer.feed(dataSet);

        trans.pointValuesToPixel(buffer.buffer);

        final boolean isSingleColor = dataSet.getColors().size() == 1;

        if (isSingleColor) {
            mRenderPaint.setColor(dataSet.getColor());
        }

        for (int j = 0; j < buffer.size(); j += 4) {

            if (!mViewPortHandler.isInBoundsLeft(buffer.buffer[j + 2]))
                continue;

            if (!mViewPortHandler.isInBoundsRight(buffer.buffer[j]))
                break;

            if (!isSingleColor) {
                // Set the color for the currently drawn value. If the index
                // is out of bounds, reuse colors.
                mRenderPaint.setColor(dataSet.getColor(j / 4));
            }

            c.drawRect(buffer.buffer[j], buffer.buffer[j + 1], buffer.buffer[j + 2],
                    buffer.buffer[j + 3], mRenderPaint);

            if (drawBorder) {
                c.drawRect(buffer.buffer[j], buffer.buffer[j + 1], buffer.buffer[j + 2],
                        buffer.buffer[j + 3], mBarBorderPaint);
            }
        }
    }

    protected void prepareBarHighlight(double x, double y1, double y2, double barWidthHalf, Transformer trans) {

        double left = x - barWidthHalf;
        double right = x + barWidthHalf;
        double top = y1;
        double bottom = y2;

        mBarRect.set(left, top, right, bottom);

        trans.rectToPixelPhase(mBarRect, mAnimator.getPhaseY());
    }

    @Override
    public void drawValues(Canvas c) {

        // if values are drawn
        if (isDrawingValuesAllowed(mChart)) {

            List<IBarDataSet> dataSets = mChart.getBarData().getDataSets();

            final double valueOffsetPlus = Utils.convertDpToPixel(4.5);
            double posOffset = 0;
            double negOffset = 0;
            boolean drawValueAboveBar = mChart.isDrawValueAboveBarEnabled();

            for (int i = 0; i < mChart.getBarData().getDataSetCount(); i++) {

                IBarDataSet dataSet = dataSets.get(i);

                if (!shouldDrawValues(dataSet))
                    continue;

                // apply the text-styling defined by the DataSet
                applyValueTextStyle(dataSet);

                boolean isInverted = mChart.isInverted(dataSet.getAxisDependency());

                // calculate the correct offset depending on the draw position of
                // the value
                double valueTextHeight = Utils.calcTextHeight(mValuePaint, "8");
                posOffset = (drawValueAboveBar ? -valueOffsetPlus : valueTextHeight + valueOffsetPlus);
                negOffset = (drawValueAboveBar ? valueTextHeight + valueOffsetPlus : -valueOffsetPlus);

                if (isInverted) {
                    posOffset = -posOffset - valueTextHeight;
                    negOffset = -negOffset - valueTextHeight;
                }

                // get the buffer
                BarBuffer buffer = mBarBuffers[i];

                final double phaseY = mAnimator.getPhaseY();

                MPPointF iconsOffset = MPPointF.getInstance(dataSet.getIconsOffset());
                iconsOffset.x = Utils.convertDpToPixel(iconsOffset.x);
                iconsOffset.y = Utils.convertDpToPixel(iconsOffset.y);

                // if only single values are drawn (sum)
                if (!dataSet.isStacked()) {

                    for (int j = 0; j < buffer.buffer.length * mAnimator.getPhaseX(); j += 4) {

                        double x = (buffer.buffer[j] + buffer.buffer[j + 2]) / 2;

                        if (!mViewPortHandler.isInBoundsRight(x))
                            break;

                        if (!mViewPortHandler.isInBoundsY(buffer.buffer[j + 1])
                                || !mViewPortHandler.isInBoundsLeft(x))
                            continue;

                        BarEntry entry = dataSet.getEntryForIndex(j / 4);
                        double val = entry.getY();

                        if (dataSet.isDrawValuesEnabled()) {
                            drawValue(c, dataSet.getValueFormatter(), val, entry, i, x,
                                    val >= 0 ?
                                            (buffer.buffer[j + 1] + posOffset) :
                                            (buffer.buffer[j + 3] + negOffset),
                                    dataSet.getValueTextColor(j / 4));
                        }

                        if (entry.getIcon() != null && dataSet.isDrawIconsEnabled()) {

                            Drawable icon = entry.getIcon();

                            double px = x;
                            double py = val >= 0 ?
                                    (buffer.buffer[j + 1] + posOffset) :
                                    (buffer.buffer[j + 3] + negOffset);

                            px += iconsOffset.x;
                            py += iconsOffset.y;

                            Utils.drawImage(
                                    c,
                                    icon,
                                    (int)px,
                                    (int)py,
                                    icon.getIntrinsicWidth(),
                                    icon.getIntrinsicHeight());
                        }
                    }

                    // if we have stacks
                } else {

                    Transformer trans = mChart.getTransformer(dataSet.getAxisDependency());

                    int bufferIndex = 0;
                    int index = 0;

                    while (index < dataSet.getEntryCount() * mAnimator.getPhaseX()) {

                        BarEntry entry = dataSet.getEntryForIndex(index);

                        double[] vals = entry.getYVals();
                        double x = (buffer.buffer[bufferIndex] + buffer.buffer[bufferIndex + 2]) / 2;

                        int color = dataSet.getValueTextColor(index);

                        // we still draw stacked bars, but there is one
                        // non-stacked
                        // in between
                        if (vals == null) {

                            if (!mViewPortHandler.isInBoundsRight(x))
                                break;

                            if (!mViewPortHandler.isInBoundsY(buffer.buffer[bufferIndex + 1])
                                    || !mViewPortHandler.isInBoundsLeft(x))
                                continue;

                            if (dataSet.isDrawValuesEnabled()) {
                                drawValue(c, dataSet.getValueFormatter(), entry.getY(), entry, i, x,
                                        buffer.buffer[bufferIndex + 1] +
                                                (entry.getY() >= 0 ? posOffset : negOffset),
                                        color);
                            }

                            if (entry.getIcon() != null && dataSet.isDrawIconsEnabled()) {

                                Drawable icon = entry.getIcon();

                                double px = x;
                                double py = buffer.buffer[bufferIndex + 1] +
                                        (entry.getY() >= 0 ? posOffset : negOffset);

                                px += iconsOffset.x;
                                py += iconsOffset.y;

                                Utils.drawImage(
                                        c,
                                        icon,
                                        (int)px,
                                        (int)py,
                                        icon.getIntrinsicWidth(),
                                        icon.getIntrinsicHeight());
                            }

                            // draw stack values
                        } else {

                            double[] transformed = new double[vals.length * 2];

                            double posY = 0;
                            double negY = -entry.getNegativeSum();

                            for (int k = 0, idx = 0; k < transformed.length; k += 2, idx++) {

                                double value = vals[idx];
                                double y;

                                if (value == 0.0 && (posY == 0.0 || negY == 0.0)) {
                                    // Take care of the situation of a 0.0 value, which overlaps a non-zero bar
                                    y = value;
                                } else if (value >= 0.0) {
                                    posY += value;
                                    y = posY;
                                } else {
                                    y = negY;
                                    negY -= value;
                                }

                                transformed[k + 1] = y * phaseY;
                            }

                            trans.pointValuesToPixel(transformed);

                            for (int k = 0; k < transformed.length; k += 2) {

                                final double val = vals[k / 2];
                                final boolean drawBelow =
                                        (val == 0.0 && negY == 0.0 && posY > 0.0) ||
                                                val < 0.0;
                                double y = transformed[k + 1]
                                        + (drawBelow ? negOffset : posOffset);

                                if (!mViewPortHandler.isInBoundsRight(x))
                                    break;

                                if (!mViewPortHandler.isInBoundsY(y)
                                        || !mViewPortHandler.isInBoundsLeft(x))
                                    continue;

                                if (dataSet.isDrawValuesEnabled()) {
                                    drawValue(c,
                                            dataSet.getValueFormatter(),
                                            vals[k / 2],
                                            entry,
                                            i,
                                            x,
                                            y,
                                            color);
                                }

                                if (entry.getIcon() != null && dataSet.isDrawIconsEnabled()) {

                                    Drawable icon = entry.getIcon();

                                    Utils.drawImage(
                                            c,
                                            icon,
                                            (int)(x + iconsOffset.x),
                                            (int)(y + iconsOffset.y),
                                            icon.getIntrinsicWidth(),
                                            icon.getIntrinsicHeight());
                                }
                            }
                        }

                        bufferIndex = vals == null ? bufferIndex + 4 : bufferIndex + 4 * vals.length;
                        index++;
                    }
                }

                MPPointF.recycleInstance(iconsOffset);
            }
        }
    }

    @Override
    public void drawHighlighted(Canvas c, Highlight[] indices) {

        BarData barData = mChart.getBarData();

        for (Highlight high : indices) {

            IBarDataSet set = barData.getDataSetByIndex(high.getDataSetIndex());

            if (set == null || !set.isHighlightEnabled())
                continue;

            BarEntry e = set.getEntryForXValue(high.getX(), high.getY());

            if (!isInBoundsX(e, set))
                continue;

            Transformer trans = mChart.getTransformer(set.getAxisDependency());

            mHighlightPaint.setColor(set.getHighLightColor());
            mHighlightPaint.setAlpha(set.getHighLightAlpha());

            boolean isStack = (high.getStackIndex() >= 0  && e.isStacked()) ? true : false;

            final double y1;
            final double y2;

            if (isStack) {

                if(mChart.isHighlightFullBarEnabled()) {

                    y1 = e.getPositiveSum();
                    y2 = -e.getNegativeSum();

                } else {

                    Range range = e.getRanges()[high.getStackIndex()];

                    y1 = range.from;
                    y2 = range.to;
                }

            } else {
                y1 = e.getY();
                y2 = 0.f;
            }

            prepareBarHighlight(e.getX(), y1, y2, barData.getBarWidth() / 2, trans);

            setHighlightDrawPos(high, mBarRect);

            c.drawRect(mBarRect, mHighlightPaint);
        }
    }

    /**
     * Sets the drawing position of the highlight object based on the riven bar-rect.
     * @param high
     */
    protected void setHighlightDrawPos(Highlight high, RectF bar) {
        high.setDraw(bar.centerX(), bar.top);
    }

    @Override
    public void drawExtras(Canvas c) {
    }
}
