package math.ode.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class Memoizer<T, U> {

    private final Map<T, U> cache = new ConcurrentHashMap<>();

    public Memoizer() {}

    public Function<T, U> doMemoize(final Function<T, U> function) {
        return input -> cache.computeIfAbsent(input, function);
    }

    public void clear() {
        cache.clear();
    }

    public static <T, U> Function<T, U> memoize(final Function<T, U> function) {
        return new Memoizer<T, U>().doMemoize(function);
    }
}