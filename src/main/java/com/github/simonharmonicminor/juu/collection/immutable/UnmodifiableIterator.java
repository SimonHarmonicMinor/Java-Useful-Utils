package com.github.simonharmonicminor.juu.collection.immutable;

import java.util.Iterator;
import java.util.Objects;

/**
 * An implementation of {@link Iterator} that forbids
 * removing elements. Behaves as a wrapper that accepts
 * actual iterator and allows to call concrete methods.
 *
 * @param <T> the type of the element
 */
public class UnmodifiableIterator<T> implements Iterator<T> {
    private final Iterator<T> iterator;

    /**
     * @param iterator the actual iterator that is going to be traversed
     * @throws NullPointerException if {@code iterator} is null
     */
    public UnmodifiableIterator(Iterator<T> iterator) {
        Objects.requireNonNull(iterator);
        this.iterator = iterator;
    }


    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public T next() {
        return iterator.next();
    }

    /**
     * Always throws {@link UnsupportedOperationException}.
     * Declared as final in order not be overridden.
     *
     * @throws UnsupportedOperationException always
     */
    @Override
    public final void remove() {
        throw new UnsupportedOperationException("Element's removing is not allowed");
    }
}
