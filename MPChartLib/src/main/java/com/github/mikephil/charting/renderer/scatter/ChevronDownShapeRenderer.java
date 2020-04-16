package com.github.mikephil.charting.renderer.scatter;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

/**
 * Created by wajdic on 15/06/2016.
 * Created at Time 09:08
 */
public class ChevronDownShapeRenderer implements IShapeRenderer
{


    @Override
    public void renderShape(Canvas c, IScatterDataSet dataSet, ViewPortHandler viewPortHandler,
                     double posX, double posY, Paint renderPaint) {

        final double shapeHalf = dataSet.getScatterShapeSize() / 2;

        renderPaint.setStyle(Paint.Style.STROKE);
        renderPaint.setStrokeWidth(Utils.convertDpToPixel(1));

        c.drawLine(
                posX,
                posY + (2 * shapeHalf),
                posX + (2 * shapeHalf),
                posY,
                renderPaint);

        c.drawLine(
                posX,
                posY + (2 * shapeHalf),
                posX - (2 * shapeHalf),
                posY,
                renderPaint);
    }
}
