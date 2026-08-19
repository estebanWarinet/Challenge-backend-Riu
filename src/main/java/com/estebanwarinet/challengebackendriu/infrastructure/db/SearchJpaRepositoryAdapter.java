package com.estebanwarinet.challengebackendriu.infrastructure.db;

import com.estebanwarinet.challengebackendriu.domain.repository.SearchRepository;
import com.estebanwarinet.challengebackendriu.domain.model.Search;
import com.estebanwarinet.challengebackendriu.domain.model.SearchId;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class SearchJpaRepositoryAdapter implements SearchRepository {

    private final SearchJpaRepository searchJpaRepository;

    public SearchJpaRepositoryAdapter(SearchJpaRepository searchJpaRepository) {
        this.searchJpaRepository = searchJpaRepository;
    }

    @Override
    @Transactional
    public void saveSearch(SearchId searchId, Search search) {
        String agesCsv = search.ages().stream()
                .map(String::valueOf).collect(Collectors.joining(","));

        String signature = buildSignature(search);

        SearchEntity searchEntity = new SearchEntity(
                searchId.searchId(),
                search.hotelId(),
                search.checkIn(),
                search.checkOut(),
                agesCsv,
                signature
        );

        searchJpaRepository.save(searchEntity);
    }

    @Override
    public Optional<Search> findSearch(SearchId searchId) {
        return searchJpaRepository.findBySearchId(
                searchId.searchId()).map(e -> new Search(
                e.getHotelId(),
                e.getCheckIn(),
                e.getCheckOut(),
                parseAges(e.getAges()
                )));
    }

    @Override
    public Long countSearch(Search search) {
        String signature = buildSignature(search);
        return searchJpaRepository.countBySignature(signature);
    }

    private String buildSignature(Search search) {
        String agesCsv = search.ages().stream()
                .map(String::valueOf).collect(Collectors.joining(","));
        return String.join("|", search.hotelId(), search.checkIn().toString(),
                search.checkOut().toString(), agesCsv);
    }

    private List<Integer> parseAges(String agesCsv) {
        return Arrays.stream(agesCsv.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }
}
