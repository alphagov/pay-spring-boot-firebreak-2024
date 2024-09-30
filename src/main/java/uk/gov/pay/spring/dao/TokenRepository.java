package uk.gov.pay.spring.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.gov.pay.spring.model.TokenSource;
import uk.gov.pay.spring.model.TokenState;

import java.util.List;

// Spring Data JPA creates CRUD implementation at runtime automatically.
public interface TokenRepository extends JpaRepository<TokenEntity, Long> {

    List<TokenEntity> findByTokenStateAndTokenSource(TokenState tokenState, TokenSource tokenSource); // this auto-completes!!

    List<TokenEntity> findByAccountIdIn(List<String> accountIds);
}
