package uk.gov.pay.spring.resource;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import uk.gov.pay.spring.dao.TokenEntity;
import uk.gov.pay.spring.repository.TokenRepository;

import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static uk.gov.pay.spring.model.TokenSource.API;
import static uk.gov.pay.spring.model.TokenSource.PRODUCTS;
import static uk.gov.pay.spring.model.TokenState.ACTIVE;
import static uk.gov.pay.spring.model.TokenState.REVOKED;

@SpringBootTest
@AutoConfigureMockMvc
class TokenResourceIT {

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        IntStream.rangeClosed(1,3).forEach(accountId -> {
            tokenRepository.save(new TokenEntity((long) accountId, UUID.randomUUID().toString(), ZonedDateTime.now(), REVOKED, API));
            tokenRepository.save(new TokenEntity((long) accountId, UUID.randomUUID().toString(), ZonedDateTime.now(), REVOKED, PRODUCTS));
            tokenRepository.save(new TokenEntity((long) accountId, UUID.randomUUID().toString(), ZonedDateTime.now(), ACTIVE, PRODUCTS));
            tokenRepository.save(new TokenEntity((long) accountId, UUID.randomUUID().toString(), ZonedDateTime.now(), ACTIVE, API));
        });
    }

    @AfterEach
    void tearDown() {
        tokenRepository.deleteAll();
    }

    @Test
    void getAllTokens() throws Exception {
        mvc.perform(get("/v1/token").queryParam("accountIds", "1", "2", "3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.length()", is(12)));
    }

    @Test
    void getByAccountIds() throws Exception {
        mvc.perform(get("/v1/token").queryParam("accountIds", "1", "2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.length()", is(8)))
                .andExpect(jsonPath("$..accountId", not(3)));
    }

    @Test
    void getByTokenStateAndTokenSource() throws Exception {
        mvc.perform(get("/v1/token/ACTIVE/API")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.length()", is(3)))
                .andExpect(jsonPath("$..tokenState", not("REVOKED")))
                .andExpect(jsonPath("$..tokenSource", not("PRODUCTS")));
    }
}