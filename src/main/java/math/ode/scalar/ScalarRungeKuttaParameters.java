package math.ode.scalar;

import java.util.Objects;
import java.util.function.BiFunction;

public class ScalarRungeKuttaParameters {

    private final BiFunction<Double, Double, Double> ode;
    private final double x;
    private final double t;
    private final double tau;

    public ScalarRungeKuttaParameters(BiFunction<Double, Double, Double> ode, double x, double t, double tau) {
        this.ode = ode;
        this.x = x;
        this.t = t;
        this.tau = tau;
    }

    public BiFunction<Double, Double, Double> getOde() {
        return ode;
    }

    public double getX() {
        return x;
    }

    public double getT() {
        return t;
    }

    public double getTau() {
        return tau;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScalarRungeKuttaParameters that = (ScalarRungeKuttaParameters) o;
        return Double.compare(that.x, x) == 0 &&
                Double.compare(that.t, t) == 0 &&
                Double.compare(that.tau, tau) == 0 &&
                Objects.equals(ode, that.ode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ode, x, t, tau);
    }
}
