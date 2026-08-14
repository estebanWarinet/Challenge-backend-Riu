package com.estebanwarinet.challengebackendriu.infrastructure.rest;

import com.estebanwarinet.challengebackendriu.application.dto.SearchCountResult;
import com.estebanwarinet.challengebackendriu.application.port.in.CreateSearchUseCase;
import com.estebanwarinet.challengebackendriu.application.port.in.GetSearchCountUseCase;
import com.estebanwarinet.challengebackendriu.domain.model.Search;
import com.estebanwarinet.challengebackendriu.domain.model.SearchId;
import com.estebanwarinet.challengebackendriu.infrastructure.rest.dto.CountResponseDto;
import com.estebanwarinet.challengebackendriu.infrastructure.rest.dto.SearchIdResponseDto;
import com.estebanwarinet.challengebackendriu.infrastructure.rest.dto.SearchPayloadDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class SearchController {

    private final CreateSearchUseCase createSearchUseCase;
    private final GetSearchCountUseCase getSearchCountUseCase;

    public SearchController(
            CreateSearchUseCase createSearchUseCase,
            GetSearchCountUseCase getSearchCountUseCase
    ) {
        this.createSearchUseCase = createSearchUseCase;
        this.getSearchCountUseCase = getSearchCountUseCase;
    }

    @PostMapping("/search")
    public ResponseEntity<SearchIdResponseDto> createSearch(@Valid @RequestBody SearchPayloadDto searchDto) {

        Search search = new Search(
                searchDto.hotelId(), searchDto.checkIn(), searchDto.checkOut(), searchDto.ages());

        SearchId searchId = createSearchUseCase.createSearch(search);
        return ResponseEntity.ok(new SearchIdResponseDto(searchId.searchId()));
    }

    @GetMapping("/count")
    public ResponseEntity<CountResponseDto> countSearch(@RequestParam String searchId) {
        SearchCountResult result = getSearchCountUseCase.countSearch(new SearchId(searchId));
        Search search = result.search();
        SearchPayloadDto searchPayloadDto = new SearchPayloadDto(
                search.hotelId(), search.checkIn(), search.checkOut(), search.ages());
        return ResponseEntity.ok(new CountResponseDto(searchId, searchPayloadDto, result.count()));
    }
}
