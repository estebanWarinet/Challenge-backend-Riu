package com.estebanwarinet.challengebackendriu.application.port.in;

import com.estebanwarinet.challengebackendriu.application.dto.SearchCountResult;
import com.estebanwarinet.challengebackendriu.domain.model.SearchId;

public interface GetSearchCountUseCase {
    SearchCountResult countSearch(SearchId searchId);
}
