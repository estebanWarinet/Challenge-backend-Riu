package com.estebanwarinet.challengebackendriu.application.service;

import com.estebanwarinet.challengebackendriu.application.dto.SearchCountResult;
import com.estebanwarinet.challengebackendriu.application.port.in.GetSearchCountUseCase;
import com.estebanwarinet.challengebackendriu.domain.repository.SearchRepository;
import com.estebanwarinet.challengebackendriu.domain.exception.SearchNotFoundException;
import com.estebanwarinet.challengebackendriu.domain.model.Search;
import com.estebanwarinet.challengebackendriu.domain.model.SearchId;

public class GetSearchCountService implements GetSearchCountUseCase {

    private final SearchRepository searchRepository;

    public GetSearchCountService(SearchRepository searchRepository) {
        this.searchRepository = searchRepository;
    }

    @Override
    public SearchCountResult countSearch(SearchId searchId) {
        Search search = searchRepository.findSearch(
                searchId).orElseThrow(() -> new SearchNotFoundException(searchId));

        Long count = searchRepository.countSearch(search);

        return new SearchCountResult(searchId, search, count);

    }
}
