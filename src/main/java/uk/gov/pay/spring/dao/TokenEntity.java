package uk.gov.pay.spring.dao;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import uk.gov.pay.spring.model.TokenState;
import uk.gov.pay.spring.model.TokenSource;

import java.time.ZonedDateTime;

@Entity
public class TokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long accountId;
    private String token;
    private ZonedDateTime issuedDate;
    private TokenState tokenState;
    private TokenSource tokenSource;

    public TokenEntity(Long accountId, String token, ZonedDateTime issuedDate, TokenState tokenState, TokenSource tokenSource) {
        this.accountId = accountId;
        this.token = token;
        this.issuedDate = issuedDate;
        this.tokenState = tokenState;
        this.tokenSource = tokenSource;
    }

    public TokenEntity() {}

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public TokenState getTokenState() {
        return tokenState;
    }

    public void setTokenState(TokenState tokenState) {
        this.tokenState = tokenState;
    }

    public TokenSource getTokenSource() {
        return tokenSource;
    }

    public void setTokenSource(TokenSource tokenSource) {
        this.tokenSource = tokenSource;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public ZonedDateTime getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(ZonedDateTime issuedDate) {
        this.issuedDate = issuedDate;
    }
}
