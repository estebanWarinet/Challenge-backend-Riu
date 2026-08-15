package com.estebanwarinet.challengebackendriu.infrastructure.kafka.consumer;

import com.estebanwarinet.challengebackendriu.application.event.SearchEvent;
import com.estebanwarinet.challengebackendriu.application.port.in.PersistSearchUseCase;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class KafkaSearchConsumerTest {

    @Mock
    private PersistSearchUseCase persistSearchUseCase;

    @InjectMocks
    private KafkaSearchConsumer consumer;

    @Test
    void shouldParseJsonAndPersistEvent() throws Exception {
        String json = """
                {"searchId":{"searchId":"uuid-1"},
                 "search":{"hotelId":"hotel-123","checkIn":"2026-08-20","checkOut":"2026-08-25","ages":[30,5]}}
                """;

        consumer.handleSearchEvent(json);

        ArgumentCaptor<SearchEvent> captor = ArgumentCaptor.forClass(SearchEvent.class);
        verify(persistSearchUseCase).persist(captor.capture());
        SearchEvent captured = captor.getValue();
        assertAll(
                () -> assertEquals("uuid-1", captured.searchId().searchId()),
                () -> assertEquals("hotel-123", captured.search().hotelId()),
                () -> assertEquals(LocalDate.of(2026, 8, 20), captured.search().checkIn()),
                () -> assertEquals(List.of(30, 5), captured.search().ages())
        );
    }

    @Test
    void shouldNotPersistWhenJsonIsInvalid() {
        assertThrows(JsonProcessingException.class, () -> consumer.handleSearchEvent("{ no válido"));

        verify(persistSearchUseCase, never()).persist(any());
    }
}
