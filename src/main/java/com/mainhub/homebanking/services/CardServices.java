package com.mainhub.homebanking.services;

import com.mainhub.homebanking.DTO.CardDTO;
import com.mainhub.homebanking.DTO.NewCardDTO;
import com.mainhub.homebanking.models.Card;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface CardServices {

    List<Card> getAllCards( );

    List<CardDTO> getAllCardsDTO( );

    ResponseEntity<?> createCard(Authentication authentication, NewCardDTO cardDTO);
}
