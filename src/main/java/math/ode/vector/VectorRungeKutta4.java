package math.ode.vector;

import java.util.function.BiFunction;

public class VectorRungeKutta4 implements VectorODESolver {

    private final double tau;

    /**
     * Private Constructor.
     *
     * @param builder builder to set the parameters
     */
    private VectorRungeKutta4(Builder builder) {
        this.tau = builder.tau;
    }

    /**
     * Single step 4th Order Runge-Kutta computation.
     *
     * @param ode right-hand side of first order ode: dx/dt = derivsRK(x, t)
     * @param x   current value of the dependant variable
     * @param t   independent variable
     * @param tau step size
     * @return new value of x after step size tau
     */
    static Vector rk4(BiFunction<Vector, Double, Vector> ode, Vector x, double t, double tau) {
        double halfTau = 0.5 * tau;
        double tHalf = t + halfTau;
        Vector f1 = ode.apply(x, t).immutable();
        Vector f2 = ode.apply(f1.mult(halfTau).add(x), tHalf).immutable();
        Vector f3 = ode.apply(f2.mult(halfTau).add(x), tHalf).immutable();
        Vector f4 = ode.apply(f3.mult(tau).add(x), t + tau).immutable();
        return f2.add(f3).mult(2.0).add(f1).add(f4).mult(tau / 6.0).add(x);
    }

    @Override
    public Vector solve(BiFunction<Vector, Double, Vector> ode, Vector xi, double ti, double t) {
        xi = xi.immutable();
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
        public VectorRungeKutta4 build() {
            return new VectorRungeKutta4(this);
        }
    }

}
