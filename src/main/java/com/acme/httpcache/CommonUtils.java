package com.acme.httpcache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class CommonUtils {
  private static final Logger LOGGER = LoggerFactory.getLogger(CommonUtils.class);

  public static String getETagRequestHeaderValue() {
    RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
    if (requestAttributes instanceof ServletRequestAttributes) {
      String ifNoneMatchHeaderValue = ((ServletRequestAttributes) requestAttributes).getRequest()
          .getHeader(HttpHeaders.IF_NONE_MATCH);
      return ifNoneMatchHeaderValue;
    }
    LOGGER.error(
        "Failed to retrieve IF_NONE_MATCH Request Header. Not called in the context of an HTTP request");
    return null;
  }

  public static String createWeakETagValue(String etag) {
    return "W/\"" + etag + "\"";
  }

}
