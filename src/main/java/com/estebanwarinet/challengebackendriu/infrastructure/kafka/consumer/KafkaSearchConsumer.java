package com.estebanwarinet.challengebackendriu.infrastructure.kafka.consumer;

import com.estebanwarinet.challengebackendriu.application.port.in.PersistSearchUseCase;
import com.estebanwarinet.challengebackendriu.infrastructure.kafka.dto.SearchEventDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Component
public class KafkaSearchConsumer {

    private final PersistSearchUseCase persistSearchUseCase;
    private static final ObjectMapper OBJECT_MAPPER =
            new ObjectMapper().registerModule(new JavaTimeModule());

    public KafkaSearchConsumer(PersistSearchUseCase persistSearchUseCase) {
        this.persistSearchUseCase = persistSearchUseCase;
    }

    @KafkaListener(topics = "hotel_availability_searches", groupId = "hotel-search-consumers")
    public void handleSearchEvent(String json) throws JsonProcessingException {
        SearchEventDto dto = OBJECT_MAPPER.readValue(json, SearchEventDto.class);
        persistSearchUseCase.persist(dto.toSearchEvent());
    }
}
