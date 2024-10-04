package com.mainhub.homebanking.DTO;

import java.time.LocalDate;

public record AddCardDTO(String number, Integer cvv, LocalDate thruDate, Double amount) {
}
