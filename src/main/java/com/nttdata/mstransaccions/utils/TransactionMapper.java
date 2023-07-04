package com.nttdata.mstransaccions.utils;


import com.nttdata.mstransaccions.dto.TransactionDto;
import com.nttdata.mstransaccions.model.Transaction;

public class TransactionMapper {

    public static TransactionDto convertToDto(Transaction transaction) {
        return new TransactionDto(
                transaction.getId(),
                transaction.getClientId(),
                transaction.getProductId(),
                transaction.getProductType(),
                transaction.getAmount(),
                transaction.getTransactionType(),
                transaction.getTransactionDate(),
                transaction.getCommission()
        );
    }

    public static Transaction convertToEntity(TransactionDto transactionDto) {
        return new Transaction(
                transactionDto.getId(),
                transactionDto.getClientId(),
                transactionDto.getProductId(),
                transactionDto.getProductType(),
                transactionDto.getAmount(),
                transactionDto.getTransactionType(),
                transactionDto.getTransactionDate(),
                transactionDto.getCommission()
        );
    }
}


