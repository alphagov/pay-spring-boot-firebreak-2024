package uk.gov.pay.spring.dao;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.ZonedDateTime;

@Entity
public class PayoutEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String gatewayPayoutId;
    private ZonedDateTime paidOutDate;
    private Long amount;

    public PayoutEntity() {}

    public PayoutEntity(String gatewayPayoutId, ZonedDateTime paidOutDate, Long amount) {
        this.gatewayPayoutId = gatewayPayoutId;
        this.paidOutDate = paidOutDate;
        this.amount = amount;
    }

    public String getGatewayPayoutId() {
        return gatewayPayoutId;
    }

    public void setGatewayPayoutId(String gatewayPayoutId) {
        this.gatewayPayoutId = gatewayPayoutId;
    }

    public ZonedDateTime getPaidOutDate() {
        return paidOutDate;
    }

    public void setPaidOutDate(ZonedDateTime paidOutDate) {
        this.paidOutDate = paidOutDate;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }
}
