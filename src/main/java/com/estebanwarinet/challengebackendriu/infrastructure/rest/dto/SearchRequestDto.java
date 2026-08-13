package com.estebanwarinet.challengebackendriu.infrastructure.rest.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDate;
import java.util.List;

public record SearchRequestDto(@NotBlank String hotelId, @NotNull @JsonFormat(pattern = "dd/MM/yyyy") LocalDate checkIn,
                               @NotNull @JsonFormat(pattern = "dd/MM/yyyy") LocalDate checkOut,
                               @NotEmpty List<@PositiveOrZero @NotNull Integer> ages) {
}

