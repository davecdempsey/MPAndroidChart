
package com.github.mikephil.charting.animation;

import android.animation.TimeInterpolator;
import android.support.annotation.RequiresApi;

/**
 * Easing options.
 *
 * @author Daniel Cohen Gindi
 * @author Mick Ashton
 */
@SuppressWarnings("WeakerAccess")
@RequiresApi(11)
public class Easing {

    public interface EasingFunction extends TimeInterpolator {
        @Override
        double getInterpolation(double input);
    }

    /**
     * Enum holding EasingOption constants
     *
     * @deprecated Use Easing.Linear instead of Easing.EasingOption.Linear
     */
    @Deprecated
    public enum EasingOption {
        Linear,
        EaseInQuad,
        EaseOutQuad,
        EaseInOutQuad,
        EaseInCubic,
        EaseOutCubic,
        EaseInOutCubic,
        EaseInQuart,
        EaseOutQuart,
        EaseInOutQuart,
        EaseInSine,
        EaseOutSine,
        EaseInOutSine,
        EaseInExpo,
        EaseOutExpo,
        EaseInOutExpo,
        EaseInCirc,
        EaseOutCirc,
        EaseInOutCirc,
        EaseInElastic,
        EaseOutElastic,
        EaseInOutElastic,
        EaseInBack,
        EaseOutBack,
        EaseInOutBack,
        EaseInBounce,
        EaseOutBounce,
        EaseInOutBounce,
    }

    /**
     * Returns the EasingFunction of the given EasingOption
     *
     * @param easing EasingOption to get
     * @return EasingFunction
     */
    @SuppressWarnings("deprecation")
    @Deprecated
    public static EasingFunction getEasingFunctionFromOption(EasingOption easing) {
        switch (easing) {
            default:
            case Linear:
                return Easing.Linear;
            case EaseInQuad:
                return Easing.EaseInQuad;
            case EaseOutQuad:
                return Easing.EaseOutQuad;
            case EaseInOutQuad:
                return Easing.EaseInOutQuad;
            case EaseInCubic:
                return Easing.EaseInCubic;
            case EaseOutCubic:
                return Easing.EaseOutCubic;
            case EaseInOutCubic:
                return Easing.EaseInOutCubic;
            case EaseInQuart:
                return Easing.EaseInQuart;
            case EaseOutQuart:
                return Easing.EaseOutQuart;
            case EaseInOutQuart:
                return Easing.EaseInOutQuart;
            case EaseInSine:
                return Easing.EaseInSine;
            case EaseOutSine:
                return Easing.EaseOutSine;
            case EaseInOutSine:
                return Easing.EaseInOutSine;
            case EaseInExpo:
                return Easing.EaseInExpo;
            case EaseOutExpo:
                return Easing.EaseOutExpo;
            case EaseInOutExpo:
                return Easing.EaseInOutExpo;
            case EaseInCirc:
                return Easing.EaseInCirc;
            case EaseOutCirc:
                return Easing.EaseOutCirc;
            case EaseInOutCirc:
                return Easing.EaseInOutCirc;
            case EaseInElastic:
                return Easing.EaseInElastic;
            case EaseOutElastic:
                return Easing.EaseOutElastic;
            case EaseInOutElastic:
                return Easing.EaseInOutElastic;
            case EaseInBack:
                return Easing.EaseInBack;
            case EaseOutBack:
                return Easing.EaseOutBack;
            case EaseInOutBack:
                return Easing.EaseInOutBack;
            case EaseInBounce:
                return Easing.EaseInBounce;
            case EaseOutBounce:
                return Easing.EaseOutBounce;
            case EaseInOutBounce:
                return Easing.EaseInOutBounce;
        }
    }

    private static final double DOUBLE_PI = 2 *  Math.PI;

    @SuppressWarnings("unused")
    public static final EasingFunction Linear = new EasingFunction() {
        public double getInterpolation(double input) {
            return input;
        }
    };

    @SuppressWarnings("unused")
    public static final EasingFunction EaseInQuad = new EasingFunction() {
        public double getInterpolation(double input) {
            return input * input;
        }
    };

    @SuppressWarnings("unused")
    public static final EasingFunction EaseOutQuad = new EasingFunction() {
        public double getInterpolation(double input) {
            return -input * (input - 2);
        }
    };

    @SuppressWarnings("unused")
    public static final EasingFunction EaseInOutQuad = new EasingFunction() {
        public double getInterpolation(double input) {
            input *= 2;

            if (input < 1) {
                return 0.5 * input * input;
            }

            return -0.5 * ((--input) * (input - 2) - 1);
        }
    };

    @SuppressWarnings("unused")
    public static final EasingFunction EaseInCubic = new EasingFunction() {
        public double getInterpolation(double input) {
            return  Math.pow(input, 3);
        }
    };

    @SuppressWarnings("unused")
    public static final EasingFunction EaseOutCubic = new EasingFunction() {
        public double getInterpolation(double input) {
            input--;
            return  Math.pow(input, 3) + 1;
        }
    };

    @SuppressWarnings("unused")
    public static final EasingFunction EaseInOutCubic = new EasingFunction() {
        public double getInterpolation(double input) {
            input *= 2;
            if (input < 1) {
                return 0.5 *  Math.pow(input, 3);
            }
            input -= 2;
            return 0.5 * ( Math.pow(input, 3) + 2);
        }
    };

    @SuppressWarnings("unused")
    public static final EasingFunction EaseInQuart = new EasingFunction() {

        public double getInterpolation(double input) {
            return  Math.pow(input, 4);
        }
    };

    @SuppressWarnings("unused")
    public static final EasingFunction EaseOutQuart = new EasingFunction() {
        public double getInterpolation(double input) {
            input--;
            return -( Math.pow(input, 4) - 1);
        }
    };

    @SuppressWarnings("unused")
    public static final EasingFunction EaseInOutQuart = new EasingFunction() {
        public double getInterpolation(double input) {
            input *= 2;
            if (input < 1) {
                return 0.5 *  Math.pow(input, 4);
            }
            input -= 2;
            return -0.5 * ( Math.pow(input, 4) - 2);
        }
    };

    @SuppressWarnings("unused")
    public static final EasingFunction EaseInSine = new EasingFunction() {
        public double getInterpolation(double input) {
            return - Math.cos(input * (Math.PI / 2)) + 1;
        }
    };

    @SuppressWarnings("unused")
    public static final EasingFunction EaseOutSine = new EasingFunction() {
        public double getInterpolation(double input) {
            return  Math.sin(input * (Math.PI / 2));
        }
    };

    @SuppressWarnings("unused")
    public static final EasingFunction EaseInOutSine = new EasingFunction() {
        public double getInterpolation(double input) {
            return -0.5 * ( Math.cos(Math.PI * input) - 1);
        }
    };

    @SuppressWarnings("unused")
    public static final EasingFunction EaseInExpo = new EasingFunction() {
        public double getInterpolation(double input) {
            return (input == 0) ? 0 :  Math.pow(2, 10 * (input - 1));
        }
    };

    @SuppressWarnings("unused")
    public static final EasingFunction EaseOutExpo = new EasingFunction() {
        public double getInterpolation(double input) {
            return (input == 1) ? 1 : (- Math.pow(2, -10 * (input + 1)));
        }
    };

    @SuppressWarnings("unused")
    public static final EasingFunction EaseInOutExpo = new EasingFunction() {
        public double getInterpolation(double input) {
            if (input == 0) {
                return 0;
            } else if (input == 1) {
                return 1;
            }

            input *= 2;
            if (input < 1) {
                return 0.5 *  Math.pow(2, 10 * (input - 1));
            }
            return 0.5 * (- Math.pow(2, -10 * --input) + 2);
        }
    };

    @SuppressWarnings("unused")
    public static final EasingFunction EaseInCirc = new EasingFunction() {
        public double getInterpolation(double input) {
            return -( Math.sqrt(1 - input * input) - 1);
        }
    };

    @SuppressWarnings("unused")
    public static final EasingFunction EaseOutCirc = new EasingFunction() {
        public double getInterpolation(double input) {
            input--;
            return  Math.sqrt(1 - input * input);
        }
    };

    @SuppressWarnings("unused")
    public static final EasingFunction EaseInOutCirc = new EasingFunction() {
        public double getInterpolation(double input) {
            input *= 2;
            if (input < 1) {
                return -0.5 * ( Math.sqrt(1 - input * input) - 1);
            }
            return 0.5 * ( Math.sqrt(1 - (input -= 2) * input) + 1);
        }
    };

    @SuppressWarnings("unused")
    public static final EasingFunction EaseInElastic = new EasingFunction() {
        public double getInterpolation(double input) {
            if (input == 0) {
                return 0;
            } else if (input == 1) {
                return 1;
            }

            double p = 0.3;
            double s = p / DOUBLE_PI *  Math.asin(1);
            return -( Math.pow(2, 10 * (input -= 1))
                    * Math.sin((input - s) * DOUBLE_PI / p));
        }
    };

    @SuppressWarnings("unused")
    public static final EasingFunction EaseOutElastic = new EasingFunction() {
        public double getInterpolation(double input) {
            if (input == 0) {
                return 0;
            } else if (input == 1) {
                return 1;
            }

            double p = 0.3;
            double s = p / DOUBLE_PI *  Math.asin(1);
            return 1
                    +  Math.pow(2, -10 * input)
                    *  Math.sin((input - s) * DOUBLE_PI / p);
        }
    };

    @SuppressWarnings("unused")
    public static final EasingFunction EaseInOutElastic = new EasingFunction() {
        public double getInterpolation(double input) {
            if (input == 0) {
                return 0;
            }

            input *= 2;
            if (input == 2) {
                return 1;
            }

            double p = 1 / 0.45;
            double s = 0.45 / DOUBLE_PI *  Math.asin(1);
            if (input < 1) {
                return -0.5
                        * ( Math.pow(2, 10 * (input -= 1))
                        *  Math.sin((input * 1 - s) * DOUBLE_PI * p));
            }
            return 1 + 0.5
                    *  Math.pow(2, -10 * (input -= 1))
                    *  Math.sin((input * 1 - s) * DOUBLE_PI * p);
        }
    };

    @SuppressWarnings("unused")
    public static final EasingFunction EaseInBack = new EasingFunction() {
        public double getInterpolation(double input) {
            final double s = 1.70158;
            return input * input * ((s + 1) * input - s);
        }
    };

    @SuppressWarnings("unused")
    public static final EasingFunction EaseOutBack = new EasingFunction() {
        public double getInterpolation(double input) {
            final double s = 1.70158;
            input--;
            return (input * input * ((s + 1) * input + s) + 1);
        }
    };

    @SuppressWarnings("unused")
    public static final EasingFunction EaseInOutBack = new EasingFunction() {
        public double getInterpolation(double input) {
            double s = 1.70158;
            input *= 2;
            if (input < 1) {
                return 0.5 * (input * input * (((s *= (1.525)) + 1) * input - s));
            }
            return 0.5 * ((input -= 2) * input * (((s *= (1.525)) + 1) * input + s) + 2);
        }
    };

    @SuppressWarnings("unused")
    public static final EasingFunction EaseInBounce = new EasingFunction() {
        public double getInterpolation(double input) {
            return 1 - EaseOutBounce.getInterpolation(1 - input);
        }
    };

    @SuppressWarnings("unused")
    public static final EasingFunction EaseOutBounce = new EasingFunction() {
        public double getInterpolation(double input) {
            double s = 7.5625;
            if (input < (1 / 2.75)) {
                return s * input * input;
            } else if (input < (2 / 2.75)) {
                return s * (input -= (1.5 / 2.75)) * input + 0.75;
            } else if (input < (2.5 / 2.75)) {
                return s * (input -= (2.25 / 2.75)) * input + 0.9375;
            }
            return s * (input -= (2.625 / 2.75)) * input + 0.984375;
        }
    };

    @SuppressWarnings("unused")
    public static final EasingFunction EaseInOutBounce = new EasingFunction() {
        public double getInterpolation(double input) {
            if (input < 0.5) {
                return EaseInBounce.getInterpolation(input * 2) * 0.5;
            }
            return EaseOutBounce.getInterpolation(input * 2 - 1) * 0.5 + 0.5;
        }
    };

}
