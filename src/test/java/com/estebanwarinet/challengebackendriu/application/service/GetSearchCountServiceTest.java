package com.estebanwarinet.challengebackendriu.application.service;

import com.estebanwarinet.challengebackendriu.application.dto.SearchCountResult;
import com.estebanwarinet.challengebackendriu.domain.repository.SearchRepository;
import com.estebanwarinet.challengebackendriu.domain.exception.SearchNotFoundException;
import com.estebanwarinet.challengebackendriu.domain.model.Search;
import com.estebanwarinet.challengebackendriu.domain.model.SearchId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetSearchCountServiceTest {

    @Mock
    private SearchRepository searchRepository;

    @InjectMocks
    GetSearchCountService getSearchCountService;

    @Test
    void shouldGetSearchCount() {
        SearchId searchId = new SearchId("uuid-1");
        Search search = new Search(
                "hotel-123",
                LocalDate.of(2026, 8, 20),
                LocalDate.of(2026, 8, 25),
                List.of(30, 5)
        );

        when(searchRepository.findSearch(searchId)).thenReturn(Optional.of(search));
        when(searchRepository.countSearch(search)).thenReturn(3L);

        SearchCountResult result = getSearchCountService.countSearch(searchId);

        assertNotNull(result);
        assertEquals(new SearchCountResult(searchId, search, 3L), result);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenSearchNotFound() {
        SearchId searchId = new SearchId("uuid-1");

        when(searchRepository.findSearch(searchId)).thenReturn(Optional.empty());

        assertThrows(SearchNotFoundException.class, () -> getSearchCountService.countSearch(searchId));
    }
}
