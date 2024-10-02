package uk.gov.pay.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.gov.pay.spring.dao.PayoutEntity;

@Repository
public interface PayoutRepository extends JpaRepository<PayoutEntity, Long> {
}
