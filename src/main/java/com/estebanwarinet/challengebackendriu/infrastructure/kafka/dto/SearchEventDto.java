package com.estebanwarinet.challengebackendriu.infrastructure.kafka.dto;

import com.estebanwarinet.challengebackendriu.application.event.SearchEvent;
import com.estebanwarinet.challengebackendriu.domain.model.Search;
import com.estebanwarinet.challengebackendriu.domain.model.SearchId;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.util.List;

public record SearchEventDto(
        String searchId,
        String hotelId,
        @JsonFormat(pattern = "yyyy-MM-dd") LocalDate checkIn,
        @JsonFormat(pattern = "yyyy-MM-dd") LocalDate checkOut,
        List<Integer> ages
) {

    public static SearchEventDto from(SearchEvent searchEvent) {
        return new SearchEventDto(
                searchEvent.searchId().searchId(),
                searchEvent.search().hotelId(),
                searchEvent.search().checkIn(),
                searchEvent.search().checkOut(),
                searchEvent.search().ages()
        );
    }

    public SearchEvent toSearchEvent() {
        return new SearchEvent(
                new SearchId(searchId),
                new Search(hotelId, checkIn, checkOut, ages)
        );
    }
}
