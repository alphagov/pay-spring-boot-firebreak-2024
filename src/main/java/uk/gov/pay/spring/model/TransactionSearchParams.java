package uk.gov.pay.spring.model;

import jakarta.persistence.criteria.Join;
import jakarta.validation.Valid;
import org.springframework.data.jpa.domain.Specification;
import uk.gov.pay.spring.dao.PayoutEntity;
import uk.gov.pay.spring.dao.TransactionEntity;

import java.time.ZonedDateTime;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class TransactionSearchParams {

    private final List<Long> accountIds;
    private final String email;
    private final String reference;
    private final String cardHolderName;
    private final String cardBrand;
    private final ZonedDateTime fromDate;
    private final ZonedDateTime toDate;
    private final ZonedDateTime fromSettledDate;
    private final ZonedDateTime toSettledDate;

    public TransactionSearchParams(List<Long> accountIds,
                                   String email,
                                   String reference,
                                   String cardHolderName,
                                   String cardBrand,
                                   ZonedDateTime fromDate,
                                   ZonedDateTime toDate,
                                   ZonedDateTime fromSettledDate,
                                   ZonedDateTime toSettledDate) {
        this.accountIds = accountIds;
        this.email = email;
        this.reference = reference;
        this.cardHolderName = cardHolderName;
        this.cardBrand = cardBrand;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.fromSettledDate = fromSettledDate;
        this.toSettledDate = toSettledDate;
    }

    public Specification<TransactionEntity> buildSpecification() {
        Specification<TransactionEntity> specification = Specification.where(null);

        if (accountIds != null && !accountIds.isEmpty()) {
            specification = specification.and((Specification<TransactionEntity>) (root, query, criteriaBuilder) ->
                    criteriaBuilder.in(root.get("accountId")).value(accountIds));
        }

        if (isNotBlank(reference)) {
            specification = specification.and((Specification<TransactionEntity>) (root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("reference"), reference));
        }

        if (isNotBlank(email)) {
            specification = specification.and((Specification<TransactionEntity>) (root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("email"), email));
        }

        if (fromDate != null) {
            specification = specification.and((Specification<TransactionEntity>) (root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThan(root.get("createdDate"), fromDate));
        }

        if (toDate != null) {
            specification = specification.and((Specification<TransactionEntity>) (root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThan(root.get("createdDate"), toDate));
        }

        if (isNotBlank(cardHolderName)) {
            specification = specification.and((Specification<TransactionEntity>) (root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("cardHolderName"), "%" + cardHolderName + "%"));
        }

        if (isNotBlank(cardBrand)) {
            specification = specification.and((Specification<TransactionEntity>) (root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("cardBrand"), cardBrand));
        }

        if (fromSettledDate != null) {
            specification = specification.and((Specification<TransactionEntity>) (root, query, criteriaBuilder) ->
            {
                Join<TransactionEntity, PayoutEntity> payoutEntityJoin = root.join("payout");
                return criteriaBuilder.greaterThan(payoutEntityJoin.get("paidOutDate"), fromSettledDate);
            });
        }

        if (toSettledDate != null) {
            specification = specification.and((Specification<TransactionEntity>) (root, query, criteriaBuilder) ->
            {
                Join<TransactionEntity, PayoutEntity> payoutEntityJoin = root.join("payout");
                return criteriaBuilder.lessThan(payoutEntityJoin.get("paidOutDate"), toSettledDate);
            });
        }

        return specification;
    }
}
