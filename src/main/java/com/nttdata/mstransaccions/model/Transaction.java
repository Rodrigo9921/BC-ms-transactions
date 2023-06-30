package com.nttdata.mstransaccions.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "transactions")
public class Transaction {
    @Id
    private String id;
    private String clientId;
    private String productId;
    private String productType; // This can be "Passive" or "Active"
    private Double amount;
    private String transactionType; // This can be "Deposit", "Withdrawal", "Payment", "Charge"
    private LocalDateTime transactionDate;
}
