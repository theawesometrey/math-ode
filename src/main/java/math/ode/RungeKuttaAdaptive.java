package math.ode;

import java.util.function.BiFunction;

import static math.ode.RungeKutta4.rk4;

/**
 * Adaptive Runge-Kutta Algorithm.
 */
public class RungeKuttaAdaptive implements FirstOrderODESolver {

    private static final double EPS = Math.ulp(1.0);

    private final double err;
    private final double initialTau;
    private final int maxTry;
    private final double safe1;
    private final double safe2;

    /**
     * Private Constructor.
     *
     * @param builder builder to set the parameters
     */
    private RungeKuttaAdaptive(Builder builder) {
        this.err = builder.err;
        this.initialTau = builder.initialTau;
        this.maxTry = builder.maxTry;
        this.safe1 = builder.safe1;
        this.safe2 = builder.safe2;
    }

    @Override
    public double solve(BiFunction<Double, Double, Double> ode, double xi, double ti, double t) {
        final int sign = t < ti ? -1 : 1;
        double tau = sign * initialTau;
        boolean done = false;
        while (!done) {
            int iTry;
            for (iTry = 1; iTry <= maxTry; ++iTry) {
                double halfTau = 0.5 * tau;
                double xSmall = rk4(ode, rk4(ode, xi, ti, halfTau),ti + halfTau, halfTau);
                double xBig = rk4(ode, xi, ti, tau);
                double errorRatio = Math.abs(xSmall - xBig) / (err * (Math.abs(xSmall) + Math.abs(xBig)) / 2.0 + EPS);
                double tauOld = tau;
                tau = (sign > 0.0)
                        ? Math.min(Math.max(safe1 * tau * Math.pow(errorRatio, -0.2), tauOld / safe2), safe2 * tauOld)
                        : Math.max(Math.min(safe1 * tau * Math.pow(errorRatio, -0.2), tauOld / safe2), safe2 * tauOld);
                if (errorRatio < 1.0) {
                    double tDiff = sign * (t - (ti + tauOld));
                    if (tDiff < 0.0) {
                        done = true;
                    } else {
                        xi = xSmall;
                        ti = ti + tauOld;
                        done = (tDiff == 0.0);
                    }
                    break;
                }
            }
            if (iTry > maxTry) {
                String errorMessage = String.format("Adaptive Runge-Kutta failed at ti = %f.", ti);
                throw new IllegalStateException(errorMessage);
            }
        }
        return t != ti ? rk4(ode, xi, ti, t - ti) : xi;
    }

    /**
     * Builder class for the Adaptive Runge-Kutta class.
     */
    public static class Builder {

        private double err;
        private double initialTau;
        private int maxTry;
        private double safe1;
        private double safe2;

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
            this.err = 1e-12;
            this.initialTau = 0.1;
            this.safe1 = 0.9;
            this.safe2 = 4.0;
            this.maxTry = 100;
        }

        /**
         * @param err desired fractional local truncation error
         * @return this
         */
        public Builder setLocalTruncationError(double err) {
            if (err < 0) throw new IllegalArgumentException("Local truncation error must be positive.");
            this.err = err;
            return this;
        }

        /**
         * @param initialTau initial step size
         * @return this
         */
        public Builder setInitialStepSize(double initialTau) {
            if (initialTau == 0.0) throw new IllegalArgumentException("Initial step size cannot be zero.");
            this.initialTau = initialTau;
            return this;
        }

        /**
         * @param maxTry maximum number of tries to converge on an acceptable step size
         * @return this
         */
        public Builder setMaximumTries(int maxTry) {
            if (maxTry < 0) throw new IllegalArgumentException("Maximum tries must be positive.");
            this.maxTry = maxTry;
            return this;
        }

        /**
         * @param safe1 safety factor 1 (value must be non-negative and less than 1.0)
         * @return this
         */
        public Builder setSafetyFactor1(double safe1) {
            if (safe1 < 0.0) throw new IllegalArgumentException("Safety Factor 1 must be non-negative.");
            if (safe1 >= 1.0) throw new IllegalArgumentException("Safety Factor 1 must be less than 1.0.");
            this.safe1 = safe1;
            return this;
        }

        /**
         * @param safe2 safety factor 2 (value must be greater than 1.0)
         * @return this
         */
        public Builder setSafetyFactor2(double safe2) {
            if (safe2 <= 1.0) throw new IllegalArgumentException("Safety Factor 2 must be greater than 1.0.");
            this.safe2 = safe2;
            return this;
        }

        /**
         * Build the Adaptive Runge-Kutta class with this builder's parameters.
         *
         * @return rka instance
         */
        public RungeKuttaAdaptive build() {
            return new RungeKuttaAdaptive(this);
        }
    }
}
