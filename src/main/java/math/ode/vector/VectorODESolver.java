package math.ode.vector;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Interface that represents a Numerical Method for Solving Vector First Order Ordinary Differential Equations(ODEs).
 */
public interface VectorODESolver {

    /**
     * Computations for solving the Vector First Order Vector ODE.
     *
     * @param ode right-hand side of the first order ode dx/dt(x, t)
     * @param xi  initial condition of the dependent variables
     * @param ti  initial condition of the independent variable
     * @param t   desired value of independent variable
     * @return computed value of x(t)
     */
    Vector solve(BiFunction<Vector, Double, Vector> ode, Vector xi, double ti, double t);

    /**
     * Get the solution function for the Vector ODE.
     *
     * @param ode right-hand side of the first order ode dx/dt(x, t)
     * @param xi  initial condition of the dependent variables
     * @param ti  initial condition of the independent variable
     * @return function x(t)
     */
    default Function<Double, Vector> solution(BiFunction<Vector, Double, Vector> ode, Vector xi, double ti) {
        return t -> solve(ode, xi, ti, t);
    }
}
