package com.estebanwarinet.challengebackendriu.application.event;

import com.estebanwarinet.challengebackendriu.domain.model.Search;
import com.estebanwarinet.challengebackendriu.domain.model.SearchId;

public record SearchEvent(SearchId searchId, Search search) {
}
