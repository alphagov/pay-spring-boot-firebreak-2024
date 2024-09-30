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
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TransactionIT {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private TransactionRepository transactionRepository;

    @BeforeEach
    void setUp() {
        // gateway account id 1
        transactionRepository.save(new TransactionEntity(1L, "river.cartwright@sloughhouse.com", "ABCD123", "River Cartwright", "Visa", ZonedDateTime.now()));
        transactionRepository.save(new TransactionEntity(1L, "cat.standish@sloughhouse.com", "ABC1235", "Catherine Standish", "Visa", ZonedDateTime.now().minusHours(6)));
        transactionRepository.save(new TransactionEntity(1L, "sid.baker@sloughhouse.com", "AB12356", "Sid Baker", "Visa", ZonedDateTime.now().minusHours(12)));

        // gateway account id 2
        transactionRepository.save(new TransactionEntity(2L, "hermione.granger@hp.com", "ABCD123", "Hermione Granger", "Mastercard", ZonedDateTime.now()));
        transactionRepository.save(new TransactionEntity(2L, "mundungus.fletcher@hp.com", "1234XYZ", "Mundungus Fletcher", "American Express", ZonedDateTime.now().minusHours(6)));
        transactionRepository.save(new TransactionEntity(2L, "death.eater@hp.com", "134XYZZ", "Death Eater", "American Express", ZonedDateTime.now().minusHours(12)));
    }

    @AfterEach
    void tearDown() {
        transactionRepository.deleteAll();
    }

    @Test
    void search() throws Exception {
        // search all gateway accounts
        mvc.perform(get("/v1/transactions")
                        .queryParam("accountIds", "1", "2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(6)));

        // search by reference ABCD123
        mvc.perform(get("/v1/transactions")
                        .queryParam("reference", "ABCD123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)));

        // search by email and reference
        mvc.perform(get("/v1/transactions")
                        .queryParam("reference", "ABCD123")
                        .queryParam("email", "hermione.granger@hp.com")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$[0].reference", is("ABCD123")))
                .andExpect(jsonPath("$[0].email", is("hermione.granger@hp.com")))
                .andExpect(jsonPath("$[0].accountId", is(2)))
                .andExpect(jsonPath("$[0].cardHolderName", is("Hermione Granger")))
                .andExpect(jsonPath("$[0].cardBrand", is("Mastercard")))
                .andExpect(jsonPath("$[0].createdDate", notNullValue()));

        // search by gateway account 1 and created date between
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        mvc.perform(get("/v1/transactions")
                        .queryParam("accountIds", "1")
                        .queryParam("fromDate", ZonedDateTime.now().minusDays(1).format(formatter))
                        .queryParam("toDate", ZonedDateTime.now().minusHours(1).format(formatter))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)))
                .andExpect(jsonPath("$..cardHolderName", not("River Cartwright")));

        // search where name contains "at" - Catherine, Death Eater
        mvc.perform(get("/v1/transactions")
                        .queryParam("cardHolderName", "at")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)));
    }

    @Test
    void validationErrors() throws Exception {
        // reference
        mvc.perform(get("/v1/transactions")
                        .queryParam("reference", "123456789021343")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        // incorrectly formatted dates
        mvc.perform(get("/v1/transactions")
                        .queryParam("fromDate", "12/1999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        // invalid account ids
        mvc.perform(get("/v1/transactions")
                        .queryParam("accountIds", "a", "b")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
