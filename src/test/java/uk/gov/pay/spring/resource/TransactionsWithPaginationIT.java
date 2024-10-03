package uk.gov.pay.spring.resource;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import uk.gov.pay.spring.dao.TransactionEntity;
import uk.gov.pay.spring.repository.TransactionRepository;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static uk.gov.pay.spring.utils.RandomIdentifierGenerator.randomIdentifier;

@SpringBootTest
@AutoConfigureMockMvc
public class TransactionsWithPaginationIT {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private TransactionRepository transactionRepository;

    @BeforeEach
    void setUp() {
        IntStream.rangeClosed(1, 5).forEach(i ->
                transactionRepository.save(new TransactionEntity(1L, "finance@rnc.com",
                        randomIdentifier(), "Elon M", "Mastercard", ZonedDateTime.now(), null)));

        IntStream.rangeClosed(6, 10).forEach(i ->
                transactionRepository.save(new TransactionEntity(1L, "finance@rnc.com",
                        randomIdentifier(), "Peter T", "Mastercard", ZonedDateTime.now(), null)));

        IntStream.rangeClosed(11, 15).forEach(i ->
                transactionRepository.save(new TransactionEntity(1L, "finance@rnc.com",
                        randomIdentifier(), "Donald T", "Mastercard", ZonedDateTime.now(), null)));

        IntStream.rangeClosed(16, 20).forEach(i ->
                transactionRepository.save(new TransactionEntity(1L, "finance@rnc.com",
                        randomIdentifier(), "Kamala H", "Mastercard", ZonedDateTime.now(), null)));
    }

    @AfterEach
    void tearDown() {
        transactionRepository.deleteAll();
    }

    @Test
    void testPagination() throws Exception {
        mvc.perform(get("/v1/transactions-pageable")
                        .queryParam("pageNo", "1")
                        .queryParam("pageSize", "5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.totalPages", is(4)))
                .andExpect(jsonPath("$.totalElements", is(20)))
                .andExpect(jsonPath("$.size", is(5)))
                .andExpect(jsonPath("$.content[*].cardHolderName", everyItem(is("Donald T"))));

        mvc.perform(get("/v1/transactions-pageable")
                        .queryParam("pageNo", "2")
                        .queryParam("pageSize", "5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.totalPages", is(4)))
                .andExpect(jsonPath("$.totalElements", is(20)))
                .andExpect(jsonPath("$.size", is(5)))
                .andExpect(jsonPath("$.content[*].cardHolderName", everyItem(is("Elon M"))));

        mvc.perform(get("/v1/transactions-pageable")
                        .queryParam("pageNo", "3")
                        .queryParam("pageSize", "5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.totalPages", is(4)))
                .andExpect(jsonPath("$.totalElements", is(20)))
                .andExpect(jsonPath("$.size", is(5)))
                .andExpect(jsonPath("$.content[*].cardHolderName", everyItem(is("Kamala H"))));
    }
}
