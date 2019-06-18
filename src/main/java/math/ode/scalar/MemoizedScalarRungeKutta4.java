package math.ode.scalar;

import math.ode.utils.Memoizer;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Scalar 4th Order Runge-Kutta Algorithm.
 */
public class MemoizedScalarRungeKutta4 implements ScalarODESolver {

    /**
     * Memoizer for clearing.
     */
    private Memoizer<ScalarRungeKuttaParameters, Double> memoizer = new Memoizer<>();

    /**
     * Memoized rk4 function.
     */
    private Function<ScalarRungeKuttaParameters, Double> rk4 = memoizer.doMemoize(MemoizedScalarRungeKutta4::rk4);

    /**
     * Step size.
     */
    private final double tau;

    /**
     * Private Constructor.
     *
     * @param builder builder to set the parameters
     */
    private MemoizedScalarRungeKutta4(Builder builder) {
        this.tau = builder.tau;
    }

    /**
     * Single step Scalar 4th Order Runge-Kutta computation.
     *
     * @param params call parameters
     * @return new value of x after step size tau
     */
    static double rk4(ScalarRungeKuttaParameters params) {
        BiFunction<Double, Double, Double> ode = params.getOde();
        double x = params.getX();
        double t = params.getT();
        double tau = params.getTau();
        double halfTau = 0.5 * tau;
        double tHalf = t + halfTau;
        double f1 = ode.apply(x, t);
        double f2 = ode.apply(x + halfTau * f1, tHalf);
        double f3 = ode.apply(x + halfTau * f2, tHalf);
        double f4 = ode.apply(x + tau * f3, t + tau);
        return x + tau / 6.0 * (f1 + f4 + 2.0 * (f2 + f3));
    }

    @Override
    public double solve(BiFunction<Double, Double, Double> ode, double xi, double ti, double t) {
        double dt = t < ti ? -1.0 * tau : tau;
        final double iterations = (t - ti) / dt;
        for (int i = 0; i < iterations; ++i) {
            xi = rk4.apply(new ScalarRungeKuttaParameters(ode, xi, ti, dt));
            ti += dt;
        }
        return t != ti ? rk4.apply(new ScalarRungeKuttaParameters(ode, xi, ti, t - ti)) : xi;
    }

    public void clear() {
        memoizer.clear();
    }

    /**
     * Builder class for the Scalar 4th Order Runge-Kutta class.
     */
    public static class Builder {

        /**
         * Step size.
         */
        private double tau;

        /**
         * Get a builder instance with default settings.
         *
         * @return builder
         */
        public static Builder builder() {
            return new Builder();
        }

        /**
         * Private Constructor.
         */
        private Builder() {
            this.tau = 0.1;
        }

        /**
         * @param tau initial step size
         * @return this
         */
        public Builder setStepSize(double tau) {
            if (tau == 0.0) throw new IllegalArgumentException("Step size cannot be zero.");
            this.tau = (tau > 0.0) ? tau : -tau;
            return this;
        }

        /**
         * Build the Scalar 4th Order Runge-Kutta class with this builder's parameters.
         *
         * @return rka instance
         */
        public MemoizedScalarRungeKutta4 build() {
            return new MemoizedScalarRungeKutta4(this);
        }
    }
}
