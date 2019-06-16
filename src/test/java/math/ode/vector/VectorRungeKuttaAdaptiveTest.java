package math.ode.vector;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Random;
import java.util.function.Function;
import java.util.stream.IntStream;

public class VectorRungeKuttaAdaptiveTest {

    private static Object[] randomMotionCase(Random rand) {
        // Initial parameters
        int n = 3;
        Vector a = Vector.mutable(n);
        Vector vi = Vector.mutable(n);
        Vector xi = Vector.mutable(n);
        for (int i = 0; i < n; ++i) {
            a.set(i, (rand.nextDouble() - 0.5) * 20.0);
            vi.set(i, (rand.nextDouble() - 0.5) * 100.0);
            xi.set(i, (rand.nextDouble() - 0.5) * 200.0);
        }
        double ti = (rand.nextDouble() - 0.5) * 20.0;
        // Expected equation of motion
        Function<Double, Vector> xExp = t -> xi.immutable()
                .add(vi.immutable().mult(t - ti))
                .add(a.immutable().mult((t - ti) * (t - ti)).div(2.0));
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

    @Test(dataProvider = "createMotionTests")
    public void testMotion(Function<Double, Vector> xExp, Vector a, Vector vi, Vector xi, double ti) {
        // Velocity function
        Function<Double, Vector> v = VectorRungeKuttaAdaptive.Builder.getInstance()
                .setInitialStepSize(0.1)
                .setLocalTruncationError(1e-9)
                .build()
                .solution((vel, t) -> a, vi, ti);
        // Position function
        Function<Double, Vector> x = VectorRungeKuttaAdaptive.Builder.getInstance()
                .setInitialStepSize(0.1)
                .setLocalTruncationError(1e-9)
                .build()
                .solution((xPos, t) -> v.apply(t), xi, ti);
        // Assert that the position function matches the expected
        for (double time = -10.0; time <= 10.0; time += 1.0) {
            Vector actual = x.apply(time);
            Vector expected = xExp.apply(time);
            Assert.assertEquals(actual.get(0), expected.get(0), 1e-6);
            Assert.assertEquals(actual.get(1), expected.get(1), 1e-6);
            Assert.assertEquals(actual.get(2), expected.get(2), 1e-6);
        }
    }

    private static Object[] randomSpringCase(Random rand) {
        // Initial parameters
        double period = rand.nextDouble() * 5.0 + 5.0;
        double omega = 2.0 * Math.PI / period;
        double ti = 0.0;
        double vi = 0.0;
        double xi = rand.nextDouble() * 2.0 + 0.1;
        // Expected equation of motion
        Function<Double, Double> xExp = t -> xi * Math.cos(omega * (t - ti)) + vi * Math.sin(omega * (t - ti));
        return new Object[]{xExp, omega, xi, vi, ti};
    }

    @DataProvider
    public Object[][] createSpringTests() {
        Random rand = new Random();
        rand.setSeed(94789234);
        final int numTests = 1;
        return IntStream.range(0, numTests)
                .mapToObj(i -> randomSpringCase(rand))
                .toArray(Object[][]::new);
    }

    @Test(dataProvider = "createSpringTests")
    public void testSpring(Function<Double, Double> xExp, double omega, double xi, double vi, double ti) {
        Vector vxi = Vector.mutable(vi, xi);
        // Position/Velocity function
        Function<Double, Vector> vx = VectorRungeKutta4.Builder.getInstance()
                .setStepSize(0.0001)
                .build()
                .solution((vec, tim) -> Vector.mutable(-omega * vec.get(1), vec.get(0)), vxi, ti);
        // Assert that the position function matches the expected
        for (double time = -10.0; time <= 10.0; time += 0.1) {
            Vector actual = vx.apply(time);
            System.out.println(time + ", " + actual.get(1));
//            double expected = xExp.apply(time);
//            System.out.println(time + ", " + expected);
//            Assert.assertEquals(actual.get(1), expected, 0.1);
        }
    }
}