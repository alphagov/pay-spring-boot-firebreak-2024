package uk.gov.pay.spring.resource;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.pay.spring.model.Transaction;
import uk.gov.pay.spring.model.TransactionSearchParams;
import uk.gov.pay.spring.service.TransactionService;

import java.time.ZonedDateTime;
import java.util.List;

@RestController
public class TransactionResource {

    @Autowired
    private TransactionService transactionService;

    @GetMapping(path = "/v1/transactions", produces = "application/json")
    public ResponseEntity<List<Transaction>> search(
            @RequestParam("accountIds") @Nullable @Valid List<Long> accountIds,
            @RequestParam("email") @Nullable String email,
            @RequestParam("reference") @Nullable @Length(max = 10) String reference,
            @RequestParam("cardHolderName") @Nullable String cardHolderName,
            @RequestParam("cardBrand") @Nullable String cardBrand,
            @RequestParam("fromDate") @Nullable @DateTimeFormat(pattern="dd-MM-yyyy HH:mm") @Valid ZonedDateTime fromDate,
            @RequestParam("toDate") @Nullable @DateTimeFormat(pattern="dd-MM-yyyy HH:mm") @Valid ZonedDateTime toDate) {
        TransactionSearchParams params = new TransactionSearchParams(accountIds, email, reference, cardHolderName, cardBrand, fromDate, toDate);
        return ResponseEntity.ok().body(transactionService.findAll(params));
    }
}
