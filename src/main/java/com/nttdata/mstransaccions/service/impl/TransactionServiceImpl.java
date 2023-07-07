package com.nttdata.mstransaccions.service.impl;


import com.nttdata.mstransaccions.dto.ActiveProductDto;
import com.nttdata.mstransaccions.dto.PassiveProductDto;
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
import java.util.UUID;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final WebClient webClient;
    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository,WebClient.Builder webClientBuilder) {
        this.transactionRepository = transactionRepository;
        this.webClient=webClientBuilder.baseUrl("http://localhost:8080/clients").build();
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
    public Mono<TransactionDto> makePayment(String clientId, String productId, double amount) {
        return webClient.get()
                .uri("/{clientId}/activeProducts/{productId}", clientId, productId)
                .retrieve()
                .bodyToMono(ActiveProductDto.class)
                .flatMap(activeProduct -> {
                    if (activeProduct.getBalance() < amount) {
                        return Mono.error(new RuntimeException("Insufficient balance"));
                    }

                    Transaction transaction = new Transaction();
                    transaction.setClientId(clientId);
                    transaction.setProductId(productId);
                    transaction.setAmount(amount);
                    transaction.setTransactionDate(LocalDateTime.now());
                    transaction.setTransactionType("payment");
                    transaction.setCommission(0.0);

                    return transactionRepository.save(transaction)
                            .map(TransactionMapper::convertToDto);
                });
    }
    @Override
    public Mono<TransactionDto> transfer(String sourceClientId, String sourceProductId, String targetClientId, String targetProductId, double amount) {
        return webClient.get()
                .uri("/" + sourceClientId + "/passiveProducts/" + sourceProductId)
                .retrieve()
                .bodyToMono(PassiveProductDto.class)
                .flatMap(sourceProduct -> {
                    if (sourceProduct.getBalance() < amount) {
                        return Mono.error(new RuntimeException("Insufficient balance"));
                    }
                    return webClient.get()
                            .uri("/" + targetClientId + "/passiveProducts/" + targetProductId)
                            .retrieve()
                            .bodyToMono(PassiveProductDto.class)
                            .flatMap(targetProduct -> {
                                Transaction transaction = new Transaction();
                                transaction.setId(UUID.randomUUID().toString());
                                transaction.setClientId(sourceClientId);
                                transaction.setProductId(sourceProductId);
                                transaction.setProductType("Passive");
                                transaction.setAmount(amount);
                                transaction.setTransactionType("Transfer");
                                transaction.setTransactionDate(LocalDateTime.now());
                                transaction.setCommission(0.0); // Set the commission to 0 for now
                                return transactionRepository.save(transaction)
                                        .flatMap(savedTransaction -> {
                                            // Update the source and target products' balances
                                            sourceProduct.setBalance(sourceProduct.getBalance() - amount);
                                            targetProduct.setBalance(targetProduct.getBalance() + amount);
                                            return webClient.put()
                                                    .uri("/" + sourceClientId + "/passiveProducts/" + sourceProductId)
                                                    .bodyValue(sourceProduct)
                                                    .retrieve()
                                                    .bodyToMono(PassiveProductDto.class)
                                                    .then(webClient.put()
                                                            .uri("/" + targetClientId + "/passiveProducts/" + targetProductId)
                                                            .bodyValue(targetProduct)
                                                            .retrieve()
                                                            .bodyToMono(PassiveProductDto.class))
                                                    .thenReturn(TransactionMapper.convertToDto(savedTransaction));
                                        });
                            });
                });
    }



}

