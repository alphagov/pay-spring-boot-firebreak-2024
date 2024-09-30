package uk.gov.pay.spring.resource;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.pay.spring.model.Token;
import uk.gov.pay.spring.model.TokenSource;
import uk.gov.pay.spring.model.TokenState;
import uk.gov.pay.spring.service.TokenService;

import java.util.List;

@RestController
public class TokenResource {

    private final TokenService tokenService;

    public TokenResource(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @GetMapping(path = "/v1/token", produces = "application/json")
    public ResponseEntity<List<Token>> getByAccountIds(@RequestParam("accountIds") List<String> accountIds) {
        return ResponseEntity.ok().body(tokenService.findByAccountIds(accountIds));
    }

    @GetMapping(path = "/v1/token/{tokenState}/{tokenSource}", produces = "application/json")
    public ResponseEntity<List<Token>> getByTokenStateAndTokenSource(@PathVariable("tokenState") TokenState tokenState,
                                                       @PathVariable("tokenSource")TokenSource tokenSource) {
        return ResponseEntity.ok().body(tokenService.findByTokenStateAndTokenSource(tokenState, tokenSource));
    }
}
