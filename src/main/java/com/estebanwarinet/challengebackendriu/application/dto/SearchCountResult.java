package com.estebanwarinet.challengebackendriu.application.dto;

import com.estebanwarinet.challengebackendriu.domain.model.Search;
import com.estebanwarinet.challengebackendriu.domain.model.SearchId;

public record SearchCountResult(SearchId searchId, Search search, Long count) {
}
