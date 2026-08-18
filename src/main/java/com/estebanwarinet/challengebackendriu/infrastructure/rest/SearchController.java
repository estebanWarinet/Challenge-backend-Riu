package com.estebanwarinet.challengebackendriu.infrastructure.rest;

import com.estebanwarinet.challengebackendriu.application.dto.SearchCountResult;
import com.estebanwarinet.challengebackendriu.application.port.in.CreateSearchUseCase;
import com.estebanwarinet.challengebackendriu.application.port.in.GetSearchCountUseCase;
import com.estebanwarinet.challengebackendriu.domain.model.Search;
import com.estebanwarinet.challengebackendriu.domain.model.SearchId;
import com.estebanwarinet.challengebackendriu.infrastructure.rest.dto.CountResponseDto;
import com.estebanwarinet.challengebackendriu.infrastructure.rest.dto.ErrorResponse;
import com.estebanwarinet.challengebackendriu.infrastructure.rest.dto.SearchIdResponseDto;
import com.estebanwarinet.challengebackendriu.infrastructure.rest.dto.SearchPayloadDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Búsquedas", description = "Registro y conteo de búsquedas de disponibilidad hotelera")
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

    @Operation(summary = "Registrar una búsqueda",
            description = "Valida el payload, lo publica al topic de Kafka y devuelve el searchId generado.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Búsqueda registrada"),
            @ApiResponse(responseCode = "400", description = "Payload inválido",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/search")
    public ResponseEntity<SearchIdResponseDto> createSearch(@Valid @RequestBody SearchPayloadDto searchDto) {

        Search search = new Search(
                searchDto.hotelId(), searchDto.checkIn(), searchDto.checkOut(), searchDto.ages());

        SearchId searchId = createSearchUseCase.createSearch(search);
        return ResponseEntity.status(HttpStatus.CREATED).body(new SearchIdResponseDto(searchId.searchId()));
    }

    @Operation(summary = "Contar búsquedas idénticas",
            description = "Devuelve la búsqueda original y cuántas búsquedas idénticas se registraron.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Búsqueda encontrada con su contador"),
            @ApiResponse(responseCode = "400", description = "Falta el parámetro searchId"),
            @ApiResponse(responseCode = "404", description = "searchId inexistente",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/count")
    public ResponseEntity<CountResponseDto> countSearch(@RequestParam String searchId) {
        SearchCountResult result = getSearchCountUseCase.countSearch(new SearchId(searchId));
        Search search = result.search();
        SearchPayloadDto searchPayloadDto = new SearchPayloadDto(
                search.hotelId(), search.checkIn(), search.checkOut(), search.ages());
        return ResponseEntity.ok(new CountResponseDto(searchId, searchPayloadDto, result.count()));
    }
}
