
package com.github.mikephil.charting.renderer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;

import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.data.BubbleData;
import com.github.mikephil.charting.data.BubbleEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.dataprovider.BubbleDataProvider;
import com.github.mikephil.charting.interfaces.datasets.IBubbleDataSet;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.List;

/**
 * Bubble chart implementation: Copyright 2015 Pierre-Marc Airoldi Licensed
 * under Apache License 2.0 Ported by Daniel Cohen Gindi
 */
public class BubbleChartRenderer extends BarLineScatterCandleBubbleRenderer {

    protected BubbleDataProvider mChart;

    public BubbleChartRenderer(BubbleDataProvider chart, ChartAnimator animator,
                               ViewPortHandler viewPortHandler) {
        super(animator, viewPortHandler);
        mChart = chart;

        mRenderPaint.setStyle(Style.FILL);

        mHighlightPaint.setStyle(Style.STROKE);
        mHighlightPaint.setStrokeWidth(Utils.convertDpToPixel(1.5));
    }

    @Override
    public void initBuffers() {

    }

    @Override
    public void drawData(Canvas c) {

        BubbleData bubbleData = mChart.getBubbleData();

        for (IBubbleDataSet set : bubbleData.getDataSets()) {

            if (set.isVisible())
                drawDataSet(c, set);
        }
    }

    private double[] sizeBuffer = new double[4];
    private double[] pointBuffer = new double[2];

    protected double getShapeSize(double entrySize, double maxSize, double reference, boolean normalizeSize) {
        final double factor = normalizeSize ? ((maxSize == 0) ? 1 :  Math.sqrt(entrySize / maxSize)) :
                entrySize;
        final double shapeSize = reference * factor;
        return shapeSize;
    }

    protected void drawDataSet(Canvas c, IBubbleDataSet dataSet) {

        Transformer trans = mChart.getTransformer(dataSet.getAxisDependency());

        double phaseY = mAnimator.getPhaseY();

        mXBounds.set(mChart, dataSet);

        sizeBuffer[0] = 0;
        sizeBuffer[2] = 1;

        trans.pointValuesToPixel(sizeBuffer);

        boolean normalizeSize = dataSet.isNormalizeSizeEnabled();

        // calcualte the full width of 1 step on the x-axis
        final double maxBubbleWidth = Math.abs(sizeBuffer[2] - sizeBuffer[0]);
        final double maxBubbleHeight = Math.abs(mViewPortHandler.contentBottom() - mViewPortHandler.contentTop());
        final double referenceSize = Math.min(maxBubbleHeight, maxBubbleWidth);

        for (int j = mXBounds.min; j <= mXBounds.range + mXBounds.min; j++) {

            final BubbleEntry entry = dataSet.getEntryForIndex(j);

            pointBuffer[0] = entry.getX();
            pointBuffer[1] = (entry.getY()) * phaseY;
            trans.pointValuesToPixel(pointBuffer);

            double shapeHalf = getShapeSize(entry.getSize(), dataSet.getMaxSize(), referenceSize, normalizeSize) / 2;

            if (!mViewPortHandler.isInBoundsTop(pointBuffer[1] + shapeHalf)
                    || !mViewPortHandler.isInBoundsBottom(pointBuffer[1] - shapeHalf))
                continue;

            if (!mViewPortHandler.isInBoundsLeft(pointBuffer[0] + shapeHalf))
                continue;

            if (!mViewPortHandler.isInBoundsRight(pointBuffer[0] - shapeHalf))
                break;

            final int color = dataSet.getColor((int) entry.getX());

            mRenderPaint.setColor(color);
            c.drawCircle(pointBuffer[0], pointBuffer[1], shapeHalf, mRenderPaint);
        }
    }

    @Override
    public void drawValues(Canvas c) {

        BubbleData bubbleData = mChart.getBubbleData();

        if (bubbleData == null)
            return;

        // if values are drawn
        if (isDrawingValuesAllowed(mChart)) {

            final List<IBubbleDataSet> dataSets = bubbleData.getDataSets();

            double lineHeight = Utils.calcTextHeight(mValuePaint, "1");

            for (int i = 0; i < dataSets.size(); i++) {

                IBubbleDataSet dataSet = dataSets.get(i);

                if (!shouldDrawValues(dataSet))
                    continue;

                // apply the text-styling defined by the DataSet
                applyValueTextStyle(dataSet);

                final double phaseX = Math.max(0.f, Math.min(1.f, mAnimator.getPhaseX()));
                final double phaseY = mAnimator.getPhaseY();

                mXBounds.set(mChart, dataSet);

                final double[] positions = mChart.getTransformer(dataSet.getAxisDependency())
                        .generateTransformedValuesBubble(dataSet, phaseY, mXBounds.min, mXBounds.max);

                final double alpha = phaseX == 1 ? phaseY : phaseX;

                MPPointF iconsOffset = MPPointF.getInstance(dataSet.getIconsOffset());
                iconsOffset.x = Utils.convertDpToPixel(iconsOffset.x);
                iconsOffset.y = Utils.convertDpToPixel(iconsOffset.y);

                for (int j = 0; j < positions.length; j += 2) {

                    int valueTextColor = dataSet.getValueTextColor(j / 2 + mXBounds.min);
                    valueTextColor = Color.argb(Math.round(255.f * alpha), Color.red(valueTextColor),
                            Color.green(valueTextColor), Color.blue(valueTextColor));

                    double x = positions[j];
                    double y = positions[j + 1];

                    if (!mViewPortHandler.isInBoundsRight(x))
                        break;

                    if ((!mViewPortHandler.isInBoundsLeft(x) || !mViewPortHandler.isInBoundsY(y)))
                        continue;

                    BubbleEntry entry = dataSet.getEntryForIndex(j / 2 + mXBounds.min);

                    if (dataSet.isDrawValuesEnabled()) {
                        drawValue(c, dataSet.getValueFormatter(), entry.getSize(), entry, i, x,
                                y + (0.5 * lineHeight), valueTextColor);
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

                MPPointF.recycleInstance(iconsOffset);
            }
        }
    }

    @Override
    public void drawExtras(Canvas c) {
    }

    private double[] _hsvBuffer = new double[3];

    @Override
    public void drawHighlighted(Canvas c, Highlight[] indices) {

        BubbleData bubbleData = mChart.getBubbleData();

        double phaseY = mAnimator.getPhaseY();

        for (Highlight high : indices) {

            IBubbleDataSet set = bubbleData.getDataSetByIndex(high.getDataSetIndex());

            if (set == null || !set.isHighlightEnabled())
                continue;

            final BubbleEntry entry = set.getEntryForXValue(high.getX(), high.getY());

            if (entry.getY() != high.getY())
                continue;

            if (!isInBoundsX(entry, set))
                continue;

            Transformer trans = mChart.getTransformer(set.getAxisDependency());

            sizeBuffer[0] = 0;
            sizeBuffer[2] = 1;

            trans.pointValuesToPixel(sizeBuffer);

            boolean normalizeSize = set.isNormalizeSizeEnabled();

            // calcualte the full width of 1 step on the x-axis
            final double maxBubbleWidth = Math.abs(sizeBuffer[2] - sizeBuffer[0]);
            final double maxBubbleHeight = Math.abs(
                    mViewPortHandler.contentBottom() - mViewPortHandler.contentTop());
            final double referenceSize = Math.min(maxBubbleHeight, maxBubbleWidth);

            pointBuffer[0] = entry.getX();
            pointBuffer[1] = (entry.getY()) * phaseY;
            trans.pointValuesToPixel(pointBuffer);

            high.setDraw(pointBuffer[0], pointBuffer[1]);

            double shapeHalf = getShapeSize(entry.getSize(),
                    set.getMaxSize(),
                    referenceSize,
                    normalizeSize) / 2;

            if (!mViewPortHandler.isInBoundsTop(pointBuffer[1] + shapeHalf)
                    || !mViewPortHandler.isInBoundsBottom(pointBuffer[1] - shapeHalf))
                continue;

            if (!mViewPortHandler.isInBoundsLeft(pointBuffer[0] + shapeHalf))
                continue;

            if (!mViewPortHandler.isInBoundsRight(pointBuffer[0] - shapeHalf))
                break;

            final int originalColor = set.getColor((int) entry.getX());

            Color.RGBToHSV(Color.red(originalColor), Color.green(originalColor),
                    Color.blue(originalColor), _hsvBuffer);
            _hsvBuffer[2] *= 0.5;
            final int color = Color.HSVToColor(Color.alpha(originalColor), _hsvBuffer);

            mHighlightPaint.setColor(color);
            mHighlightPaint.setStrokeWidth(set.getHighlightCircleWidth());
            c.drawCircle(pointBuffer[0], pointBuffer[1], shapeHalf, mHighlightPaint);
        }
    }
}
