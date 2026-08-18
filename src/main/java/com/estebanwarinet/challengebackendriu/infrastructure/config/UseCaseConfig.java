package com.estebanwarinet.challengebackendriu.infrastructure.config;

import com.estebanwarinet.challengebackendriu.application.port.in.CreateSearchUseCase;
import com.estebanwarinet.challengebackendriu.application.port.in.GetSearchCountUseCase;
import com.estebanwarinet.challengebackendriu.application.port.in.PersistSearchUseCase;
import com.estebanwarinet.challengebackendriu.application.port.out.SearchEventPublisher;
import com.estebanwarinet.challengebackendriu.application.port.out.SearchRepository;
import com.estebanwarinet.challengebackendriu.application.service.CreateSearchService;
import com.estebanwarinet.challengebackendriu.application.service.GetSearchCountService;
import com.estebanwarinet.challengebackendriu.application.service.PersistSearchService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class UseCaseConfig {

    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }

    @Bean
    public CreateSearchUseCase createSearchUseCase(SearchEventPublisher searchEventPublisher, Clock clock) {
        return new CreateSearchService(searchEventPublisher, clock);
    }

    @Bean
    public PersistSearchUseCase persistSearchUseCase(SearchRepository searchRepository) {
        return new PersistSearchService(searchRepository);
    }

    @Bean
    public GetSearchCountUseCase getSearchCountUseCase(SearchRepository searchRepository) {
        return new GetSearchCountService(searchRepository);
    }
}
