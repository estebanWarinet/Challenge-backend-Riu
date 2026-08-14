package com.estebanwarinet.challengebackendriu.domain.model;

import com.estebanwarinet.challengebackendriu.domain.exception.InvalidAgeException;
import com.estebanwarinet.challengebackendriu.domain.exception.InvalidDateRangeException;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public record Search(String hotelId, @JsonFormat(pattern = "yyyy-MM-dd") LocalDate checkIn,
                     @JsonFormat(pattern = "yyyy-MM-dd") LocalDate checkOut, List<Integer> ages) {
    public Search {
        Objects.requireNonNull(hotelId, "hotelId es obligatorio");
        Objects.requireNonNull(checkIn, "checkIn es obligatorio");
        Objects.requireNonNull(checkOut, "checkOut es obligatorio");
        Objects.requireNonNull(ages, "ages es obligatorio");
        if (!checkIn.isBefore(checkOut)) {
            throw new InvalidDateRangeException("checkIn debe ser anterior a checkOut");
        }
        boolean areAllValuesBiggerThanZero = ages.stream().allMatch(n -> n >= 0);

        if (!areAllValuesBiggerThanZero) {
            throw new InvalidAgeException("Todas las edades deben ser mayores o iguales a 0");
        }
        ages = List.copyOf(ages);
    }
}