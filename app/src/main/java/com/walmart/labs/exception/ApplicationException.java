package com.walmart.labs.exception;

import com.walmart.labs.handler.ApplicationGlobalExceptionHandler;
import java.text.MessageFormat;
import org.springframework.http.HttpStatus;

/**
 * Create an application specific exception (ApplicationException class) used for expected
 * exceptions and throw this exception on any level and it will get picked up by Spring.
 *
 * <p>With the help of {@link ApplicationGlobalExceptionHandler} (Customized handler), {@link
 * ApplicationException} (Customized global exception), {@link ExceptionType} and {@link
 * ExceptionFactory} (Customized exception types), No internal server errors will be propagated to
 * the end user, and both expected(customized) and unexpected(common) exceptions are handled by the
 * DefaultExceptionHandler. The exception is assigned a certain HTTP error code and error message,
 * which will be returned to the client.
 * https://stackoverflow.com/questions/45034371/where-to-handle-exceptions-in-spring-applications
 */
public class ApplicationException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  /** {@link ExceptionType} Define customized exception types using ExceptionType enum. */
  private final ExceptionType exceptionType;

  /**
   * {@link Object...} varargs of Objects for array or detailed messages.
   *
   * <p>Brief Message: {@link String} Defined in ExceptionType enum. This message is a preview
   * statement of what kind or type of error it can be.
   *
   * <p>Detailed Message(s): {@link Object...} array of Object that contains the detailed
   * information for exceptions. Check FormatStyle for Object using following link.
   *
   * <p>Whole Message: Combined brief message and detailed messages with {@link MessageFormat}
   * (https://docs.oracle.com/javase/7/docs/api/java/text/MessageFormat.html)
   *
   * <p>An example to understand {@link MessageFormat}:
   *
   * <p>String result = MessageFormat.format( "At {1,time} on {1,date}, there was {2} on planet
   * {0,number,integer}.", planet, new Date(), event);
   */
  private final Object[] detailedMessageArguments;

  public ApplicationException(ExceptionType exceptionType, Object... detailedMessageArguments) {
    super(MessageFormat.format(exceptionType.getBriefMessage(), detailedMessageArguments));
    this.exceptionType = exceptionType;
    this.detailedMessageArguments = detailedMessageArguments;
  }

  public ApplicationException(
      ExceptionType exceptionType, final Throwable cause, Object... detailedMessageArguments) {
    super(MessageFormat.format(exceptionType.getBriefMessage(), detailedMessageArguments), cause);
    this.exceptionType = exceptionType;
    this.detailedMessageArguments = detailedMessageArguments;
  }

  public ExceptionType getExceptionType() {
    return exceptionType;
  }

  public HttpStatus getHttpStatus() {
    return exceptionType.getStatus();
  }

  public String getWholeMessage() {
    return MessageFormat.format(exceptionType.getBriefMessage(), detailedMessageArguments);
  }
}
