package com.nttdata.mstransaccions.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferDto {
    private String sourceClientId;
    private String sourceProductId;
    private String targetClientId;
    private String targetProductId;
    private Double amount;
}
