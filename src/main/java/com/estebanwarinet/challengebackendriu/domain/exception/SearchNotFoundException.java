package com.estebanwarinet.challengebackendriu.domain.exception;

import com.estebanwarinet.challengebackendriu.domain.model.SearchId;

public class SearchNotFoundException extends RuntimeException {
    public SearchNotFoundException(SearchId searchId) {
        super("Búsqueda no encontrada: " + searchId.searchId());
    }
}
