package uk.gov.pay.spring.dao;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.ZonedDateTime;

@Entity
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long accountId;
    private String email;
    private String reference;
    private String cardHolderName;
    private String cardBrand;
    private ZonedDateTime createdDate;

    public TransactionEntity() {}

    public TransactionEntity(Long accountId, String email, String reference, String cardHolderName, String cardBrand, ZonedDateTime createdDate) {
        this.accountId = accountId;
        this.email = email;
        this.reference = reference;
        this.cardHolderName = cardHolderName;
        this.cardBrand = cardBrand;
        this.createdDate = createdDate;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public String getCardBrand() {
        return cardBrand;
    }

    public void setCardBrand(String cardBrand) {
        this.cardBrand = cardBrand;
    }
}
