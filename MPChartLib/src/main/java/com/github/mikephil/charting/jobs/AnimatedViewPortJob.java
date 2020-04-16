package com.github.mikephil.charting.jobs;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.view.View;

import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.ViewPortHandler;

/**
 * Created by Philipp Jahoda on 19/02/16.
 */
@SuppressLint("NewApi")
public abstract class AnimatedViewPortJob extends ViewPortJob implements ValueAnimator.AnimatorUpdateListener, Animator.AnimatorListener {

    protected ObjectAnimator animator;

    protected double phase;

    protected double xOrigin;
    protected double yOrigin;

    public AnimatedViewPortJob(ViewPortHandler viewPortHandler, double xValue, double yValue, Transformer trans, View v, double xOrigin, double yOrigin, long duration) {
        super(viewPortHandler, xValue, yValue, trans, v);
        this.xOrigin = xOrigin;
        this.yOrigin = yOrigin;
        animator = ObjectAnimator.ofDouble(this, "phase", 0, 1);
        animator.setDuration(duration);
        animator.addUpdateListener(this);
        animator.addListener(this);
    }

    @SuppressLint("NewApi")
    @Override
    public void run() {
        animator.start();
    }

    public double getPhase() {
        return phase;
    }

    public void setPhase(double phase) {
        this.phase = phase;
    }

    public double getXOrigin() {
        return xOrigin;
    }

    public double getYOrigin() {
        return yOrigin;
    }

    public abstract void recycleSelf();

    protected void resetAnimator(){
        animator.removeAllListeners();
        animator.removeAllUpdateListeners();
        animator.reverse();
        animator.addUpdateListener(this);
        animator.addListener(this);
    }

    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {
        try{
            recycleSelf();
        }catch (IllegalArgumentException e){
            // don't worry about it.
        }
    }

    @Override
    public void onAnimationCancel(Animator animation) {
        try{
            recycleSelf();
        }catch (IllegalArgumentException e){
            // don't worry about it.
        }
    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {

    }
}
