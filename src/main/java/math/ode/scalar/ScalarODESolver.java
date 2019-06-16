package math.ode.scalar;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Interface that represents a Numerical Method for Solving First Order Ordinary Differential Equations(ODEs).
 */
public interface ScalarODESolver {

    /**
     * Computations for solving the First Order ODE.
     *
     * @param ode right-hand side of the first order ode dx/dt(x, t)
     * @param xi  initial condition of the dependent variable
     * @param ti  initial condition of the independent variable
     * @param t   value of independent variable
     * @return computed value of x(t)
     */
    double solve(BiFunction<Double, Double, Double> ode, double xi, double ti, double t);

    /**
     * Get the solution function for the ODE.
     *
     * @param ode right-hand side of the first order ode dx/dt(x, t)
     * @param xi  initial condition of the dependent variable
     * @param ti  initial condition of the independent variable
     * @return function x(t)
     */
    default Function<Double, Double> solution(BiFunction<Double, Double, Double> ode, double xi, double ti) {
        return t -> solve(ode, xi, ti, t);
    }
}
