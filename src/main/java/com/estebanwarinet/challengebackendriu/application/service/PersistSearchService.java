package com.estebanwarinet.challengebackendriu.application.service;

import com.estebanwarinet.challengebackendriu.application.event.SearchEvent;
import com.estebanwarinet.challengebackendriu.application.port.in.PersistSearchUseCase;
import com.estebanwarinet.challengebackendriu.application.port.out.SearchRepository;

public class PersistSearchService implements PersistSearchUseCase {

    private final SearchRepository searchRepository;

    public PersistSearchService(SearchRepository searchRepository) {
        this.searchRepository = searchRepository;
    }

    @Override
    public void persist(SearchEvent event) {
        searchRepository.saveSearch(event.searchId(), event.search());
    }
}
