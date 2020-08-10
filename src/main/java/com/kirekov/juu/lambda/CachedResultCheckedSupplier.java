package com.kirekov.juu.lambda;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation for {@linkplain CheckedSupplier} which calculates value only
 * for the first time and then returns cached result. This implementation is not thread-safe.
 *
 * @param <T> the type of the return value
 * @param <E> the type of the exception
 */
public class CachedResultCheckedSupplier<T, E extends Throwable> implements CheckedSupplier<T, E> {
    private final List<T> result = new ArrayList<>(1);

    private final CheckedSupplier<T, E> checkedSupplier;

    public CachedResultCheckedSupplier(CheckedSupplier<T, E> checkedSupplier) {
        this.checkedSupplier = checkedSupplier;
    }

    @Override
    public T get() throws E {
        if (result.isEmpty()) {
            result.add(checkedSupplier.get());
        }
        return result.get(0);
    }
}
