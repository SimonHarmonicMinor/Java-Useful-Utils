package com.kirekov.juu.lambda;

/**
 * Represents a procedure, that do some stuff and returns nothing
 *
 * @since 1.0
 */
@FunctionalInterface
public interface Action {
    void execute();
}
