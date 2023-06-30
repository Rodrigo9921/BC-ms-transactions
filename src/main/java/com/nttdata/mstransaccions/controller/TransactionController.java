package com.nttdata.mstransaccions.controller;

import com.nttdata.mstransaccions.dto.TransactionDto;
import com.nttdata.mstransaccions.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public Mono<ResponseEntity<TransactionDto>> createTransaction(@RequestBody TransactionDto transactionDto) {
        return transactionService.createTransaction(transactionDto)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping
    public Flux<TransactionDto> getAllTransactions() {
        return transactionService.getAllTransactions();
    }

    @GetMapping("/client/{clientId}")
    public Flux<TransactionDto> getTransactionsByClientId(@PathVariable String clientId) {
        return transactionService.getTransactionsByClientId(clientId);
    }

    @GetMapping("/product/{productId}")
    public Flux<TransactionDto> getTransactionsByProductId(@PathVariable String productId) {
        return transactionService.getTransactionsByProductId(productId);
    }

    @PostMapping("/{clientId}/activeProducts/{productId}/payment")
    public Mono<ResponseEntity<TransactionDto>> makePaymentTransaction(@PathVariable String clientId, @PathVariable String productId, @RequestBody Double amount) {
        return transactionService.createPaymentTransaction(clientId, productId, amount)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

}

