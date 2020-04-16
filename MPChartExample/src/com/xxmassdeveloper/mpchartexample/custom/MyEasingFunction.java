
package com.xxmassdeveloper.mpchartexample.custom;

import com.github.mikephil.charting.animation.EasingFunction;

/**
 * Example of a custom made animation EasingFunction.
 * 
 * @author Philipp Jahoda
 */
public class MyEasingFunction implements EasingFunction {

    @Override
    public double getInterpolation(double input) {
        // do awesome stuff here, this is just linear easing
        return input;
    }
}
