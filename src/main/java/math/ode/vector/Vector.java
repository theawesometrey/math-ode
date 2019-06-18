package math.ode.vector;

import java.util.Arrays;
import java.util.function.Function;

import static math.ode.vector.VectorType.IMMUTABLE;
import static math.ode.vector.VectorType.MUTABLE;

public class Vector {

    private static final VectorType DEFAULT_TYPE = MUTABLE;

    private VectorType type;
    private final double[] x;

    public static Vector mutable(int size) {
        return create(MUTABLE, new double[size]);
    }

    public static Vector randomMutable(int size) {
        return random(MUTABLE, size);
    }

    public static Vector mutable(int size, double fill) {
        double[] out = new double[size];
        Arrays.fill(out, fill);
        return create(MUTABLE, out);
    }

    public static Vector mutable(double... x) {
        return create(MUTABLE, x);
    }

    public static Vector immutable(int size) {
        return create(IMMUTABLE, new double[size]);
    }

    public static Vector randomImmutable(int size) {
        return random(IMMUTABLE, size);
    }

    public static Vector immutable(int size, double fill) {
        double[] out = new double[size];
        Arrays.fill(out, fill);
        return create(IMMUTABLE, out);
    }

    public static Vector immutable(double... x) {
        return create(IMMUTABLE, x);
    }
    
    public static Vector create(VectorType type, int size) {
        return new Vector(type, new double[size]);
    }

    public static Vector random(VectorType type, int size) {
        double[] out = new double[size];
        for (int i = 0; i < size; ++i) {
            out[i] = Math.random();
        }
        return create(type, out);
    }

    public static Vector create(VectorType type, double... x) {
        return new Vector(type, x);
    }

    private Vector(VectorType type, double[] x) {
        this.type = type;
        this.x = x;
    }

    private static double[] arrayCopy(double[] array) {
        int n = array.length;
        double[] out = new double[n];
        System.arraycopy(array, 0, out, 0, n);
        return out;
    }

    public Vector immutable() {
        return (this.type == IMMUTABLE) ? this : new Vector(IMMUTABLE, arrayCopy(x));
    }

    public Vector mutable() {
        return (this.type == MUTABLE) ? this :new Vector(MUTABLE, arrayCopy(x));
    }

    public Vector add(double scalar) {
        return add(scalar, DEFAULT_TYPE);
    }

    public Vector add(double scalar, VectorType outType) {
        Vector out = (outType == IMMUTABLE)
                ? Vector.immutable(x.length)
                : (this.type == IMMUTABLE) ? Vector.mutable(x.length) : this;
        for (int i = 0; i < x.length; ++i) {
            out.x[i] = x[i] + scalar;
        }
        return out;
    }

    public Vector add(Vector vector) {
        return add(vector, DEFAULT_TYPE);
    }

    public Vector add(Vector vector, VectorType outType) {
        Vector out = (outType == IMMUTABLE)
                ? Vector.immutable(x.length)
                : (this.type == IMMUTABLE) ? Vector.mutable(x.length) : this;
        for (int i = 0; i < x.length; ++i) {
            out.x[i] = x[i] + vector.x[i];
        }
        return out;
    }

    public Vector sub(double scalar) {
        return sub(scalar, DEFAULT_TYPE);
    }

    public Vector sub(double scalar, VectorType outType) {
        Vector out = (outType == IMMUTABLE)
                ? Vector.immutable(x.length)
                : (this.type == IMMUTABLE) ? Vector.mutable(x.length) : this;
        for (int i = 0; i < x.length; ++i) {
            out.x[i] = x[i] - scalar;
        }
        return out;
    }

    public Vector sub(Vector vector) {
        return sub(vector, DEFAULT_TYPE);
    }

    public Vector sub(Vector vector, VectorType outType) {
        Vector out = (outType == IMMUTABLE)
                ? Vector.immutable(x.length)
                : (this.type == IMMUTABLE) ? Vector.mutable(x.length) : this;
        for (int i = 0; i < x.length; ++i) {
            out.x[i] = x[i] - vector.x[i];
        }
        return out;
    }

    public Vector mult(double scalar) {
        return mult(scalar, DEFAULT_TYPE);
    }

    public Vector mult(double scalar, VectorType outType) {
        Vector out = (outType == IMMUTABLE)
                ? Vector.immutable(x.length)
                : (this.type == IMMUTABLE) ? Vector.mutable(x.length) : this;
        for (int i = 0; i < x.length; ++i) {
            out.x[i] = x[i] * scalar;
        }
        return out;
    }

    public Vector mult(Vector vector) {
        return mult(vector, DEFAULT_TYPE);
    }

    public Vector mult(Vector vector, VectorType outType) {
        Vector out = (outType == IMMUTABLE)
                ? Vector.immutable(x.length)
                : (this.type == IMMUTABLE) ? Vector.mutable(x.length) : this;
        for (int i = 0; i < x.length; ++i) {
            out.x[i] = x[i] * vector.x[i];
        }
        return out;
    }

    public Vector div(double scalar) {
        return div(scalar, DEFAULT_TYPE);
    }

    public Vector div(double scalar, VectorType outType) {
        Vector out = (outType == IMMUTABLE)
                ? Vector.immutable(x.length)
                : (this.type == IMMUTABLE) ? Vector.mutable(x.length) : this;
        for (int i = 0; i < x.length; ++i) {
            out.x[i] = x[i] / scalar;
        }
        return out;
    }

    public Vector div(Vector vector) {
        return div(vector, DEFAULT_TYPE);
    }

    public Vector div(Vector vector, VectorType outType) {
        Vector out = (outType == IMMUTABLE)
                ? Vector.immutable(x.length)
                : (this.type == IMMUTABLE) ? Vector.mutable(x.length) : this;
        for (int i = 0; i < x.length; ++i) {
            out.x[i] = x[i] / vector.x[i];
        }
        return out;
    }

    public Vector negate() {
        return negate(DEFAULT_TYPE);
    }

    public Vector negate(VectorType outType) {
        Vector out = (outType == IMMUTABLE)
                ? Vector.immutable(x.length)
                : (this.type == IMMUTABLE) ? Vector.mutable(x.length) : this;
        for (int i = 0; i < x.length; ++i) {
            out.x[i] = -x[i];
        }
        return out;
    }

    public Vector inverse() {
        return inverse(DEFAULT_TYPE);
    }

    public Vector inverse(VectorType outType) {
        Vector out = (outType == IMMUTABLE)
                ? Vector.immutable(x.length)
                : (this.type == IMMUTABLE) ? Vector.mutable(x.length) : this;
        for (int i = 0; i < x.length; ++i) {
            out.x[i] = 1.0 / x[i];
        }
        return out;
    }

    public Vector apply(Function<Double, Double> function) {
        return apply(function, DEFAULT_TYPE);
    }

    public Vector apply(Function<Double, Double> function, VectorType outType) {
        Vector out = (outType == IMMUTABLE)
                ? Vector.immutable(x.length)
                : (this.type == IMMUTABLE) ? Vector.mutable(x.length) : this;
        for (int i = 0; i < x.length; ++i) {
            out.x[i] = function.apply(x[i]);
        }
        return out;
    }

    public Vector scalb(int scale) {
        return scalb(scale, DEFAULT_TYPE);
    }

    public Vector scalb(int scale, VectorType outType) {
        Vector out = (outType == IMMUTABLE)
                ? Vector.immutable(x.length)
                : (this.type == IMMUTABLE) ? Vector.mutable(x.length) : this;
        for (int i = 0; i < x.length; ++i) {
            out.x[i] = Math.scalb(x[i], scale);
        }
        return out;
    }

    public Vector pow(double exp) {
        return pow(exp, DEFAULT_TYPE);
    }

    public Vector pow(double exp, VectorType outType) {
        Vector out = (outType == IMMUTABLE)
                ? Vector.immutable(x.length)
                : (this.type == IMMUTABLE) ? Vector.mutable(x.length) : this;
        for (int i = 0; i < x.length; ++i) {
            out.x[i] = Math.pow(x[i], exp);
        }
        return out;
    }

    public Vector signum() {
        return signum(DEFAULT_TYPE);
    }

    public Vector signum(VectorType outType) {
        Vector out = (outType == IMMUTABLE)
                ? Vector.immutable(x.length)
                : (this.type == IMMUTABLE) ? Vector.mutable(x.length) : this;
        for (int i = 0; i < x.length; ++i) {
            out.x[i] = Math.signum(x[i]);
        }
        return out;
    }

    public Vector abs() {
        return abs(DEFAULT_TYPE);
    }

    public Vector abs(VectorType outType) {
        Vector out = (outType == IMMUTABLE)
                ? Vector.immutable(x.length)
                : (this.type == IMMUTABLE) ? Vector.mutable(x.length) : this;
        for (int i = 0; i < x.length; ++i) {
            out.x[i] = Math.abs(x[i]);
        }
        return out;
    }

    public Vector exp() {
        return exp(DEFAULT_TYPE);
    }

    public Vector exp(VectorType outType) {
        Vector out = (outType == IMMUTABLE)
                ? Vector.immutable(x.length)
                : (this.type == IMMUTABLE) ? Vector.mutable(x.length) : this;
        for (int i = 0; i < x.length; ++i) {
            out.x[i] = Math.exp(x[i]);
        }
        return out;
    }

    public Vector expm1() {
        return expm1(DEFAULT_TYPE);
    }

    public Vector expm1(VectorType outType) {
        Vector out = (outType == IMMUTABLE)
                ? Vector.immutable(x.length)
                : (this.type == IMMUTABLE) ? Vector.mutable(x.length) : this;
        for (int i = 0; i < x.length; ++i) {
            out.x[i] = Math.expm1(x[i]);
        }
        return out;
    }

    public Vector log() {
        return log(DEFAULT_TYPE);
    }

    public Vector log(VectorType outType) {
        Vector out = (outType == IMMUTABLE)
                ? Vector.immutable(x.length)
                : (this.type == IMMUTABLE) ? Vector.mutable(x.length) : this;
        for (int i = 0; i < x.length; ++i) {
            out.x[i] = Math.log(x[i]);
        }
        return out;
    }

    public Vector log10() {
        return log10(DEFAULT_TYPE);
    }

    public Vector log10(VectorType outType) {
        Vector out = (outType == IMMUTABLE)
                ? Vector.immutable(x.length)
                : (this.type == IMMUTABLE) ? Vector.mutable(x.length) : this;
        for (int i = 0; i < x.length; ++i) {
            out.x[i] = Math.log10(x[i]);
        }
        return out;
    }

    public Vector log1p() {
        return log1p(DEFAULT_TYPE);
    }

    public Vector log1p(VectorType outType) {
        Vector out = (outType == IMMUTABLE)
                ? Vector.immutable(x.length)
                : (this.type == IMMUTABLE) ? Vector.mutable(x.length) : this;
        for (int i = 0; i < x.length; ++i) {
            out.x[i] = Math.log1p(x[i]);
        }
        return out;
    }

    public Vector cbrt() {
        return cbrt(DEFAULT_TYPE);
    }

    public Vector cbrt(VectorType outType) {
        Vector out = (outType == IMMUTABLE)
                ? Vector.immutable(x.length)
                : (this.type == IMMUTABLE) ? Vector.mutable(x.length) : this;
        for (int i = 0; i < x.length; ++i) {
            out.x[i] = Math.cbrt(x[i]);
        }
        return out;
    }

    public Vector sqrt() {
        return sqrt(DEFAULT_TYPE);
    }

    public Vector sqrt(VectorType outType) {
        Vector out = (outType == IMMUTABLE)
                ? Vector.immutable(x.length)
                : (this.type == IMMUTABLE) ? Vector.mutable(x.length) : this;
        for (int i = 0; i < x.length; ++i) {
            out.x[i] = Math.sqrt(x[i]);
        }
        return out;
    }

    public Vector sin() {
        return sin(DEFAULT_TYPE);
    }

    public Vector sin(VectorType outType) {
        Vector out = (outType == IMMUTABLE)
                ? Vector.immutable(x.length)
                : (this.type == IMMUTABLE) ? Vector.mutable(x.length) : this;
        for (int i = 0; i < x.length; ++i) {
            out.x[i] = Math.sin(x[i]);
        }
        return out;
    }

    public Vector asin() {
        return asin(DEFAULT_TYPE);
    }

    public Vector asin(VectorType outType) {
        Vector out = (outType == IMMUTABLE)
                ? Vector.immutable(x.length)
                : (this.type == IMMUTABLE) ? Vector.mutable(x.length) : this;
        for (int i = 0; i < x.length; ++i) {
            out.x[i] = Math.asin(x[i]);
        }
        return out;
    }

    public Vector sinh() {
        return sinh(DEFAULT_TYPE);
    }

    public Vector sinh(VectorType outType) {
        Vector out = (outType == IMMUTABLE)
                ? Vector.immutable(x.length)
                : (this.type == IMMUTABLE) ? Vector.mutable(x.length) : this;
        for (int i = 0; i < x.length; ++i) {
            out.x[i] = Math.sinh(x[i]);
        }
        return out;
    }

    public Vector cos() {
        return cos(DEFAULT_TYPE);
    }

    public Vector cos(VectorType outType) {
        Vector out = (outType == IMMUTABLE)
                ? Vector.immutable(x.length)
                : (this.type == IMMUTABLE) ? Vector.mutable(x.length) : this;
        for (int i = 0; i < x.length; ++i) {
            out.x[i] = Math.cos(x[i]);
        }
        return out;
    }

    public Vector acos() {
        return acos(DEFAULT_TYPE);
    }

    public Vector acos(VectorType outType) {
        Vector out = (outType == IMMUTABLE)
                ? Vector.immutable(x.length)
                : (this.type == IMMUTABLE) ? Vector.mutable(x.length) : this;
        for (int i = 0; i < x.length; ++i) {
            out.x[i] = Math.acos(x[i]);
        }
        return out;
    }

    public Vector cosh() {
        return cosh(DEFAULT_TYPE);
    }

    public Vector cosh(VectorType outType) {
        Vector out = (outType == IMMUTABLE)
                ? Vector.immutable(x.length)
                : (this.type == IMMUTABLE) ? Vector.mutable(x.length) : this;
        for (int i = 0; i < x.length; ++i) {
            out.x[i] = Math.cosh(x[i]);
        }
        return out;
    }

    public Vector tan() {
        return tan(DEFAULT_TYPE);
    }

    public Vector tan(VectorType outType) {
        Vector out = (outType == IMMUTABLE)
                ? Vector.immutable(x.length)
                : (this.type == IMMUTABLE) ? Vector.mutable(x.length) : this;
        for (int i = 0; i < x.length; ++i) {
            out.x[i] = Math.tan(x[i]);
        }
        return out;
    }

    public Vector atan() {
        return atan(DEFAULT_TYPE);
    }

    public Vector atan(VectorType outType) {
        Vector out = (outType == IMMUTABLE)
                ? Vector.immutable(x.length)
                : (this.type == IMMUTABLE) ? Vector.mutable(x.length) : this;
        for (int i = 0; i < x.length; ++i) {
            out.x[i] = Math.atan(x[i]);
        }
        return out;
    }

    public Vector tanh() {
        return tanh(DEFAULT_TYPE);
    }

    public Vector tanh(VectorType outType) {
        Vector out = (outType == IMMUTABLE)
                ? Vector.immutable(x.length)
                : (this.type == IMMUTABLE) ? Vector.mutable(x.length) : this;
        for (int i = 0; i < x.length; ++i) {
            out.x[i] = Math.tanh(x[i]);
        }
        return out;
    }

    public double dotProduct(Vector vector) {
        double out = 0.0;
        for (int i = 0; i < x.length; ++i) {
            out += x[i] * vector.x[i];
        }
        return out;
    }

    public double max() {
        double out = x[0];
        for (int i = 1; i < x.length; ++i) {
            out = (x[i] > out) ? x[i] : out;
        }
        return out;
    }

    public double min() {
        double out = x[0];
        for (int i = 1; i < x.length; ++i) {
            out = (x[i] < out) ? x[i] : out;
        }
        return out;
    }

    public VectorType getType() {
        return type;
    }

    public double get(int index) {
        return x[index];
    }

    public void set(int index, double val) {
        if (type == IMMUTABLE) {
            throw new IllegalAccessError("The vector is immutable and cannot be modified.");
        }
        x[index] = val;
    }

    public int length() {
        return x.length;
    }
}
