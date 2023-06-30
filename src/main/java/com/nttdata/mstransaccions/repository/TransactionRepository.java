package com.nttdata.mstransaccions.repository;

import com.nttdata.mstransaccions.model.Transaction;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface TransactionRepository extends ReactiveMongoRepository<Transaction,String> {
    Flux<Transaction> findByClientId(String clientId);
    Flux<Transaction> findByProductId(String productId);
}
