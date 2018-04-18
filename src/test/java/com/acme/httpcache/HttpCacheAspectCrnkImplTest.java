package com.acme.httpcache;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.databind.node.BooleanNode;
import com.fasterxml.jackson.databind.node.LongNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

import io.crnk.core.engine.dispatcher.Response;
import io.crnk.core.engine.document.Document;
import io.crnk.core.engine.http.HttpRequestContext;
import io.crnk.core.engine.internal.http.JsonApiRequestProcessor;

public class HttpCacheAspectCrnkImplTest {

  private HttpCacheAspectCrnkImpl target;
  private JsonApiRequestProcessor adviceTarget;
  private ProceedingJoinPoint joinPoint;
  private HttpRequestContext requestContext;

  @Before
  public void setup() {
    joinPoint = mock(ProceedingJoinPoint.class);
    requestContext = mock(HttpRequestContext.class);
    target = new HttpCacheAspectCrnkImpl();
  }

  @Test
  public void testaAplyHttpCaching_WHEN_no_meta_THEN_do_nothing() throws Throwable {
    Document document = new Document();
    givenResponse(document);

    target.applyHttpCaching(joinPoint);

    verify(joinPoint).proceed();
    verify(requestContext, never()).setResponseHeader(anyString(), anyString());
    verify(requestContext, never()).setResponse(anyInt(), anyString());
  }

  @Test
  public void testaAplyHttpCaching_WHEN_meta_present_BUT_no_etag_THEN_do_nothing()
      throws Throwable {
    Document document = new Document();
    String etag = null;
    Boolean httpCacheValid = null;
    document.setMeta(getMeta(1234L, etag, httpCacheValid));
    givenResponse(document);

    target.applyHttpCaching(joinPoint);

    verify(joinPoint).proceed();
    verify(requestContext, never()).setResponseHeader(anyString(), anyString());
    verify(requestContext, never()).setResponse(anyInt(), anyString());
  }

  @Test
  public void testaAplyHttpCaching_WHEN_etag_present_AND_cache_out_of_date_THEN_add_etag_resp_header()
      throws Throwable {
    Document document = new Document();
    String etag = CommonUtils.createWeakETagValue("etag-1234");
    Boolean httpCacheValid = false;
    document.setMeta(getMeta(1234L, etag, httpCacheValid));
    givenResponse(document);

    target.applyHttpCaching(joinPoint);

    verify(joinPoint).proceed();
    verify(requestContext).setResponseHeader(eq(HttpHeaders.ETAG), eq(etag));
    verify(requestContext, never()).setResponse(anyInt(), anyString());
  }

  @Test
  public void testaAplyHttpCaching_WHEN_etag_present_AND_cache_up_to_date_THEN_respond_with_304_AND_skip_the_joinpoint()
      throws Throwable {
    Document document = new Document();
    String etag = CommonUtils.createWeakETagValue("etag-1234");
    Boolean httpCacheValid = true;
    document.setMeta(getMeta(1234L, etag, httpCacheValid));
    givenResponse(document);

    target.applyHttpCaching(joinPoint);

    verify(joinPoint, never()).proceed();
    verify(requestContext).setResponseHeader(eq(HttpHeaders.ETAG), eq(etag));
    verify(requestContext).setResponse(eq(HttpStatus.NOT_MODIFIED.value()), eq(""));
  }


  private void givenResponse(Document document) {
    Response crnkResponse = new Response(document, 200);
    Object[] argsArray = new Object[] {requestContext, crnkResponse};
    when(joinPoint.getArgs()).thenReturn(argsArray);

  }

  private ObjectNode getMeta(Long totalResourceCount, String etag, Boolean httpCacheValid) {
    ObjectNode meta = mock(ObjectNode.class);
    if (totalResourceCount != null) {
      when(meta.get("totalResourceCount")).thenReturn(new LongNode(totalResourceCount));
    }

    if (etag != null) {
      when(meta.get(Constants.ETAG)).thenReturn(new TextNode(etag));
      when(meta.hasNonNull(eq(Constants.ETAG))).thenReturn(true);
    } else {
      when(meta.hasNonNull(eq(Constants.ETAG))).thenReturn(false);
    }

    if (httpCacheValid != null) {
      when(meta.get(Constants.HTTP_CACHE_VALID)).thenReturn(BooleanNode.valueOf(httpCacheValid));
      when(meta.hasNonNull(eq(Constants.HTTP_CACHE_VALID))).thenReturn(true);
    } else {
      when(meta.hasNonNull(eq(Constants.HTTP_CACHE_VALID))).thenReturn(false);
    }

    return meta;
  }
}
