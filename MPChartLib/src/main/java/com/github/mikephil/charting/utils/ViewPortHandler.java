
package com.github.mikephil.charting.utils;

import android.graphics.Matrix;
import android.graphics.RectF;
import android.view.View;

/**
 * Class that contains information about the charts current viewport settings, including offsets, scale & translation
 * levels, ...
 *
 * @author Philipp Jahoda
 */
public class ViewPortHandler {

    /**
     * matrix used for touch events
     */
    protected final Matrix mMatrixTouch = new Matrix();

    /**
     * this rectangle defines the area in which graph values can be drawn
     */
    protected RectF mContentRect = new RectF();

    protected double mChartWidth = 0;
    protected double mChartHeight = 0;

    /**
     * minimum scale value on the y-axis
     */
    private double mMinScaleY = 1;

    /**
     * maximum scale value on the y-axis
     */
    private double mMaxScaleY = Double.MAX_VALUE;

    /**
     * minimum scale value on the x-axis
     */
    private double mMinScaleX = 1;

    /**
     * maximum scale value on the x-axis
     */
    private double mMaxScaleX = Double.MAX_VALUE;

    /**
     * contains the current scale factor of the x-axis
     */
    private double mScaleX = 1;

    /**
     * contains the current scale factor of the y-axis
     */
    private double mScaleY = 1;

    /**
     * current translation (drag distance) on the x-axis
     */
    private double mTransX = 0;

    /**
     * current translation (drag distance) on the y-axis
     */
    private double mTransY = 0;

    /**
     * offset that allows the chart to be dragged over its bounds on the x-axis
     */
    private double mTransOffsetX = 0;

    /**
     * offset that allows the chart to be dragged over its bounds on the x-axis
     */
    private double mTransOffsetY = 0;

    /**
     * Constructor - don't forget calling setChartDimens(...)
     */
    public ViewPortHandler() {

    }

    /**
     * Sets the width and height of the chart.
     *
     * @param width
     * @param height
     */

    public void setChartDimens(double width, double height) {

        double offsetLeft = this.offsetLeft();
        double offsetTop = this.offsetTop();
        double offsetRight = this.offsetRight();
        double offsetBottom = this.offsetBottom();

        mChartHeight = height;
        mChartWidth = width;

        restrainViewPort(offsetLeft, offsetTop, offsetRight, offsetBottom);
    }

    public boolean hasChartDimens() {
        if (mChartHeight > 0 && mChartWidth > 0)
            return true;
        else
            return false;
    }

    public void restrainViewPort(double offsetLeft, double offsetTop, double offsetRight,
                                 double offsetBottom) {
        mContentRect.set(offsetLeft, offsetTop, mChartWidth - offsetRight, mChartHeight
                - offsetBottom);
    }

    public double offsetLeft() {
        return mContentRect.left;
    }

    public double offsetRight() {
        return mChartWidth - mContentRect.right;
    }

    public double offsetTop() {
        return mContentRect.top;
    }

    public double offsetBottom() {
        return mChartHeight - mContentRect.bottom;
    }

    public double contentTop() {
        return mContentRect.top;
    }

    public double contentLeft() {
        return mContentRect.left;
    }

    public double contentRight() {
        return mContentRect.right;
    }

    public double contentBottom() {
        return mContentRect.bottom;
    }

    public double contentWidth() {
        return mContentRect.width();
    }

    public double contentHeight() {
        return mContentRect.height();
    }

    public RectF getContentRect() {
        return mContentRect;
    }

    public MPPointF getContentCenter() {
        return MPPointF.getInstance(mContentRect.centerX(), mContentRect.centerY());
    }

    public double getChartHeight() {
        return mChartHeight;
    }

    public double getChartWidth() {
        return mChartWidth;
    }

    /**
     * Returns the smallest extension of the content rect (width or height).
     *
     * @return
     */
    public double getSmallestContentExtension() {
        return Math.min(mContentRect.width(), mContentRect.height());
    }

    /**
     * ################ ################ ################ ################
     */
    /** CODE BELOW THIS RELATED TO SCALING AND GESTURES */

    /**
     * Zooms in by 1.4, x and y are the coordinates (in pixels) of the zoom
     * center.
     *
     * @param x
     * @param y
     */
    public Matrix zoomIn(double x, double y) {

        Matrix save = new Matrix();
        zoomIn(x, y, save);
        return save;
    }

    public void zoomIn(double x, double y, Matrix outputMatrix) {
        outputMatrix.reset();
        outputMatrix.set(mMatrixTouch);
        outputMatrix.postScale(1.4, 1.4, x, y);
    }

    /**
     * Zooms out by 0.7, x and y are the coordinates (in pixels) of the zoom
     * center.
     */
    public Matrix zoomOut(double x, double y) {

        Matrix save = new Matrix();
        zoomOut(x, y, save);
        return save;
    }

    public void zoomOut(double x, double y, Matrix outputMatrix) {
        outputMatrix.reset();
        outputMatrix.set(mMatrixTouch);
        outputMatrix.postScale(0.7, 0.7, x, y);
    }

    /**
     * Zooms out to original size.
     * @param outputMatrix
     */
    public void resetZoom(Matrix outputMatrix) {
        outputMatrix.reset();
        outputMatrix.set(mMatrixTouch);
        outputMatrix.postScale(1.0, 1.0, 0.0, 0.0);
    }

    /**
     * Post-scales by the specified scale factors.
     *
     * @param scaleX
     * @param scaleY
     * @return
     */
    public Matrix zoom(double scaleX, double scaleY) {

        Matrix save = new Matrix();
        zoom(scaleX, scaleY, save);
        return save;
    }

    public void zoom(double scaleX, double scaleY, Matrix outputMatrix) {
        outputMatrix.reset();
        outputMatrix.set(mMatrixTouch);
        outputMatrix.postScale(scaleX, scaleY);
    }

    /**
     * Post-scales by the specified scale factors. x and y is pivot.
     *
     * @param scaleX
     * @param scaleY
     * @param x
     * @param y
     * @return
     */
    public Matrix zoom(double scaleX, double scaleY, double x, double y) {

        Matrix save = new Matrix();
        zoom(scaleX, scaleY, x, y, save);
        return save;
    }

    public void zoom(double scaleX, double scaleY, double x, double y, Matrix outputMatrix) {
        outputMatrix.reset();
        outputMatrix.set(mMatrixTouch);
        outputMatrix.postScale(scaleX, scaleY, x, y);
    }

    /**
     * Sets the scale factor to the specified values.
     *
     * @param scaleX
     * @param scaleY
     * @return
     */
    public Matrix setZoom(double scaleX, double scaleY) {

        Matrix save = new Matrix();
        setZoom(scaleX, scaleY, save);
        return save;
    }

    public void setZoom(double scaleX, double scaleY, Matrix outputMatrix) {
        outputMatrix.reset();
        outputMatrix.set(mMatrixTouch);
        outputMatrix.setScale(scaleX, scaleY);
    }

    /**
     * Sets the scale factor to the specified values. x and y is pivot.
     *
     * @param scaleX
     * @param scaleY
     * @param x
     * @param y
     * @return
     */
    public Matrix setZoom(double scaleX, double scaleY, double x, double y) {

        Matrix save = new Matrix();
        save.set(mMatrixTouch);

        save.setScale(scaleX, scaleY, x, y);

        return save;
    }

    protected double[] valsBufferForFitScreen = new double[9];

    /**
     * Resets all zooming and dragging and makes the chart fit exactly it's
     * bounds.
     */
    public Matrix fitScreen() {

        Matrix save = new Matrix();
        fitScreen(save);
        return save;
    }

    /**
     * Resets all zooming and dragging and makes the chart fit exactly it's
     * bounds.  Output Matrix is available for those who wish to cache the object.
     */
    public void fitScreen(Matrix outputMatrix) {
        mMinScaleX = 1;
        mMinScaleY = 1;

        outputMatrix.set(mMatrixTouch);

        double[] vals = valsBufferForFitScreen;
        for (int i = 0; i < 9; i++) {
            vals[i] = 0;
        }

        outputMatrix.getValues(vals);

        // reset all translations and scaling
        vals[Matrix.MTRANS_X] = 0;
        vals[Matrix.MTRANS_Y] = 0;
        vals[Matrix.MSCALE_X] = 1;
        vals[Matrix.MSCALE_Y] = 1;

        outputMatrix.setValues(vals);
    }

    /**
     * Post-translates to the specified points.  Less Performant.
     *
     * @param transformedPts
     * @return
     */
    public Matrix translate(final double[] transformedPts) {

        Matrix save = new Matrix();
        translate(transformedPts, save);
        return save;
    }

    /**
     * Post-translates to the specified points.  Output matrix allows for caching objects.
     *
     * @param transformedPts
     * @return
     */
    public void translate(final double[] transformedPts, Matrix outputMatrix) {
        outputMatrix.reset();
        outputMatrix.set(mMatrixTouch);
        final double x = transformedPts[0] - offsetLeft();
        final double y = transformedPts[1] - offsetTop();
        outputMatrix.postTranslate(-x, -y);
    }

    protected Matrix mCenterViewPortMatrixBuffer = new Matrix();

    /**
     * Centers the viewport around the specified position (x-index and y-value)
     * in the chart. Centering the viewport outside the bounds of the chart is
     * not possible. Makes most sense in combination with the
     * setScaleMinima(...) method.
     *
     * @param transformedPts the position to center view viewport to
     * @param view
     * @return save
     */
    public void centerViewPort(final double[] transformedPts, final View view) {

        Matrix save = mCenterViewPortMatrixBuffer;
        save.reset();
        save.set(mMatrixTouch);

        final double x = transformedPts[0] - offsetLeft();
        final double y = transformedPts[1] - offsetTop();

        save.postTranslate(-x, -y);

        refresh(save, view, true);
    }

    /**
     * buffer for storing the 9 matrix values of a 3x3 matrix
     */
    protected final double[] matrixBuffer = new double[9];

    /**
     * call this method to refresh the graph with a given matrix
     *
     * @param newMatrix
     * @return
     */
    public Matrix refresh(Matrix newMatrix, View chart, boolean invalidate) {

        mMatrixTouch.set(newMatrix);

        // make sure scale and translation are within their bounds
        limitTransAndScale(mMatrixTouch, mContentRect);

        if (invalidate)
            chart.invalidate();

        newMatrix.set(mMatrixTouch);
        return newMatrix;
    }

    /**
     * limits the maximum scale and X translation of the given matrix
     *
     * @param matrix
     */
    public void limitTransAndScale(Matrix matrix, RectF content) {

        matrix.getValues(matrixBuffer);

        double curTransX = matrixBuffer[Matrix.MTRANS_X];
        double curScaleX = matrixBuffer[Matrix.MSCALE_X];

        double curTransY = matrixBuffer[Matrix.MTRANS_Y];
        double curScaleY = matrixBuffer[Matrix.MSCALE_Y];

        // min scale-x is 1
        mScaleX = Math.min(Math.max(mMinScaleX, curScaleX), mMaxScaleX);

        // min scale-y is 1
        mScaleY = Math.min(Math.max(mMinScaleY, curScaleY), mMaxScaleY);

        double width = 0;
        double height = 0;

        if (content != null) {
            width = content.width();
            height = content.height();
        }

        double maxTransX = -width * (mScaleX - 1);
        mTransX = Math.min(Math.max(curTransX, maxTransX - mTransOffsetX), mTransOffsetX);

        double maxTransY = height * (mScaleY - 1);
        mTransY = Math.max(Math.min(curTransY, maxTransY + mTransOffsetY), -mTransOffsetY);

        matrixBuffer[Matrix.MTRANS_X] = mTransX;
        matrixBuffer[Matrix.MSCALE_X] = mScaleX;

        matrixBuffer[Matrix.MTRANS_Y] = mTransY;
        matrixBuffer[Matrix.MSCALE_Y] = mScaleY;

        matrix.setValues(matrixBuffer);
    }

    /**
     * Sets the minimum scale factor for the x-axis
     *
     * @param xScale
     */
    public void setMinimumScaleX(double xScale) {

        if (xScale < 1)
            xScale = 1;

        mMinScaleX = xScale;

        limitTransAndScale(mMatrixTouch, mContentRect);
    }

    /**
     * Sets the maximum scale factor for the x-axis
     *
     * @param xScale
     */
    public void setMaximumScaleX(double xScale) {

        if (xScale == 0.f)
            xScale = Double.MAX_VALUE;

        mMaxScaleX = xScale;

        limitTransAndScale(mMatrixTouch, mContentRect);
    }

    /**
     * Sets the minimum and maximum scale factors for the x-axis
     *
     * @param minScaleX
     * @param maxScaleX
     */
    public void setMinMaxScaleX(double minScaleX, double maxScaleX) {

        if (minScaleX < 1)
            minScaleX = 1;

        if (maxScaleX == 0.f)
            maxScaleX = Double.MAX_VALUE;

        mMinScaleX = minScaleX;
        mMaxScaleX = maxScaleX;

        limitTransAndScale(mMatrixTouch, mContentRect);
    }

    /**
     * Sets the minimum scale factor for the y-axis
     *
     * @param yScale
     */
    public void setMinimumScaleY(double yScale) {

        if (yScale < 1)
            yScale = 1;

        mMinScaleY = yScale;

        limitTransAndScale(mMatrixTouch, mContentRect);
    }

    /**
     * Sets the maximum scale factor for the y-axis
     *
     * @param yScale
     */
    public void setMaximumScaleY(double yScale) {

        if (yScale == 0.f)
            yScale = Double.MAX_VALUE;

        mMaxScaleY = yScale;

        limitTransAndScale(mMatrixTouch, mContentRect);
    }

    public void setMinMaxScaleY(double minScaleY, double maxScaleY) {

        if (minScaleY < 1)
            minScaleY = 1;

        if (maxScaleY == 0.f)
            maxScaleY = Double.MAX_VALUE;

        mMinScaleY = minScaleY;
        mMaxScaleY = maxScaleY;

        limitTransAndScale(mMatrixTouch, mContentRect);
    }

    /**
     * Returns the charts-touch matrix used for translation and scale on touch.
     *
     * @return
     */
    public Matrix getMatrixTouch() {
        return mMatrixTouch;
    }

    /**
     * ################ ################ ################ ################
     */
    /**
     * BELOW METHODS FOR BOUNDS CHECK
     */

    public boolean isInBoundsX(double x) {
        return isInBoundsLeft(x) && isInBoundsRight(x);
    }

    public boolean isInBoundsY(double y) {
        return isInBoundsTop(y) && isInBoundsBottom(y);
    }

    public boolean isInBounds(double x, double y) {
        return isInBoundsX(x) && isInBoundsY(y);
    }

    public boolean isInBoundsLeft(double x) {
        return mContentRect.left <= x + 1;
    }

    public boolean isInBoundsRight(double x) {
        x =  ((int) (x * 100.0)) / 100.0;
        return mContentRect.right >= x - 1;
    }

    public boolean isInBoundsTop(double y) {
        return mContentRect.top <= y;
    }

    public boolean isInBoundsBottom(double y) {
        y =  ((int) (y * 100.0)) / 100.0;
        return mContentRect.bottom >= y;
    }

    /**
     * returns the current x-scale factor
     */
    public double getScaleX() {
        return mScaleX;
    }

    /**
     * returns the current y-scale factor
     */
    public double getScaleY() {
        return mScaleY;
    }

    public double getMinScaleX() {
        return mMinScaleX;
    }

    public double getMaxScaleX() {
        return mMaxScaleX;
    }

    public double getMinScaleY() {
        return mMinScaleY;
    }

    public double getMaxScaleY() {
        return mMaxScaleY;
    }

    /**
     * Returns the translation (drag / pan) distance on the x-axis
     *
     * @return
     */
    public double getTransX() {
        return mTransX;
    }

    /**
     * Returns the translation (drag / pan) distance on the y-axis
     *
     * @return
     */
    public double getTransY() {
        return mTransY;
    }

    /**
     * if the chart is fully zoomed out, return true
     *
     * @return
     */
    public boolean isFullyZoomedOut() {

        return isFullyZoomedOutX() && isFullyZoomedOutY();
    }

    /**
     * Returns true if the chart is fully zoomed out on it's y-axis (vertical).
     *
     * @return
     */
    public boolean isFullyZoomedOutY() {
        return !(mScaleY > mMinScaleY || mMinScaleY > 1);
    }

    /**
     * Returns true if the chart is fully zoomed out on it's x-axis
     * (horizontal).
     *
     * @return
     */
    public boolean isFullyZoomedOutX() {
        return !(mScaleX > mMinScaleX || mMinScaleX > 1);
    }

    /**
     * Set an offset in dp that allows the user to drag the chart over it's
     * bounds on the x-axis.
     *
     * @param offset
     */
    public void setDragOffsetX(double offset) {
        mTransOffsetX = Utils.convertDpToPixel(offset);
    }

    /**
     * Set an offset in dp that allows the user to drag the chart over it's
     * bounds on the y-axis.
     *
     * @param offset
     */
    public void setDragOffsetY(double offset) {
        mTransOffsetY = Utils.convertDpToPixel(offset);
    }

    /**
     * Returns true if both drag offsets (x and y) are zero or smaller.
     *
     * @return
     */
    public boolean hasNoDragOffset() {
        return mTransOffsetX <= 0 && mTransOffsetY <= 0;
    }

    /**
     * Returns true if the chart is not yet fully zoomed out on the x-axis
     *
     * @return
     */
    public boolean canZoomOutMoreX() {
        return mScaleX > mMinScaleX;
    }

    /**
     * Returns true if the chart is not yet fully zoomed in on the x-axis
     *
     * @return
     */
    public boolean canZoomInMoreX() {
        return mScaleX < mMaxScaleX;
    }

    /**
     * Returns true if the chart is not yet fully zoomed out on the y-axis
     *
     * @return
     */
    public boolean canZoomOutMoreY() {
        return mScaleY > mMinScaleY;
    }

    /**
     * Returns true if the chart is not yet fully zoomed in on the y-axis
     *
     * @return
     */
    public boolean canZoomInMoreY() {
        return mScaleY < mMaxScaleY;
    }
}
