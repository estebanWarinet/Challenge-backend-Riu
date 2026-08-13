package com.estebanwarinet.challengebackendriu.domain.model;

import java.util.UUID;

public record SearchId(String searchId) {
    public static SearchId random() {
        return new SearchId(UUID.randomUUID().toString());
    }
}
