package com.github.simonharmonicminor.juu.lambda;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Implementation for {@linkplain Supplier} which calculates value only once
 * for the first time and then returns cached result. This implementation is not thread safe.
 * @param <T> the type of the return value
 */
public class CachedResultSupplier<T> implements Supplier<T> {
    private final List<T> result = new ArrayList<>(1);

    private final Supplier<T> supplier;

    public CachedResultSupplier(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    @Override
    public T get() {
        if (result.isEmpty()) {
            result.add(supplier.get());
        }
        return result.get(0);
    }
}
