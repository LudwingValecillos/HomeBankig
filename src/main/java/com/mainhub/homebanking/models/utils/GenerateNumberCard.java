package com.mainhub.homebanking.models.utils;

import com.mainhub.homebanking.models.Client;
import com.mainhub.homebanking.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Random;

public class GenerateNumberCard {

    @Autowired
    private AccountRepository accountRepository;

    public String generateNumber() {
        Random random = new Random();
        String number = "";
        for (int i = 0; i < 4; i++) {
            number += random.nextInt((9999 - 1000) + 1) + 1000;
            if(i < 3){
                number += "-";
            }
        }
        return number;
    }

    public String accountNumber() {

        Random random = new Random();

        int num = random.nextInt((9999 - 1000) + 1) + 1000;

        while (accountRepository.findByNumber("VIN-" + num) != null) {
            num = random.nextInt((9999 - 1000) + 1) + 1000;
        }
        return "VIN-" + num;
    }
}
