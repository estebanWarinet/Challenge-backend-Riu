package com.estebanwarinet.challengebackendriu.infrastructure.db;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "search", indexes = {
        @Index(name = "idx_signature", columnList = "signature")
})
public class SearchEntity {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "search_id", unique = true)
    private String searchId;

    @Column(name = "hotel_id")
    private String hotelId;

    @Column(name = "check_in")
    private LocalDate checkIn;

    @Column(name = "check_out")
    private LocalDate checkOut;

    @Column(name = "ages")
    private String ages;

    @Column(name = "signature")
    private String signature;

    protected SearchEntity() {
    }

    public SearchEntity(
            String searchId,
            String hotelId,
            LocalDate checkIn,
            LocalDate checkOut,
            String ages,
            String signature
    ) {
        this.searchId = searchId;
        this.hotelId = hotelId;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.ages = ages;
        this.signature = signature;
    }

    public String getSearchId() {
        return searchId;
    }

    public String getHotelId() {
        return hotelId;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public String getAges() {
        return ages;
    }

    public String getSignature() {
        return signature;
    }
}
