package com.estebanwarinet.challengebackendriu.infrastructure.config;

import com.estebanwarinet.challengebackendriu.application.port.in.CreateSearchUseCase;
import com.estebanwarinet.challengebackendriu.application.port.out.SearchEventPublisher;
import com.estebanwarinet.challengebackendriu.application.service.CreateSearchService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public CreateSearchUseCase createSearchUseCase(SearchEventPublisher searchEventPublisher) {
        return new CreateSearchService(searchEventPublisher);
    }
}
