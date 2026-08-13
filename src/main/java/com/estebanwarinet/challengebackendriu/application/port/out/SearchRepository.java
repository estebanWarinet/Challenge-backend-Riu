package com.estebanwarinet.challengebackendriu.application.port.out;

import com.estebanwarinet.challengebackendriu.domain.model.Search;
import com.estebanwarinet.challengebackendriu.domain.model.SearchId;

import java.util.Optional;

public interface SearchRepository {
    void saveSearch(SearchId searchId, Search search);
    Optional<Search> findSearch(SearchId searchId);
    Long countSearch(Search search);
}
