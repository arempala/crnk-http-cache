package com.acme.httpcache;

public class Constants {

  public static final String JSONAPI_CONTENT_TYPE = "application/vnd.api+json";

  public static final String DEFAULT_CHARSET = "utf-8";

  public static final String JSONAPI_CONTENT_TYPE_AND_CHARSET =
      JSONAPI_CONTENT_TYPE + "; charset=" + DEFAULT_CHARSET;

  public static final String ETAG = "etag";
  public static final String HTTP_CACHE_VALID = "httpCacheValid";

}
