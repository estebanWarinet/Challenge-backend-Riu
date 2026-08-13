package com.estebanwarinet.challengebackendriu.infrastructure.kafka.producer;

import com.estebanwarinet.challengebackendriu.application.event.SearchEvent;
import com.estebanwarinet.challengebackendriu.application.port.out.SearchEventPublisher;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaSearchEventPublisher implements SearchEventPublisher {

    private static final String TOPIC = "hotel_availability_searches";

    private final KafkaTemplate<String, SearchEvent> kafkaSearchEventTemplatePublisher;

    public KafkaSearchEventPublisher(KafkaTemplate<String, SearchEvent> kafkaSearchEventTemplatePublisher) {
        this.kafkaSearchEventTemplatePublisher = kafkaSearchEventTemplatePublisher;
    }

    @Override
    public void publishSearchEvent(SearchEvent searchEvent) {
        kafkaSearchEventTemplatePublisher.send(TOPIC, searchEvent.searchId().searchId(), searchEvent);
    }
}
