
package com.github.mikephil.charting.charts;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.animation.Easing.EasingFunction;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.listener.PieRadarChartTouchListener;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;

/**
 * Baseclass of PieChart and RadarChart.
 *
 * @author Philipp Jahoda
 */
public abstract class PieRadarChartBase<T extends ChartData<? extends IDataSet<? extends Entry>>>
        extends Chart<T> {

    /**
     * holds the normalized version of the current rotation angle of the chart
     */
    private double mRotationAngle = 270;

    /**
     * holds the raw version of the current rotation angle of the chart
     */
    private double mRawRotationAngle = 270;

    /**
     * flag that indicates if rotation is enabled or not
     */
    protected boolean mRotateEnabled = true;

    /**
     * Sets the minimum offset (padding) around the chart, defaults to 0.f
     */
    protected double mMinOffset = 0.f;

    public PieRadarChartBase(Context context) {
        super(context);
    }

    public PieRadarChartBase(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PieRadarChartBase(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void init() {
        super.init();

        mChartTouchListener = new PieRadarChartTouchListener(this);
    }

    @Override
    protected void calcMinMax() {
        //mXAxis.mAxisRange = mData.getXVals().size() - 1;
    }

    @Override
    public int getMaxVisibleCount() {
        return mData.getEntryCount();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // use the pie- and radarchart listener own listener
        if (mTouchEnabled && mChartTouchListener != null)
            return mChartTouchListener.onTouch(this, event);
        else
            return super.onTouchEvent(event);
    }

    @Override
    public void computeScroll() {

        if (mChartTouchListener instanceof PieRadarChartTouchListener)
            ((PieRadarChartTouchListener) mChartTouchListener).computeScroll();
    }

    @Override
    public void notifyDataSetChanged() {
        if (mData == null)
            return;

        calcMinMax();

        if (mLegend != null)
            mLegendRenderer.computeLegend(mData);

        calculateOffsets();
    }

    @Override
    public void calculateOffsets() {

        double legendLeft = 0, legendRight = 0, legendBottom = 0, legendTop = 0;

        if (mLegend != null && mLegend.isEnabled() && !mLegend.isDrawInsideEnabled()) {

            double fullLegendWidth = Math.min(mLegend.mNeededWidth,
                    mViewPortHandler.getChartWidth() * mLegend.getMaxSizePercent());

            switch (mLegend.getOrientation()) {
                case VERTICAL: {
                    double xLegendOffset = 0.f;

                    if (mLegend.getHorizontalAlignment() == Legend.LegendHorizontalAlignment.LEFT
                            || mLegend.getHorizontalAlignment() == Legend.LegendHorizontalAlignment.RIGHT) {
                        if (mLegend.getVerticalAlignment() == Legend.LegendVerticalAlignment.CENTER) {
                            // this is the space between the legend and the chart
                            final double spacing = Utils.convertDpToPixel(13);

                            xLegendOffset = fullLegendWidth + spacing;

                        } else {
                            // this is the space between the legend and the chart
                            double spacing = Utils.convertDpToPixel(8);

                            double legendWidth = fullLegendWidth + spacing;
                            double legendHeight = mLegend.mNeededHeight + mLegend.mTextHeightMax;

                            MPPointF center = getCenter();

                            double bottomX = mLegend.getHorizontalAlignment() ==
                                    Legend.LegendHorizontalAlignment.RIGHT
                                    ? getWidth() - legendWidth + 15.f
                                    : legendWidth - 15.f;
                            double bottomY = legendHeight + 15.f;
                            double distLegend = distanceToCenter(bottomX, bottomY);

                            MPPointF reference = getPosition(center, getRadius(),
                                    getAngleForPoint(bottomX, bottomY));

                            double distReference = distanceToCenter(reference.x, reference.y);
                            double minOffset = Utils.convertDpToPixel(5);

                            if (bottomY >= center.y && getHeight() - legendWidth > getWidth()) {
                                xLegendOffset = legendWidth;
                            } else if (distLegend < distReference) {

                                double diff = distReference - distLegend;
                                xLegendOffset = minOffset + diff;
                            }

                            MPPointF.recycleInstance(center);
                            MPPointF.recycleInstance(reference);
                        }
                    }

                    switch (mLegend.getHorizontalAlignment()) {
                        case LEFT:
                            legendLeft = xLegendOffset;
                            break;

                        case RIGHT:
                            legendRight = xLegendOffset;
                            break;

                        case CENTER:
                            switch (mLegend.getVerticalAlignment()) {
                                case TOP:
                                    legendTop = Math.min(mLegend.mNeededHeight,
                                            mViewPortHandler.getChartHeight() * mLegend.getMaxSizePercent());
                                    break;
                                case BOTTOM:
                                    legendBottom = Math.min(mLegend.mNeededHeight,
                                            mViewPortHandler.getChartHeight() * mLegend.getMaxSizePercent());
                                    break;
                            }
                            break;
                    }
                }
                break;

                case HORIZONTAL:
                    double yLegendOffset = 0.f;

                    if (mLegend.getVerticalAlignment() == Legend.LegendVerticalAlignment.TOP ||
                            mLegend.getVerticalAlignment() == Legend.LegendVerticalAlignment.BOTTOM) {

                        // It's possible that we do not need this offset anymore as it
                        //   is available through the extraOffsets, but changing it can mean
                        //   changing default visibility for existing apps.
                        double yOffset = getRequiredLegendOffset();

                        yLegendOffset = Math.min(mLegend.mNeededHeight + yOffset,
                                mViewPortHandler.getChartHeight() * mLegend.getMaxSizePercent());

                        switch (mLegend.getVerticalAlignment()) {
                            case TOP:
                                legendTop = yLegendOffset;
                                break;
                            case BOTTOM:
                                legendBottom = yLegendOffset;
                                break;
                        }
                    }
                    break;
            }

            legendLeft += getRequiredBaseOffset();
            legendRight += getRequiredBaseOffset();
            legendTop += getRequiredBaseOffset();
            legendBottom += getRequiredBaseOffset();
        }

        double minOffset = Utils.convertDpToPixel(mMinOffset);

        if (this instanceof RadarChart) {
            XAxis x = this.getXAxis();

            if (x.isEnabled() && x.isDrawLabelsEnabled()) {
                minOffset = Math.max(minOffset, x.mLabelRotatedWidth);
            }
        }

        legendTop += getExtraTopOffset();
        legendRight += getExtraRightOffset();
        legendBottom += getExtraBottomOffset();
        legendLeft += getExtraLeftOffset();

        double offsetLeft = Math.max(minOffset, legendLeft);
        double offsetTop = Math.max(minOffset, legendTop);
        double offsetRight = Math.max(minOffset, legendRight);
        double offsetBottom = Math.max(minOffset, Math.max(getRequiredBaseOffset(), legendBottom));

        mViewPortHandler.restrainViewPort(offsetLeft, offsetTop, offsetRight, offsetBottom);

        if (mLogEnabled)
            Log.i(LOG_TAG, "offsetLeft: " + offsetLeft + ", offsetTop: " + offsetTop
                    + ", offsetRight: " + offsetRight + ", offsetBottom: " + offsetBottom);
    }

    /**
     * returns the angle relative to the chart center for the given point on the
     * chart in degrees. The angle is always between 0 and 360°, 0° is NORTH,
     * 90° is EAST, ...
     *
     * @param x
     * @param y
     * @return
     */
    public double getAngleForPoint(double x, double y) {

        MPPointF c = getCenterOffsets();

        double tx = x - c.x, ty = y - c.y;
        double length = Math.sqrt(tx * tx + ty * ty);
        double r = Math.acos(ty / length);

        double angle =  Math.toDegrees(r);

        if (x > c.x)
            angle = 360 - angle;

        // add 90° because chart starts EAST
        angle = angle + 90;

        // neutralize overflow
        if (angle > 360)
            angle = angle - 360;

        MPPointF.recycleInstance(c);

        return angle;
    }

    /**
     * Returns a recyclable MPPointF instance.
     * Calculates the position around a center point, depending on the distance
     * from the center, and the angle of the position around the center.
     *
     * @param center
     * @param dist
     * @param angle  in degrees, converted to radians internally
     * @return
     */
    public MPPointF getPosition(MPPointF center, double dist, double angle) {

        MPPointF p = MPPointF.getInstance(0, 0);
        getPosition(center, dist, angle, p);
        return p;
    }

    public void getPosition(MPPointF center, double dist, double angle, MPPointF outputPoint) {
        outputPoint.x =  (center.x + dist * Math.cos(Math.toRadians(angle)));
        outputPoint.y =  (center.y + dist * Math.sin(Math.toRadians(angle)));
    }

    /**
     * Returns the distance of a certain point on the chart to the center of the
     * chart.
     *
     * @param x
     * @param y
     * @return
     */
    public double distanceToCenter(double x, double y) {

        MPPointF c = getCenterOffsets();

        double dist = 0;

        double xDist = 0;
        double yDist = 0;

        if (x > c.x) {
            xDist = x - c.x;
        } else {
            xDist = c.x - x;
        }

        if (y > c.y) {
            yDist = y - c.y;
        } else {
            yDist = c.y - y;
        }

        // pythagoras
        dist =  Math.sqrt(Math.pow(xDist, 2.0) + Math.pow(yDist, 2.0));

        MPPointF.recycleInstance(c);

        return dist;
    }

    /**
     * Returns the xIndex for the given angle around the center of the chart.
     * Returns -1 if not found / outofbounds.
     *
     * @param angle
     * @return
     */
    public abstract int getIndexForAngle(double angle);

    /**
     * Set an offset for the rotation of the RadarChart in degrees. Default 270
     * --> top (NORTH)
     *
     * @param angle
     */
    public void setRotationAngle(double angle) {
        mRawRotationAngle = angle;
        mRotationAngle = Utils.getNormalizedAngle(mRawRotationAngle);
    }

    /**
     * gets the raw version of the current rotation angle of the pie chart the
     * returned value could be any value, negative or positive, outside of the
     * 360 degrees. this is used when working with rotation direction, mainly by
     * gestures and animations.
     *
     * @return
     */
    public double getRawRotationAngle() {
        return mRawRotationAngle;
    }

    /**
     * gets a normalized version of the current rotation angle of the pie chart,
     * which will always be between 0.0 < 360.0
     *
     * @return
     */
    public double getRotationAngle() {
        return mRotationAngle;
    }

    /**
     * Set this to true to enable the rotation / spinning of the chart by touch.
     * Set it to false to disable it. Default: true
     *
     * @param enabled
     */
    public void setRotationEnabled(boolean enabled) {
        mRotateEnabled = enabled;
    }

    /**
     * Returns true if rotation of the chart by touch is enabled, false if not.
     *
     * @return
     */
    public boolean isRotationEnabled() {
        return mRotateEnabled;
    }

    /**
     * Gets the minimum offset (padding) around the chart, defaults to 0.f
     */
    public double getMinOffset() {
        return mMinOffset;
    }

    /**
     * Sets the minimum offset (padding) around the chart, defaults to 0.f
     */
    public void setMinOffset(double minOffset) {
        mMinOffset = minOffset;
    }

    /**
     * returns the diameter of the pie- or radar-chart
     *
     * @return
     */
    public double getDiameter() {
        RectF content = mViewPortHandler.getContentRect();
        content.left += getExtraLeftOffset();
        content.top += getExtraTopOffset();
        content.right -= getExtraRightOffset();
        content.bottom -= getExtraBottomOffset();
        return Math.min(content.width(), content.height());
    }

    /**
     * Returns the radius of the chart in pixels.
     *
     * @return
     */
    public abstract double getRadius();

    /**
     * Returns the required offset for the chart legend.
     *
     * @return
     */
    protected abstract double getRequiredLegendOffset();

    /**
     * Returns the base offset needed for the chart without calculating the
     * legend size.
     *
     * @return
     */
    protected abstract double getRequiredBaseOffset();

    @Override
    public double getYChartMax() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double getYChartMin() {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * ################ ################ ################ ################
     */
    /** CODE BELOW THIS RELATED TO ANIMATION */

    /**
     * Applys a spin animation to the Chart.
     *
     * @param durationmillis
     * @param fromangle
     * @param toangle
     */
    @SuppressLint("NewApi")
    public void spin(int durationmillis, double fromangle, double toangle, EasingFunction easing) {

        if (android.os.Build.VERSION.SDK_INT < 11)
            return;

        setRotationAngle(fromangle);

        ObjectAnimator spinAnimator = ObjectAnimator.ofDouble(this, "rotationAngle", fromangle,
                toangle);
        spinAnimator.setDuration(durationmillis);
        spinAnimator.setInterpolator(easing);

        spinAnimator.addUpdateListener(new AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                postInvalidate();
            }
        });
        spinAnimator.start();
    }
}
