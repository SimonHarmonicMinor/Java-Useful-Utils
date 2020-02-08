package com.github.simonharmonicminor.juu.collection.mutable;

/**
 * Container which allows to hold long value and change it if necessary.
 *
 * @see MutableValue
 */
public class MutableLong {
    private long value;

    public MutableLong(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }
}
