package uk.gov.pay.spring.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import uk.gov.pay.spring.dao.TokenEntity;

import java.time.ZonedDateTime;

public record Token (
    Long id,
    Long accountId,
    String token,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm") ZonedDateTime issuedDate,
    TokenState tokenState,
    TokenSource tokenSource) {

    public static Token from(TokenEntity tokenEntity) {
        return new Token(tokenEntity.getId(), tokenEntity.getAccountId(), tokenEntity.getToken(), tokenEntity.getIssuedDate(),
                tokenEntity.getTokenState(), tokenEntity.getTokenSource());
    }
}
