package uk.gov.pay.spring.resource;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import uk.gov.pay.spring.dao.PayoutEntity;
import uk.gov.pay.spring.dao.TransactionEntity;
import uk.gov.pay.spring.repository.PayoutRepository;
import uk.gov.pay.spring.repository.TransactionRepository;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * This test is for demonstrating searching on join columns. Here we want to search
 * transactions by the payout date
 */
@SpringBootTest
@AutoConfigureMockMvc
public class TransactionWithPayoutsIT {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private PayoutRepository payoutRepository;

    @BeforeEach
    void setUp() {
        var payoutYesterday = new PayoutEntity("po_2", ZonedDateTime.now().minusDays(1), 10L);
        payoutRepository.save(payoutYesterday);
        transactionRepository.save(new TransactionEntity(1L, "river.cartwright@sloughhouse.com",
                "ABCD123", "River Cartwright", "Visa", ZonedDateTime.now().minusDays(2),
                payoutYesterday));
        transactionRepository.save(new TransactionEntity(1L, "cat.standish@sloughhouse.com",
                "ABC1235", "Catherine Standish", "Visa", ZonedDateTime.now().minusDays(2),
                payoutYesterday));

        var payoutWeekAgo = new PayoutEntity("po_1", ZonedDateTime.now().minusWeeks(1), 10L);
        payoutRepository.save(payoutWeekAgo);
        transactionRepository.save(new TransactionEntity(1L, "doctor.doom@marvel.com",
                "AB12356", "Doctor Doom", "Visa", ZonedDateTime.now().minusWeeks(1).minusDays(1),
                payoutWeekAgo));
    }

    @AfterEach
    void tearDown() {
        transactionRepository.deleteAll();
        payoutRepository.deleteAll();
    }

    @Test
    void search() throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        mvc.perform(get("/v1/transactions")
                        .queryParam("fromSettledDate", ZonedDateTime.now().minusWeeks(1).minusDays(1).format(formatter))
                        .queryParam("toSettledDate", ZonedDateTime.now().minusDays(5).format(formatter))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$[0].reference", is("AB12356")))
                .andExpect(jsonPath("$[0].email", is("doctor.doom@marvel.com")))
                .andExpect(jsonPath("$[0].accountId", is(1)))
                .andExpect(jsonPath("$[0].cardHolderName", is("Doctor Doom")))
                .andExpect(jsonPath("$[0].cardBrand", is("Visa")))
                .andExpect(jsonPath("$[0].createdDate", notNullValue()));
    }
}
