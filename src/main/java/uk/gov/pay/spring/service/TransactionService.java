package uk.gov.pay.spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.pay.spring.model.Transaction;
import uk.gov.pay.spring.model.TransactionSearchParams;
import uk.gov.pay.spring.repository.TransactionRepository;

import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public List<Transaction> findAll(TransactionSearchParams params) {
        return transactionRepository.findAll(params.buildSpecification()).stream().map(Transaction::from).toList();
    }
}
