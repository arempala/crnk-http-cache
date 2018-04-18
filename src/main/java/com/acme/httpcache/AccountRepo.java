package com.acme.httpcache;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.repository.ResourceRepositoryBase;
import io.crnk.core.resource.list.DefaultResourceList;
import io.crnk.core.resource.list.ResourceList;

@Component
public class AccountRepo extends ResourceRepositoryBase<Account, String> {
  private static final Logger LOGGER = LoggerFactory.getLogger(AccountRepo.class);

  protected AccountRepo() {
    super(Account.class);
  }

  @Override
  public Account findOne(String id, QuerySpec querySpec) {
    return new Account(id, "checking");
  }


  @Override
  public ResourceList<Account> findAll(QuerySpec querySpec) {
    String ifNoneMatch = CommonUtils.getETagRequestHeaderValue();
    String eTagValue = CommonUtils.createWeakETagValue("someEtagValue");

    LOGGER.info("If-None-Match vs etag -> {} vs {}", ifNoneMatch, eTagValue);
    MetaInfo metaInfo = new MetaInfo(eTagValue, eTagValue.equals(ifNoneMatch));

    if (metaInfo.isHttpCacheValid()) {
      LOGGER.info("HttpCache still valid. Will not query mongo");
      return new DefaultResourceList<>(Collections.EMPTY_LIST, metaInfo, null);
    }


    List<Account> accounts = Stream.of(new Account("1", "checking"), new Account("2", "savings"))
        .collect(Collectors.toList());
    return new DefaultResourceList<>(accounts, metaInfo, null);
  }

}
