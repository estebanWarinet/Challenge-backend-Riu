package com.estebanwarinet.challengebackendriu.application.service;

import com.estebanwarinet.challengebackendriu.application.event.SearchEvent;
import com.estebanwarinet.challengebackendriu.application.port.in.CreateSearchUseCase;
import com.estebanwarinet.challengebackendriu.application.port.out.SearchEventPublisher;
import com.estebanwarinet.challengebackendriu.domain.exception.PastSearchDateException;
import com.estebanwarinet.challengebackendriu.domain.model.Search;
import com.estebanwarinet.challengebackendriu.domain.model.SearchId;

import java.time.Clock;
import java.time.LocalDate;

public class CreateSearchService implements CreateSearchUseCase {

    private final SearchEventPublisher searchEventPublisher;
    private final Clock clock;

    public CreateSearchService(SearchEventPublisher searchEventPublisher, Clock clock) {
        this.searchEventPublisher = searchEventPublisher;
        this.clock = clock;
    }

    @Override
    public SearchId createSearch(Search search) {
        if (search.checkIn().isBefore(LocalDate.now(clock))) {
            throw new PastSearchDateException("La fecha checkIn debe ser una fecha actual o futura");
        }

        SearchId id = SearchId.random();
        searchEventPublisher.publishSearchEvent(new SearchEvent(id, search));
        return id;
    }
}
