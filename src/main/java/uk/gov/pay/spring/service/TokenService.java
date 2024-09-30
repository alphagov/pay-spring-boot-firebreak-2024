package uk.gov.pay.spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.pay.spring.dao.TokenRepository;
import uk.gov.pay.spring.model.Token;
import uk.gov.pay.spring.model.TokenSource;
import uk.gov.pay.spring.model.TokenState;

import java.util.List;

@Service
public class TokenService {

    @Autowired
    private TokenRepository tokenRepository;

    public List<Token> findByTokenStateAndTokenSource(TokenState tokenState, TokenSource tokenSource) {
        return tokenRepository.findByTokenStateAndTokenSource(tokenState, tokenSource).stream().map(Token::from).toList();
    }

    public List<Token> findByAccountIds(List<String> ids) {
        return tokenRepository.findByAccountIdIn(ids).stream().map(Token::from).toList();
    }
}
