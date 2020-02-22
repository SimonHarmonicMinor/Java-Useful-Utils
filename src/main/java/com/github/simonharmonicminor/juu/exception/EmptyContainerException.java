package com.github.simonharmonicminor.juu.exception;

import com.github.simonharmonicminor.juu.monad.Try;

/**
 * Suppose to be thrown in case of accessing empty container.
 *
 * @see Try
 * @since 1.1
 */
public class EmptyContainerException extends RuntimeException {
    public EmptyContainerException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmptyContainerException(String message) {
        super(message);
    }
}
