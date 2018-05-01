package com.walmart.labs.exception;

import com.walmart.labs.handler.ApplicationGlobalExceptionHandler;
import java.text.MessageFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An ExceptionFactory class. This allows you to automatically log the exception in your application
 * logs. At any place in your application, you can now throw an exception, and this will log the
 * exception in the application logs. The exception will be thrown and picked up by the
 * DefaultExceptionHandler thanks to the Spring @ControllerAdvice annotation in {@link
 * ApplicationGlobalExceptionHandler} class.
 *
 * <p>How to use this class: throw ExceptionFactory.create(ExceptionType.INTERNAL_SERVER_ERROR);
 */
public class ExceptionFactory {

  private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionFactory.class);

  public static ApplicationException create(
      final Throwable cause,
      final ExceptionType exceptionType,
      final Object... detailedMessageArguments) {
    LOGGER.error(
        MessageFormat.format(exceptionType.getBriefMessage(), detailedMessageArguments), cause);
    return new ApplicationException(exceptionType, cause, detailedMessageArguments);
  }

  public static ApplicationException create(
      final ExceptionType exceptionType, final Object... detailedMessageArguments) {
    LOGGER.error(MessageFormat.format(exceptionType.getBriefMessage(), detailedMessageArguments));
    // TODO: Store exception information in DB for tracking errors.
    return new ApplicationException(exceptionType, detailedMessageArguments);
  }
}
