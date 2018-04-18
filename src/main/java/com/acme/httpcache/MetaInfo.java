package com.acme.httpcache;

import io.crnk.core.resource.meta.MetaInformation;

public class MetaInfo implements MetaInformation {

  private String etag;
  private boolean httpCacheValid;

  public MetaInfo() {
    super();
  }

  public MetaInfo(String etag, boolean httpCacheValid) {
    super();
    this.etag = etag;
    this.httpCacheValid = httpCacheValid;
  }

  public String getEtag() {
    return etag;
  }

  public void setEtag(String etag) {
    this.etag = etag;
  }

  public boolean isHttpCacheValid() {
    return httpCacheValid;
  }

  public void setHttpCacheValid(boolean httpCacheValid) {
    this.httpCacheValid = httpCacheValid;
  }

}
