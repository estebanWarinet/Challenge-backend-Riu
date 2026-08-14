package com.estebanwarinet.challengebackendriu.infrastructure.rest;

import com.estebanwarinet.challengebackendriu.application.port.in.CreateSearchUseCase;
import com.estebanwarinet.challengebackendriu.domain.model.Search;
import com.estebanwarinet.challengebackendriu.domain.model.SearchId;
import com.estebanwarinet.challengebackendriu.infrastructure.rest.dto.SearchRequestDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping()
public class SearchController {

    private final CreateSearchUseCase createSearchUseCase;

    public SearchController(CreateSearchUseCase createSearchUseCase) {
        this.createSearchUseCase = createSearchUseCase;
    }

    @PostMapping("/search")
    public ResponseEntity<SearchId> createSearch(@Valid @RequestBody SearchRequestDto searchDto) {

        Search search = new Search(searchDto.hotelId(), searchDto.checkIn(), searchDto.checkOut(), searchDto.ages());

        SearchId searchId = createSearchUseCase.createSearch(search);
        return ResponseEntity.ok(searchId);
    }
}
