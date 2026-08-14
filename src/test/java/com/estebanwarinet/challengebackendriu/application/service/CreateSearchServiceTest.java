package com.estebanwarinet.challengebackendriu.application.service;

import com.estebanwarinet.challengebackendriu.application.event.SearchEvent;
import com.estebanwarinet.challengebackendriu.application.port.out.SearchEventPublisher;
import com.estebanwarinet.challengebackendriu.domain.model.Search;
import com.estebanwarinet.challengebackendriu.domain.model.SearchId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CreateSearchServiceTest {

    @Mock
    private SearchEventPublisher searchEventPublisher;

    @InjectMocks
    private CreateSearchService createSearchService;

    @Test
    void shouldCreateSearchAndPublishEvent() {
        Search search = new Search(
                "hotel-123",
                LocalDate.of(2026, 8, 20),
                LocalDate.of(2026, 8, 25),
                List.of(30, 5)
        );

        SearchId result = createSearchService.createSearch(search);

        assertNotNull(result);
        verify(searchEventPublisher).publishSearchEvent(new SearchEvent(result, search));
    }

    @Test
    void shouldPublishEventOnlyOnce() {
        Search search = new Search(
                "hotel-123",
                LocalDate.of(2026, 8, 20),
                LocalDate.of(2026, 8, 25),
                List.of(30)
        );

        createSearchService.createSearch(search);

        verify(searchEventPublisher, times(1)).publishSearchEvent(any(SearchEvent.class));
    }
}
