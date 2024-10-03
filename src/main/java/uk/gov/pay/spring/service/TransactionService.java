package uk.gov.pay.spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import uk.gov.pay.spring.dao.TransactionEntity;
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

    public Page<TransactionEntity> findAllWithPagination(TransactionSearchParams params, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo-1, pageSize, Sort.by("cardHolderName"));
        return transactionRepository.findAll(params.buildSpecification(), pageable);
    }
}
