package com.mainhub.homebanking.DTO;

public record LoanAplicationDTO(long  id, double amount, int payments, String destinationAccount) {

}
