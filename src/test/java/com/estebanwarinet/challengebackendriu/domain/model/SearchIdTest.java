package com.estebanwarinet.challengebackendriu.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SearchIdTest {

    @Test
    void shouldCreateDifferentId() {
        SearchId searchId1 = SearchId.random();
        SearchId searchId2 = SearchId.random();

        assertNotEquals(searchId1, searchId2);
    }

    @Test
    void shouldCreateNotNullId() {
        SearchId searchId = SearchId.random();

        assertNotNull(searchId);
    }
}
