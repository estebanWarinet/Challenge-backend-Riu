package com.estebanwarinet.challengebackendriu.infrastructure.db;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface SearchJpaRepository extends JpaRepository<SearchEntity, Long> {

    Optional<SearchEntity> findBySearchId(String searchId);

    Long countBySignature(String signature);
}
