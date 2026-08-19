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
        LocalDate checkIn = LocalDate.of(2026, 8, 20);
        LocalDate checkOut = LocalDate.of(2026, 8, 25);
        List<Integer> ages = List.of(30);

        assertThrows(NullPointerException.class, () -> new Search(null, checkIn, checkOut, ages));
    }

    @Test
    void shouldRejectNullCheckIn() {
        String hotelId = "hotel-123";
        LocalDate checkout = LocalDate.of(2026, 8, 25);
        List<Integer> ages = List.of(30);

        assertThrows(NullPointerException.class, () -> new Search(hotelId, null, checkout, ages));
    }

    @Test
    void shouldRejectNullCheckOut() {
        String hotelId = "hotel-123";
        LocalDate checkIn = LocalDate.of(2026, 8, 20);
        List<Integer> ages = List.of(30);

        assertThrows(NullPointerException.class, () -> new Search(hotelId, checkIn, null, ages));
    }

    @Test
    void shouldRejectNullAges() {
        String hotelId = "hotel-123";
        LocalDate checkIn = LocalDate.of(2026, 8, 20);
        LocalDate checkOut = LocalDate.of(2026, 8, 25);

        assertThrows(NullPointerException.class, () -> new Search(hotelId, checkIn, checkOut, null));
    }

    @Test
    void shouldRejectCheckInAfterCheckOut() {
        String hotelId = "hotel-123";
        LocalDate checkIn = LocalDate.of(2026, 8, 25);
        LocalDate checkOut = LocalDate.of(2026, 8, 20);
        List<Integer> ages = List.of(30);

        assertThrows(InvalidDateRangeException.class, () -> new Search(hotelId, checkIn, checkOut, ages));
    }

    @Test
    void shouldRejectSameCheckInAndCheckOut() {
        String hotelId = "hotel-123";
        LocalDate date = LocalDate.of(2026, 8, 20);
        List<Integer> ages = List.of(30);

        assertThrows(InvalidDateRangeException.class, () -> new Search(hotelId, date, date, ages));
    }

    @Test
    void shouldRejectNegativeAge() {
        String hotelId = "hotel-123";
        LocalDate checkIn = LocalDate.of(2026, 8, 20);
        LocalDate checkOut = LocalDate.of(2026, 8, 25);
        List<Integer> ages = List.of(30, -1, 5);

        assertThrows(InvalidAgeException.class, () -> new Search(hotelId, checkIn, checkOut, ages));
    }

    @Test
    void shouldAcceptZeroAge() {
        String hotelId = "hotel-123";
        LocalDate checkIn = LocalDate.of(2026, 8, 20);
        LocalDate checkOut = LocalDate.of(2026, 8, 25);
        List<Integer> ages = List.of(0);

        Search search = new Search(hotelId, checkIn, checkOut, ages);

        assertEquals(List.of(0), search.ages());
    }
}
