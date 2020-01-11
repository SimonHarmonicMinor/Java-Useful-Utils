package com.github.simonharmonicminor.juu.collection.immutable;

import java.io.Serializable;
import java.util.Objects;

/**
 * Simple implementation of {@link Pair}
 *
 * @param <K> the type of the key
 * @param <V> the type of the value
 * @see Serializable
 * @since 1.0
 */
class PairImpl<K, V> implements Pair<K, V>, Serializable {
    private final K key;
    private final V value;

    PairImpl(K key, V value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public K getKey() {
        return key;
    }

    @Override
    public V getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PairImpl<?, ?> entry = (PairImpl<?, ?>) o;
        return Objects.equals(key, entry.key) &&
                Objects.equals(value, entry.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }
}
