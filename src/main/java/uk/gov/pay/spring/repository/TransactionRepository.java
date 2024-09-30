package uk.gov.pay.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import uk.gov.pay.spring.dao.TransactionEntity;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long>, JpaSpecificationExecutor<TransactionEntity> {
}
