package com.kirekov.juu.collection.mutable;

/**
 * Container which allows to hold char value and change it if necessary.
 *
 * @see MutableValue
 */
public class MutableChar {
    private char value;

    public MutableChar(char value) {
        this.value = value;
    }

    public char getValue() {
        return value;
    }

    public void setValue(char value) {
        this.value = value;
    }
}
