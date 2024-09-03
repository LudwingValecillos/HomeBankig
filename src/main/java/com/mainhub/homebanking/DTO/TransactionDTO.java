package com.mainhub.homebanking.DTO;

import com.mainhub.homebanking.models.Transaction;
import com.mainhub.homebanking.models.type.TransactionType;

import java.time.LocalDateTime;

public record TransactionDTO (TransactionType type, double amount, String description, String SourceAccount, String DestinationAccount) {



}
