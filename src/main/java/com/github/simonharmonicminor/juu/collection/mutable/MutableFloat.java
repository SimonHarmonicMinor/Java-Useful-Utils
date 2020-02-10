package com.github.simonharmonicminor.juu.collection.mutable;

/**
 * Container which allows to hold float value and change it if necessary.
 *
 * @see MutableValue
 */
public class MutableFloat {
    private float value;

    public MutableFloat(float value) {
        this.value = value;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
