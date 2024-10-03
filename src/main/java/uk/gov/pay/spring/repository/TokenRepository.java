package uk.gov.pay.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.gov.pay.spring.dao.TokenEntity;
import uk.gov.pay.spring.model.TokenSource;
import uk.gov.pay.spring.model.TokenState;

import java.util.List;

// Spring Data JPA creates CRUD implementation at runtime when the application context is loaded and
// the Spring Data repository beans are being initialized.
public interface TokenRepository extends JpaRepository<TokenEntity, Long> {

    List<TokenEntity> findByTokenStateAndTokenSource(TokenState tokenState, TokenSource tokenSource);

    List<TokenEntity> findByAccountIdIn(List<String> accountIds);
}
