package com.mainhub.homebanking.DTO;

public record NewTransactionDTO(String type, double amount, String description, String sourceAccount, String DestinationAccount) {



}
