package com.nttdata.mstransaccions.utils;

import com.nttdata.mstransaccions.dto.TransferDto;
import com.nttdata.mstransaccions.model.Transaction;

public class TransferMapper {
    public static Transaction convertToEntity(TransferDto transferDto) {
        Transaction transaction = new Transaction();
        transaction.setClientId(transferDto.getSourceClientId());
        transaction.setProductId(transferDto.getSourceProductId());
        transaction.setAmount(transferDto.getAmount());
        // set other fields as necessary
        return transaction;
    }

    public static TransferDto convertToDto(Transaction transaction) {
        TransferDto transferDto = new TransferDto();
        transferDto.setSourceClientId(transaction.getClientId());
        transferDto.setSourceProductId(transaction.getProductId());
        transferDto.setAmount(transaction.getAmount());
        // set other fields as necessary
        return transferDto;
    }
}
