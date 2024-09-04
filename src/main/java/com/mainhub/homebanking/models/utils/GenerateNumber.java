package com.mainhub.homebanking.models.utils;

import com.mainhub.homebanking.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class GenerateNumber {

    @Autowired
    AccountRepository accountRepository;

    public String generateNumber() {
        Random random = new Random();
        String number = "";
        for (int i = 0; i < 4; i++) {
            number += random.nextInt((9999 - 1000) + 1) + 1000;
            if (i < 3) {
                number += "-";
            }
        }
        return number;
    }

    public String generateRandomNumber() {
        Random random = new Random();
        // Genera un número aleatorio entre 1000 y 9999
        return Integer.toString(random.nextInt((999 - 100) + 1) + 100);
    }

    @Bean
    public String generateAccountNumber() {
        String number = generateRandomNumber();

        // Verifica si el número ya existe, si es así, genera uno nuevo
        while (accountRepository.existsByNumber(prefijo() + number)) {
            number = generateRandomNumber();
        }

        return prefijo() + number;
    }


    public String prefijo() {
        return "VIN";
    }

}




