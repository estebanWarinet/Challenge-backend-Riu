package com.estebanwarinet.challengebackendriu.infrastructure.rest;


import com.estebanwarinet.challengebackendriu.application.dto.SearchCountResult;
import com.estebanwarinet.challengebackendriu.application.port.in.CreateSearchUseCase;
import com.estebanwarinet.challengebackendriu.application.port.in.GetSearchCountUseCase;
import com.estebanwarinet.challengebackendriu.domain.exception.PastSearchDateException;
import com.estebanwarinet.challengebackendriu.domain.exception.SearchNotFoundException;
import com.estebanwarinet.challengebackendriu.domain.model.Search;
import com.estebanwarinet.challengebackendriu.domain.model.SearchId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SearchController.class)
class SearchControllerTest {

    private final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final String VALID_CHECK_IN = LocalDate.now().plusDays(2).format(FORMAT);
    private final String VALID_CHECK_OUT = LocalDate.now().plusDays(20).format(FORMAT);

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CreateSearchUseCase createSearchUseCase;

    @MockitoBean
    private GetSearchCountUseCase getSearchCountUseCase;

    @Test
    void shouldCreateSearch() throws Exception {

        SearchId searchId = new SearchId("uuid-1");

        when(createSearchUseCase.createSearch(any(Search.class)))
                .thenReturn(searchId);

        String template = """
                {
                    "hotelId": "hotel-123",
                    "checkIn": "%s",
                    "checkOut": "%s",
                    "ages": [30, 5]
                }
                """;
        String requestBody = String.format(template, VALID_CHECK_IN, VALID_CHECK_OUT);

        mockMvc.perform(
                        post("/search")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.searchId").value("uuid-1"));

        verify(createSearchUseCase).createSearch(any(Search.class));
    }

    @Test
    void shouldCountSearch() throws Exception {

        SearchId searchId = new SearchId("uuid-1");

        Search search = new Search(
                "hotel-123",
                LocalDate.of(2026, 8, 20),
                LocalDate.of(2026, 8, 25),
                List.of(30, 5)
        );

        SearchCountResult result = new SearchCountResult(
                searchId,
                search,
                3L
        );

        when(getSearchCountUseCase.countSearch(any(SearchId.class)))
                .thenReturn(result);

        mockMvc.perform(
                        get("/count")
                                .param("searchId", "uuid-1")
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.searchId").value("uuid-1"))
                .andExpect(jsonPath("$.search.hotelId").value("hotel-123"))
                .andExpect(jsonPath("$.search.checkIn").value("20/08/2026"))
                .andExpect(jsonPath("$.search.checkOut").value("25/08/2026"))
                .andExpect(jsonPath("$.search.ages[0]").value(30))
                .andExpect(jsonPath("$.search.ages[1]").value(5))
                .andExpect(jsonPath("$.count").value(3));

        verify(getSearchCountUseCase).countSearch(any(SearchId.class));
    }

    @Test
    void shouldReturn400WhenHotelIdIsMissing() throws Exception {
        String body = """
                {
                    "checkIn": "%s",
                    "checkOut": "%s",
                    "ages": [30, 5]
                }
                """;
        String requestBody = String.format(body, VALID_CHECK_IN, VALID_CHECK_OUT);

        mockMvc.perform(post("/search").contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.message").exists());
    }

    @Test
    void shouldReturn400WhenAgesIsMissing() throws Exception {
        String body = """
                {
                    "hotelId": "hotel-123",
                    "checkIn": "%s",
                    "checkOut": "%s",
                    "ages": []
                }
                """;
        String requestBody = String.format(body, VALID_CHECK_IN, VALID_CHECK_OUT);

        mockMvc.perform(post("/search").contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.message").exists());
    }

    @Test
    void shouldReturn400WhenAgesIsNegative() throws Exception {
        String body = """
                {
                    "hotelId": "hotel-123",
                    "checkIn": "%s",
                    "checkOut": "%s",
                    "ages": [30, -5]
                }
                """;
        String requestBody = String.format(body, VALID_CHECK_IN, VALID_CHECK_OUT);

        mockMvc.perform(post("/search").contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.message").exists());
    }

    @Test
    void shouldReturn400WhenCheckInIsAfterCheckOut() throws Exception {
        String body = """
                {
                    "hotelId": "hotel-123",
                    "checkIn": "30/08/2026",
                    "checkOut": "25/08/2026",
                    "ages": [30, 5]
                }
                """;

        mockMvc.perform(post("/search").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.message").exists());
    }

    @Test
    void shouldReturn400WhenNotValidJsonFormat() throws Exception {
        String body = """
                {
                    "{ hotelId": "hotel-123",
                    "checkIn": "%s",
                    "checkOut": "%s",
                    "ages": [30, 5]
                }
                """;
        String requestBody = String.format(body, VALID_CHECK_IN, VALID_CHECK_OUT);

        mockMvc.perform(post("/search").contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.message").exists());
    }

    @Test
    void shouldReturn400WhenDateNotHaveValidFormat() throws Exception {
        String body = """
                {
                    "hotelId": "hotel-123",
                    "checkIn": "20-08-2026",
                    "checkOut": "25/08/2026",
                    "ages": [30, 5]
                }
                """;

        mockMvc.perform(post("/search").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.message").exists());
    }

    @Test
    void shouldReturn404WhenSearchIdNotFound() throws Exception {
        when(getSearchCountUseCase.countSearch(any(SearchId.class)))
                .thenThrow(new SearchNotFoundException(new SearchId("no-existe")));

        mockMvc.perform(get("/count").param("searchId", "no-existe"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void shouldReturn400WhenCheckInIsPastDate() throws Exception {
        String requestBody = """
                {
                    "hotelId": "hotel-123",
                    "checkIn": "10/08/2026",
                    "checkOut": "25/08/2026",
                    "ages": [30, 5]
                }
                """;

        when(createSearchUseCase.createSearch(any(Search.class)))
                .thenThrow(new PastSearchDateException("La fecha checkIn debe ser una fecha actual o futura"));


        mockMvc.perform(post("/search").contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }
}