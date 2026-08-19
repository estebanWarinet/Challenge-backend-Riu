package com.estebanwarinet.challengebackendriu.application.service;

import com.estebanwarinet.challengebackendriu.application.event.SearchEvent;
import com.estebanwarinet.challengebackendriu.domain.repository.SearchRepository;
import com.estebanwarinet.challengebackendriu.domain.model.Search;
import com.estebanwarinet.challengebackendriu.domain.model.SearchId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PersistSearchServiceTest {

    @Mock
    private SearchRepository searchRepository;

    @InjectMocks
    private PersistSearchService persistSearchService;

    @Test
    void shouldPersistSearchEvent() {
        SearchId searchId = new SearchId("uuid-1");
        Search search = new Search(
                "hotel-123",
                LocalDate.of(2026, 8, 20),
                LocalDate.of(2026, 8, 25),
                List.of(30, 5)
        );
        SearchEvent searchEvent = new SearchEvent(searchId, search);

        persistSearchService.persist(searchEvent);

        verify(searchRepository).saveSearch(searchEvent.searchId(), searchEvent.search());
    }
}
