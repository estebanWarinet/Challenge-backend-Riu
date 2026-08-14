package com.estebanwarinet.challengebackendriu.infrastructure.db;

import com.estebanwarinet.challengebackendriu.domain.model.Search;
import com.estebanwarinet.challengebackendriu.domain.model.SearchId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(SearchJpaRepositoryAdapter.class)
public class SearchJpaRepositoryAdapterTest {

    @Autowired
    private SearchJpaRepositoryAdapter adapter;

    @Test
    void shouldSaveAndFindSearch() {
        Search search = new Search("hotel-123",
                LocalDate.of(2026, 8, 20), LocalDate.of(2026, 8, 25), List.of(30, 5));

        adapter.saveSearch(new SearchId("uuid-1"), search);          // ← él arma entity+signature

        Optional<Search> found = adapter.findSearch(new SearchId("uuid-1"));   // ← él reconstruye

        assertTrue(found.isPresent());
        found.ifPresent(s -> assertAll(
                () -> assertEquals("hotel-123", s.hotelId()),
                () -> assertEquals(LocalDate.of(2026, 8, 20), s.checkIn()),
                () -> assertEquals(LocalDate.of(2026, 8, 25), s.checkOut()),
                () -> assertEquals(List.of(30, 5), s.ages())
        ));
    }

    @Test
    void shouldCountIdenticalSearches() {
        Search base = new Search("hotel-123",
                LocalDate.of(2026, 8, 20), LocalDate.of(2026, 8, 25), List.of(30, 5));
        Search reversed = new Search("hotel-123",
                LocalDate.of(2026, 8, 20), LocalDate.of(2026, 8, 25), List.of(5, 30));

        adapter.saveSearch(new SearchId("uuid-1"), base);
        adapter.saveSearch(new SearchId("uuid-2"), base);
        adapter.saveSearch(new SearchId("uuid-3"), reversed);

        assertAll(
                () -> assertEquals(2L, adapter.countSearch(base)),       // idénticas (mismo orden)
                () -> assertEquals(1L, adapter.countSearch(reversed))    // orden distinto ≠ idéntica
        );
    }

}
