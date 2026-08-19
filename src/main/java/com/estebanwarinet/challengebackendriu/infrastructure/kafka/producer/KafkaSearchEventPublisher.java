package com.estebanwarinet.challengebackendriu.infrastructure.kafka.producer;

import com.estebanwarinet.challengebackendriu.application.event.SearchEvent;
import com.estebanwarinet.challengebackendriu.application.port.out.SearchEventPublisher;
import com.estebanwarinet.challengebackendriu.infrastructure.kafka.dto.SearchEventDto;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaSearchEventPublisher implements SearchEventPublisher {

    private static final String TOPIC = "hotel_availability_searches";

    private final KafkaTemplate<String, SearchEventDto> kafkaSearchEventTemplatePublisher;

    public KafkaSearchEventPublisher(
            KafkaTemplate<String, SearchEventDto> kafkaSearchEventTemplatePublisher) {
        this.kafkaSearchEventTemplatePublisher = kafkaSearchEventTemplatePublisher;
    }

    @Override
    public void publishSearchEvent(SearchEvent searchEvent) {
        kafkaSearchEventTemplatePublisher.send(
                TOPIC, searchEvent.searchId().searchId(), SearchEventDto.from(searchEvent));
    }
}
