package com.estebanwarinet.challengebackendriu.application.port.in;

import com.estebanwarinet.challengebackendriu.domain.model.Search;
import com.estebanwarinet.challengebackendriu.domain.model.SearchId;

public interface CreateSearchUseCase {
    SearchId createSearch(Search search);
}
