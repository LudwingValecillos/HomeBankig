package com.mainhub.homebanking.models.utils;

import com.mainhub.homebanking.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class GenerateNumberCard {

    @Autowired
    AccountRepository accountRepository;

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

    public String generateAccountNumber() {
        String prefix = "VIN";
        int maxAccountNumber = accountRepository.findMaxAccountNumberByPrefix(prefix);

        // Generar el siguiente número en la secuencia
        int nextAccountNumber = maxAccountNumber + 1;
        String formattedNumber = String.format("%03d", nextAccountNumber); // Formato de 3 dígitos con ceros a la izquierda

        return prefix + formattedNumber;
    }

}
