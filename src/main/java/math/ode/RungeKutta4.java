package math.ode;

import java.util.function.BiFunction;

/**
 * 4th Order Runge-Kutta Algorithm.
 */
public class RungeKutta4 implements FirstOrderODESolver {

    private final double tau;

    /**
     * Private Constructor.
     *
     * @param builder builder to set the parameters
     */
    private RungeKutta4(Builder builder) {
        this.tau = builder.tau;
    }

    /**
     * Single step 4th Order Runge-Kutta computation.
     *
     * @param ode right-hand side of first order ode: dx/dt = derivsRK(x, t)
     * @param x current value of the dependant variable
     * @param t independent variable
     * @param tau step size
     * @return new value of x after step size tau
     */
    static double rk4(BiFunction<Double, Double, Double> ode, double x, double t, double tau) {
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
            xi = rk4(ode, xi, ti, dt);
            ti += dt;
        }
        return t != ti ? rk4(ode, xi, ti, t - ti) : xi;
    }

    /**
     * Builder class for the 4th Order Runge-Kutta class.
     */
    public static class Builder {

        private double tau;

        /**
         * Get a builder instance with default settings.
         *
         * @return builder
         */
        public static Builder getInstance() {
            return new Builder();
        }

        /**
         * Constructor.
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
         * Build the 4th Order Runge-Kutta class with this builder's parameters.
         *
         * @return rka instance
         */
        public RungeKutta4 build() {
            return new RungeKutta4(this);
        }
    }
}
