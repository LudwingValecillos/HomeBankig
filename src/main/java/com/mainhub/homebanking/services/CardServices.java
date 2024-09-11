package com.mainhub.homebanking.services;

import com.mainhub.homebanking.DTO.CardDTO;
import com.mainhub.homebanking.DTO.NewCardDTO;
import com.mainhub.homebanking.models.Card;
import com.mainhub.homebanking.models.Client;
import com.mainhub.homebanking.models.type.CardColor;
import com.mainhub.homebanking.models.type.CardType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;
import java.util.List;

public interface CardServices {

    List<Card> getAllCards();

    List<CardDTO> getAllCardsDTO();

//    List<CardDTO> getAllCardsDTOByClient(Client client);

    ResponseEntity<?> createCard(Authentication authentication, NewCardDTO cardDTO);


    String validateNewCardDto(NewCardDTO card);

    Client getClient(Authentication authentication);

    String validateDetailsCard(Client client, NewCardDTO newCardDTO);

    String validateColor(Client client, CardColor color, CardType type);

    List<Card> getAllCardsCredits(Client client);

    List<Card> getAllCardsDebits(Client client);

    Card generateCard(NewCardDTO newCardDTO);

    LocalDateTime getExpirationDate(int year);

    CardColor getCardColor(String color);

    CardType getCardType(String type);


}
