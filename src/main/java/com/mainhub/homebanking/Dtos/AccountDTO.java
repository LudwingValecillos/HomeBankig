package com.mainhub.homebanking.Dtos;

import com.mainhub.homebanking.models.Account;
import com.mainhub.homebanking.models.Client;

public class AccountDTO {

    private Long id;
    private String number;
    private double balance;



    public AccountDTO() {
    }

    public AccountDTO(Account account) {
        this.id = account.getId();
        this.number = account.getNumber();
        this.balance = account.getBalance();

    }

    public Long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public double getBalance() {
        return balance;
    }


}
