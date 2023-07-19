package com.nttdata.mstransaccions.controller;

import com.nttdata.mstransaccions.dto.TransactionDto;
import com.nttdata.mstransaccions.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@WebFluxTest(controllers = TransactionController.class)
class TransactionControllerTest {

    @MockBean
    private TransactionService transactionService;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        this.webTestClient = WebTestClient.bindToController(new TransactionController(transactionService)).build();
    }

    @Test
    void getAllTransactions() {
        // Arrange
        TransactionDto transactionDto = new TransactionDto(); // replace with actual transaction DTO
        given(transactionService.getAllTransactions()).willReturn(Flux.just(transactionDto));

        // Act & Assert
        webTestClient.get()
                .uri("/transactions")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(TransactionDto.class).hasSize(1).contains(transactionDto);
    }

    @Test
    void getTransactionsByClientId() {
        // Arrange
        String clientId = "clientId1";
        TransactionDto transactionDto = new TransactionDto(); // replace with actual transaction DTO
        given(transactionService.getTransactionsByClientId(clientId)).willReturn(Flux.just(transactionDto));

        // Act & Assert
        webTestClient.get()
                .uri("/transactions/client/{clientId}", clientId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(TransactionDto.class).hasSize(1).contains(transactionDto);
    }

    @Test
    void getTransactionsByProductId() {
        // Arrange
        String productId = "productId1";
        TransactionDto transactionDto = new TransactionDto(); // replace with actual transaction DTO
        given(transactionService.getTransactionsByProductId(productId)).willReturn(Flux.just(transactionDto));

        // Act & Assert
        webTestClient.get()
                .uri("/transactions/product/{productId}", productId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(TransactionDto.class).hasSize(1).contains(transactionDto);
    }
}