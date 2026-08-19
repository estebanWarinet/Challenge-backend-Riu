package com.estebanwarinet.challengebackendriu.domain.model;

import com.estebanwarinet.challengebackendriu.domain.exception.InvalidAgeException;
import com.estebanwarinet.challengebackendriu.domain.exception.InvalidDateRangeException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SearchTest {

    @Test
    void shouldCreateSearchWithValidData() {
        LocalDate checkIn = LocalDate.of(2026, 8, 20);
        LocalDate checkOut = LocalDate.of(2026, 8, 25);
        List<Integer> ages = List.of(30, 25, 5);

        Search search = new Search("hotel-123", checkIn, checkOut, ages);

        assertAll(
                () -> assertEquals("hotel-123", search.hotelId()),
                () -> assertEquals(checkIn, search.checkIn()),
                () -> assertEquals(checkOut, search.checkOut()),
                () -> assertEquals(ages, search.ages())
        );
    }

    @Test
    void shouldRejectNullHotelId() {
        assertThrows(NullPointerException.class,
                () -> new Search(
                        null,
                        LocalDate.of(2026, 8, 20),
                        LocalDate.of(2026, 8, 25),
                        List.of(30)
                ));
    }

    @Test
    void shouldRejectNullCheckIn() {
        assertThrows(NullPointerException.class,
                () -> new Search(
                        "hotel-123",
                        null,
                        LocalDate.of(2026, 8, 25),
                        List.of(30)
                ));
    }

    @Test
    void shouldRejectNullCheckOut() {
        assertThrows(NullPointerException.class,
                () -> new Search(
                        "hotel-123",
                        LocalDate.of(2026, 8, 20),
                        null,
                        List.of(30)
                ));
    }

    @Test
    void shouldRejectNullAges() {
        assertThrows(NullPointerException.class,
                () -> new Search(
                        "hotel-123",
                        LocalDate.of(2026, 8, 20),
                        LocalDate.of(2026, 8, 25),
                        null
                ));
    }

    @Test
    void shouldRejectCheckInAfterCheckOut() {
        assertThrows(InvalidDateRangeException.class,
                () -> new Search(
                        "hotel-123",
                        LocalDate.of(2026, 8, 25),
                        LocalDate.of(2026, 8, 20),
                        List.of(30)
                ));
    }

    @Test
    void shouldRejectSameCheckInAndCheckOut() {
        LocalDate date = LocalDate.of(2026, 8, 20);

        assertThrows(InvalidDateRangeException.class, () -> new Search("hotel-123", date, date, List.of(30)));
    }

    @Test
    void shouldRejectNegativeAge() {
        assertThrows(InvalidAgeException.class,
                () -> new Search(
                        "hotel-123",
                        LocalDate.of(2026, 8, 20),
                        LocalDate.of(2026, 8, 25),
                        List.of(30, -1, 5)
                ));
    }

    @Test
    void shouldAcceptZeroAge() {
        Search search = new Search(
                "hotel-123",
                LocalDate.of(2026, 8, 20),
                LocalDate.of(2026, 8, 25),
                List.of(0)
        );

        assertEquals(List.of(0), search.ages());
    }
}
