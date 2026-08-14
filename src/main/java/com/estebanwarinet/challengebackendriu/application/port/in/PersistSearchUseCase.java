package com.estebanwarinet.challengebackendriu.application.port.in;

import com.estebanwarinet.challengebackendriu.application.event.SearchEvent;

public interface PersistSearchUseCase {
    void persist(SearchEvent event);
}
