package com.estebanwarinet.challengebackendriu.application.service;

import com.estebanwarinet.challengebackendriu.application.event.SearchEvent;
import com.estebanwarinet.challengebackendriu.application.port.in.CreateSearchUseCase;
import com.estebanwarinet.challengebackendriu.application.port.out.SearchEventPublisher;
import com.estebanwarinet.challengebackendriu.domain.model.Search;
import com.estebanwarinet.challengebackendriu.domain.model.SearchId;

public class CreateSearchService implements CreateSearchUseCase {

    private final SearchEventPublisher searchEventPublisher;

    public CreateSearchService(SearchEventPublisher searchEventPublisher) {
        this.searchEventPublisher = searchEventPublisher;
    }

    @Override
    public SearchId createSearch(Search search){
        SearchId id = SearchId.random();
        searchEventPublisher.publishSearchEvent(new SearchEvent(id, search));
        return id;
    }
}
