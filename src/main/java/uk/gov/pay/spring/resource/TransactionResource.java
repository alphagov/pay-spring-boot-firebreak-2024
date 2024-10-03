package uk.gov.pay.spring.resource;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.pay.spring.dao.TransactionEntity;
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
            @RequestParam("toDate") @Nullable @DateTimeFormat(pattern="dd-MM-yyyy HH:mm") @Valid ZonedDateTime toDate,
            @RequestParam("fromSettledDate") @Nullable @DateTimeFormat(pattern="dd-MM-yyyy HH:mm") @Valid ZonedDateTime fromSettledDate,
            @RequestParam("toSettledDate") @Nullable @DateTimeFormat(pattern="dd-MM-yyyy HH:mm") @Valid ZonedDateTime toSettledDate) {

        var params = new TransactionSearchParams(accountIds, email, reference, cardHolderName, cardBrand, fromDate,
                toDate, fromSettledDate, toSettledDate);
        return ResponseEntity.ok().body(transactionService.findAll(params));
    }

    @GetMapping(path = "/v1/transactions-pageable", produces = "application/json")
    public ResponseEntity<Page<TransactionEntity>> searchReturningPages(
            @RequestParam("accountIds") @Nullable @Valid List<Long> accountIds,
            @RequestParam("email") @Nullable String email,
            @RequestParam("reference") @Nullable @Length(max = 10) String reference,
            @RequestParam("cardHolderName") @Nullable String cardHolderName,
            @RequestParam("cardBrand") @Nullable String cardBrand,
            @RequestParam("fromDate") @Nullable @DateTimeFormat(pattern="dd-MM-yyyy HH:mm") @Valid ZonedDateTime fromDate,
            @RequestParam("toDate") @Nullable @DateTimeFormat(pattern="dd-MM-yyyy HH:mm") @Valid ZonedDateTime toDate,
            @RequestParam("fromSettledDate") @Nullable @DateTimeFormat(pattern="dd-MM-yyyy HH:mm") @Valid ZonedDateTime fromSettledDate,
            @RequestParam("toSettledDate") @Nullable @DateTimeFormat(pattern="dd-MM-yyyy HH:mm") @Valid ZonedDateTime toSettledDate,
            @RequestParam(defaultValue = "1") @Min(value = 1) @Valid int pageNo,
            @RequestParam(defaultValue = "5") int pageSize) {

        var params = new TransactionSearchParams(accountIds, email, reference, cardHolderName, cardBrand, fromDate,
                toDate, fromSettledDate, toSettledDate);
        return ResponseEntity.ok().body(transactionService.findAllWithPagination(params, pageNo, pageSize));
    }

}
