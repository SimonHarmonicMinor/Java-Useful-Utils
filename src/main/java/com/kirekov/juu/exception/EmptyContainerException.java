package com.kirekov.juu.exception;

import com.kirekov.juu.monad.Try;

/**
 * Suppose to be thrown in case of accessing empty container.
 *
 * @see Try
 * @since 1.1
 */
public class EmptyContainerException extends RuntimeException {
  private static final long serialVersionUID = -12412528394L;

  public EmptyContainerException(String message, Throwable cause) {
    super(message, cause);
  }

  public EmptyContainerException(String message) {
    super(message);
  }
}
