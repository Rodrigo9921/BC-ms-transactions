package com.nttdata.mstransaccions.service.impl;


import com.nttdata.mstransaccions.dto.TransactionDto;
import com.nttdata.mstransaccions.model.Transaction;
import com.nttdata.mstransaccions.repository.TransactionRepository;
import com.nttdata.mstransaccions.service.TransactionService;

import com.nttdata.mstransaccions.utils.TransactionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final WebClient webClient;
    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository,WebClient webClient) {
        this.transactionRepository = transactionRepository;
        this.webClient=webClient;
    }

    @Override
    public Mono<TransactionDto> createTransaction(TransactionDto transactionDto) {
        return transactionRepository.save(TransactionMapper.convertToEntity(transactionDto))
                .map(TransactionMapper::convertToDto);
    }

    @Override
    public Flux<TransactionDto> getAllTransactions() {
        return transactionRepository.findAll()
                .map(TransactionMapper::convertToDto);
    }

    @Override
    public Flux<TransactionDto> getTransactionsByClientId(String clientId) {
        return transactionRepository.findByClientId(clientId)
                .map(TransactionMapper::convertToDto);
    }

    @Override
    public Flux<TransactionDto> getTransactionsByProductId(String productId) {
        return transactionRepository.findByProductId(productId)
                .map(TransactionMapper::convertToDto);
    }
    @Override
    public Mono<TransactionDto> createPaymentTransaction(String clientId, String productId, double amount) {
        Transaction transaction = new Transaction();
        transaction.setClientId(clientId);
        transaction.setProductId(productId);
        transaction.setAmount(amount);
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setTransactionType("payment");
        return transactionRepository.save(transaction)
                .map(TransactionMapper::convertToDto);
    }


}

