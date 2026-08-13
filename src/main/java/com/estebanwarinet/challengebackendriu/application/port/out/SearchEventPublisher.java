package com.estebanwarinet.challengebackendriu.application.port.out;

import com.estebanwarinet.challengebackendriu.application.event.SearchEvent;

public interface SearchEventPublisher {
    void publishSearchEvent(SearchEvent searchEvent);
}
