
package com.github.mikephil.charting.utils;

import java.util.List;

/**
 * Class for describing width and height dimensions in some arbitrary
 * unit. Replacement for the android.Util.SizeF which is available only on API >= 21.
 */
public final class FSize extends ObjectPool.Poolable{

    // TODO : Encapsulate width & height

    public double width;
    public double height;

    private static ObjectPool<FSize> pool;

    static {
        pool = ObjectPool.create(256, new FSize(0,0));
        pool.setReplenishPercentage(0.5);
    }


    protected ObjectPool.Poolable instantiate(){
        return new FSize(0,0);
    }

    public static FSize getInstance(final double width, final double height){
        FSize result = pool.get();
        result.width = width;
        result.height = height;
        return result;
    }

    public static void recycleInstance(FSize instance){
        pool.recycle(instance);
    }

    public static void recycleInstances(List<FSize> instances){
        pool.recycle(instances);
    }

    public FSize() {
    }

    public FSize(final double width, final double height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (obj instanceof FSize) {
            final FSize other = (FSize) obj;
            return width == other.width && height == other.height;
        }
        return false;
    }

    @Override
    public String toString() {
        return width + "x" + height;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Double.doubleToIntBits(width) ^ Double.doubleToIntBits(height);
    }
}
