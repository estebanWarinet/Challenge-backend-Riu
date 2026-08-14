package com.estebanwarinet.challengebackendriu.infrastructure.rest.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDate;
import java.util.List;

public record SearchPayloadDto(
        @Schema(description = "Identificador del hotel", example = "1234aBc") @NotBlank String hotelId,
        @Schema(description = "Fecha de check-in (dd/MM/yyyy)", example = "29/12/2023")
        @NotNull @JsonFormat(pattern = "dd/MM/yyyy") LocalDate checkIn,
        @Schema(description = "Fecha de check-out (dd/MM/yyyy); debe ser posterior al check-in", example = "31/12/2023")
        @NotNull @JsonFormat(pattern = "dd/MM/yyyy") LocalDate checkOut,
        @Schema(description = "Edades de los huéspedes; el orden influye en el conteo", example = "[30, 29, 1, 3]")
        @NotEmpty List<@PositiveOrZero Integer> ages
) {
}

