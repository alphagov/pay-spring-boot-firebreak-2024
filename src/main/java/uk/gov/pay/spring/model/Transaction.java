package uk.gov.pay.spring.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import uk.gov.pay.spring.dao.TransactionEntity;

import java.time.ZonedDateTime;

public record Transaction(
        Long accountId,
        String email,
        String reference,
        String cardHolderName,
        String cardBrand,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm") ZonedDateTime createdDate
) {

    public static Transaction from(TransactionEntity transaction) {
        return new Transaction(transaction.getAccountId(), transaction.getEmail(), transaction.getReference(),
                transaction.getCardHolderName(), transaction.getCardBrand(), transaction.getCreatedDate());
    }
}
