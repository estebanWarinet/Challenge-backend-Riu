package com.estebanwarinet.challengebackendriu.infrastructure.kafka.producer;

import com.estebanwarinet.challengebackendriu.application.event.SearchEvent;
import com.estebanwarinet.challengebackendriu.domain.model.Search;
import com.estebanwarinet.challengebackendriu.domain.model.SearchId;
import com.estebanwarinet.challengebackendriu.infrastructure.kafka.dto.SearchEventDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class KafkaSearchEventPublisherTest {

    @Mock
    private KafkaTemplate<String, SearchEventDto> kafkaTemplate;

    @InjectMocks
    private KafkaSearchEventPublisher publisher;

    @Test
    void shouldPublishEventToCorrectTopicWithSearchIdAsKey() {
        SearchEvent event = new SearchEvent(
                new SearchId("uuid-1"),
                new Search(
                        "hotel-123",
                        LocalDate.of(2026, 8, 20),
                        LocalDate.of(2026, 8, 25),
                        List.of(30, 5)
                )
        );

        publisher.publishSearchEvent(event);

        verify(kafkaTemplate).send(
                eq("hotel_availability_searches"), eq("uuid-1"), eq(SearchEventDto.from(event)));
    }
}
