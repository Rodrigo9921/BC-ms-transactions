package com.nttdata.mstransaccions.service;


import com.nttdata.mstransaccions.dto.TransactionDto;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TransactionService {
    Mono<TransactionDto> createTransaction(TransactionDto transactionDto);
    Flux<TransactionDto> getAllTransactions();
    Flux<TransactionDto> getTransactionsByClientId(String clientId);
    Flux<TransactionDto> getTransactionsByProductId(String productId);
    Mono<TransactionDto> createPaymentTransaction(String clientId, String productId, double amount);

}
