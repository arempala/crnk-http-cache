package com.acme.httpcache;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.node.ObjectNode;

import io.crnk.core.engine.dispatcher.Response;
import io.crnk.core.engine.http.HttpRequestContext;

@Aspect
@Component
public class HttpCacheAspectCrnkImpl {
  private static final Logger LOGGER = LoggerFactory.getLogger(HttpCacheAspectCrnkImpl.class);

  @Pointcut("execution(private void io.crnk.core.engine.internal.http.JsonApiRequestProcessor.setResponse(..))")
  public void setResponse() {}

  @Around(value = "setResponse()")
  public void applyHttpCaching(ProceedingJoinPoint joinPoint) throws Throwable {
    Response crnkResponse = (Response) joinPoint.getArgs()[1];
    ObjectNode meta = crnkResponse.getDocument().getMeta();
    if (meta == null || !meta.hasNonNull(Constants.ETAG)) {
      LOGGER.info("ETag NOT Found. No further action.");
      joinPoint.proceed();
      return;
    }

    HttpRequestContext requestContext = (HttpRequestContext) joinPoint.getArgs()[0];
    if (meta.hasNonNull(Constants.ETAG)) {
      String etag = meta.get(Constants.ETAG).asText();
      LOGGER.info("ETag Found. Adding {} to the response ETag Header.", etag);
      requestContext.setResponseHeader(HttpHeaders.ETAG, etag);
      meta.remove(Constants.ETAG);
    }

    if (!isHttpCacheValid(meta)) {
      meta.remove(Constants.HTTP_CACHE_VALID);
      joinPoint.proceed();
      return;
    } else {
      LOGGER.info("httpCacheValid is TRUE. Responding with HTTP 304");
      requestContext.setResponse(HttpStatus.NOT_MODIFIED.value(), "");
    }
  }

  private boolean isHttpCacheValid(ObjectNode meta) {
    if (meta == null || !meta.hasNonNull("httpCacheValid")) {
      return false;
    }

    return meta.get("httpCacheValid").asBoolean();
  }

}
