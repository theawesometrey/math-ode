package math.ode;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.IntStream;

public class RungeKuttaAdaptiveTest {

    @DataProvider
    public Object[][] createSolveTests() {
        return new Object[][] {
                {(BiFunction<Double, Double, Double>) (x, t) -> t,
                        (Function<Double, Double>) (t) -> t * t / 2.0,
                        8.0, -4.0},
                {(BiFunction<Double, Double, Double>) (x, t) -> x * t / 10.0,
                        (Function<Double, Double>) (t) -> -20 * Math.exp(t * t / 20.0),
                        -20.0, 0.0},
                {(BiFunction<Double, Double, Double>) (x, t) -> Math.sqrt(x) * t,
                        (Function<Double, Double>) (t) -> Math.pow(Math.pow(t, 2.0) + 4.0, 2) / 16.0,
                        25.0, 4.0}
        };
    }

    @Test(dataProvider="createSolveTests")
    public void testSolution(BiFunction<Double, Double, Double> dx, Function<Double, Double> xExp, double xi, double ti) {
        Function<Double, Double> xAct = RungeKuttaAdaptive.Builder.getInstance()
                .setLocalTruncationError(1e-12)
                .setInitialStepSize(0.03)
                .setMaximumTries(100)
                .setSafetyFactor1(0.9)
                .setSafetyFactor2(4.0)
                .build()
                .solution(dx, xi, ti);
        for (double time = -10.0; time <= 10.0; time += 1.0) {
            double expected = xExp.apply(time);
            double actual = xAct.apply(time);
            Assert.assertEquals(actual, expected, 1e-6);
        }
    }

    private static Object[] randomMotionCase(Random rand) {
        // Initial parameters
        double a = (rand.nextDouble() - 0.5) * 20.0;
        double vi = (rand.nextDouble() - 0.5) * 100.0;
        double xi = (rand.nextDouble() - 0.5) * 200.0;
        double ti = (rand.nextDouble() - 0.5) * 20.0;
        // Expected equation of motion
        Function<Double, Double> xExp = t -> xi + vi * (t - ti) + 0.5 * a * (t - ti) * (t - ti);
        return new Object[]{xExp, a, vi, xi, ti};
    }

    @DataProvider
    public Object[][] createMotionTests() {
        Random rand = new Random();
        rand.setSeed(94789234);
        final int numTests = 5;
        return IntStream.range(0, numTests)
                .mapToObj(i -> randomMotionCase(rand))
                .toArray(Object[][]::new);
    }

    @Test(dataProvider="createMotionTests")
    public void testMotion(Function<Double, Double> xExp, double a, double vi, double xi, double ti) {
        // Velocity function
        Function<Double, Double> v = RungeKuttaAdaptive.Builder.getInstance()
                .setInitialStepSize(0.1)
                .build()
                .solution((vel, t) -> a, vi, ti);
        // Position function
        Function<Double, Double> x = RungeKuttaAdaptive.Builder.getInstance()
                .setInitialStepSize(0.1)
                .build()
                .solution((xPos, t) -> v.apply(t), xi, ti);
        // Assert that the position function matches the expected
        for (double time = -10.0; time <= 10.0; time += 1.0) {
            double actual = x.apply(time);
            double expected = xExp.apply(time);
            Assert.assertEquals(actual, expected, 1e-6);
        }
    }
}