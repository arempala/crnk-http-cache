package com.acme.httpcache;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.repository.ResourceRepositoryBase;
import io.crnk.core.resource.list.DefaultResourceList;
import io.crnk.core.resource.list.ResourceList;

@Component
public class AccountRepo extends ResourceRepositoryBase<Account, String> {

	protected AccountRepo() {
		super(Account.class);
	}
	
	@Override
	  public Account findOne(String id, QuerySpec querySpec) {
	    return new Account(id, "checking");
	  }


	@Override
	public ResourceList<Account> findAll(QuerySpec querySpec) {
		List<Account> accounts = Stream.of(new Account("1", "checking"), new Account("2", "savings"))
				.collect(Collectors.toList());
		return new DefaultResourceList<>(accounts, null, null);
	}

}
