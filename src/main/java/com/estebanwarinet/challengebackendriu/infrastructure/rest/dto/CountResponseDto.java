package com.estebanwarinet.challengebackendriu.infrastructure.rest.dto;

public record CountResponseDto(String searchId, SearchPayloadDto search, Long count) {
}
