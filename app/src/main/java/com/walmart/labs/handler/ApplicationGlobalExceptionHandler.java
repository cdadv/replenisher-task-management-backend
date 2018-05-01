package com.walmart.labs.handler;

import com.walmart.labs.exception.ApiError;
import com.walmart.labs.exception.ApplicationException;
import com.walmart.labs.exception.ExceptionType;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Handle common client errors by overriding base methods and providing custom implementation.
 * handle custom errors(exceptions) that does not have a default implementation in the base class.
 * http://www.baeldung.com/global-error-handler-in-a-spring-rest-api
 *
 * <p>(Few other ways to handle global exceptions:
 * https://spring.io/blog/2013/11/01/exception-handling-in-spring-mvc
 * https://stackoverflow.com/questions/28902374/spring-boot-rest-service-exception-handling)
 */
@ControllerAdvice
public class ApplicationGlobalExceptionHandler extends ResponseEntityExceptionHandler {

  /**
   * @implNote BindException: This exception is thrown when fatal binding errors occur.
   * @implNote MethodArgumentNotValidException: This exception is thrown when argument annotated
   *     with @Valid failed validation.
   */
  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    List<String> errors = new ArrayList<String>();
    for (FieldError error : ex.getBindingResult().getFieldErrors()) {
      errors.add(error.getField() + ": " + error.getDefaultMessage());
    }
    for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
      errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
    }

    ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);
    return handleExceptionInternal(ex, apiError, headers, apiError.getStatus(), request);
  }

  /**
   * @implNote MissingServletRequestPartException: This exception is thrown when when the part of a
   *     multipart request not found.
   * @implNote MissingServletRequestParameterException: This exception is thrown when request
   *     missing parameter.
   */
  @Override
  protected ResponseEntity<Object> handleMissingServletRequestParameter(
      MissingServletRequestParameterException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    String error = ex.getParameterName() + " parameter is missing";

    ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), error);
    return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
  }

  /**
   * Customize servlet to throw NoHandlerFoundException instead of send 404 response. Used
   * configurations in application.properties under resources directory
   * spring.mvc.throw-exception-if-no-handler-found=true spring.resources.add-mappings=false
   *
   * <p>(Without this customization, if there is no handler founded in request, spring's default
   * dispatcherServlet will handle the error and try to redirect to /error page. And if there is no
   * /error page, spring will redirect the page to a Whitelabel Error Page.)
   *
   * <p>Here we customized this part to prevent from showing the whitelabel page and directly
   * response as json.
   */
  @Override
  protected ResponseEntity<Object> handleNoHandlerFoundException(
      NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
    String error = "No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL();

    ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getLocalizedMessage(), error);
    return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
  }

  /**
   * @implNote HttpRequestMethodNotSupportedException: Occurs when you send a requested with an
   *     unsupported HTTP method:
   */
  @Override
  protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
      HttpRequestMethodNotSupportedException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    StringBuilder builder = new StringBuilder();
    builder.append(ex.getMethod());
    builder.append(" method is not supported for this request. Supported methods are ");
    ex.getSupportedHttpMethods().forEach(t -> builder.append(t + " "));

    ApiError apiError =
        new ApiError(HttpStatus.METHOD_NOT_ALLOWED, ex.getLocalizedMessage(), builder.toString());
    return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
  }

  /**
   * @implNote HttpMediaTypeNotSupportedException: Occurs when the client send a request with
   *     unsupported media type
   */
  @Override
  protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
      HttpMediaTypeNotSupportedException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    StringBuilder builder = new StringBuilder();
    builder.append(ex.getContentType());
    builder.append(" media type is not supported. Supported media types are ");
    ex.getSupportedMediaTypes().forEach(t -> builder.append(t + ", "));

    ApiError apiError =
        new ApiError(
            HttpStatus.UNSUPPORTED_MEDIA_TYPE,
            ex.getLocalizedMessage(),
            builder.substring(0, builder.length() - 2));
    return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
  }

  /**
   * Default Handler: A fall-back handler - a catch-all type of logic that deals with all other
   * exceptions that don’t have specific handlers.
   */
  @ExceptionHandler({Exception.class})
  public ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {
    ApiError apiError =
        new ApiError(
            HttpStatus.INTERNAL_SERVER_ERROR,
            ex.getLocalizedMessage(),
            ex.getClass().getSimpleName());
    return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
  }

  /**
   * IMPORTANT!!
   *
   * <p>Customized Exception: {@link ApplicationException} Expecting our application will only throw
   * this exception. You will be able to define your exception type in 'ExceptionType' object {@link
   * ExceptionType} as enum.
   *
   * <p>To customize an exception: You need to define the returned HTTP status for the exception,
   * brief message (the whole message = brief message + detailed message), error(exception) name.
   */
  @ExceptionHandler({ApplicationException.class})
  public ResponseEntity<Object> handleApplicationException(
      ApplicationException ex, WebRequest request) {
    HttpStatus status = ex.getExceptionType().getStatus();
    String error = ex.getExceptionType().name();
    String message = ex.getWholeMessage();
    ApiError apiError = new ApiError(status, message, error);
    return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
  }
}
