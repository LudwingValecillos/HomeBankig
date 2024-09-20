package com.mainhub.homebanking.services.implement;

import com.mainhub.homebanking.DTO.CardDTO;
import com.mainhub.homebanking.DTO.ClientDTO;
import com.mainhub.homebanking.DTO.NewCardDTO;
import com.mainhub.homebanking.models.Card;
import com.mainhub.homebanking.models.Client;
import com.mainhub.homebanking.models.type.CardColor;
import com.mainhub.homebanking.models.type.CardType;
import com.mainhub.homebanking.repositories.CardRepository;
import com.mainhub.homebanking.repositories.ClientRepository;
import com.mainhub.homebanking.services.CardServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CardServicesImple implements CardServices {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public List<Card> getAllCards() {
        return cardRepository.findAll();
    }

    @Override
    public List<CardDTO> getAllCardsDTO() {
        return getAllCards().stream()
                .map(CardDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public ResponseEntity<?> createCard(Authentication authentication, NewCardDTO newCardDTO) {

        if (validateNewCardDto(newCardDTO) != null) {
            return new ResponseEntity<>(validateNewCardDto(newCardDTO), HttpStatus.BAD_REQUEST);
        }

        if (validateDetailsCard(getClient(authentication), newCardDTO) != null) {
            return new ResponseEntity<>(validateDetailsCard(getClient(authentication), newCardDTO), HttpStatus.FORBIDDEN);
        }

        if (validateColor(getClient(authentication), getCardColor(newCardDTO.color()),getCardType(newCardDTO.type())) != null) {
            return new ResponseEntity<>(validateColor(getClient(authentication), getCardColor(newCardDTO.color()),getCardType(newCardDTO.type())), HttpStatus.FORBIDDEN);
        }

        return ResponseEntity.ok().body(saveCard(getClient(authentication), generateCard(newCardDTO)));
    }

    @Override
    public String validateNewCardDto(NewCardDTO card) {

        if (card.type().isBlank()) {
            return "The 'type' field is required.";
        }

        if (card.color().isBlank()) {
            return "The 'color' field is required.";
        }

        return null;
    }

    @Override
    public Client getClient(Authentication authentication) {
        return clientRepository.findByEmail(authentication.getName());
    }

    @Override
    public String validateDetailsCard(Client client, NewCardDTO newCardDTO) {

        if (newCardDTO.type().equalsIgnoreCase("DEBIT")) {
            if (getAllCardsDebits(client).size() >= 3) {
                return "You can't have more than 3 debit cards";
            }
        } else {
            if (getAllCardsCredits(client).size() >= 3) {
                return "You can't have more than 3 credit cards";
            }
        }
        return null;
    }

    @Override
    public String validateColor(Client client, CardColor color, CardType type) {

        if (type == CardType.DEBIT) {
            if (getAllCardsDebits(client).stream().anyMatch(card -> card.getColor() == color)) {
                return "You already have a debit card with this color";
            }
        } else {
            if (getAllCardsCredits(client).stream().anyMatch(card -> card.getColor() == color)) {
                return "You already have a credit card with this color";
            }
        }

        return null;
    }

    @Override
    public List<Card> getAllCardsCredits(Client client) {
        return client.getCards().stream()
                .filter(card -> card.getType() == CardType.CREDIT)
                .toList();
    }

    @Override
    public List<Card> getAllCardsDebits(Client client) {
        return client.getCards().stream()
                .filter(card -> card.getType() == CardType.DEBIT)
                .toList();
    }
    @Override
    public Card generateCard(NewCardDTO newCardDTO) {
        return new Card(getExpirationDate(5), getCardType(newCardDTO.type()), getCardColor(newCardDTO.color()));
    }

    public CardDTO saveCard(Client client, Card card) {
        client.addCard(card);
        cardRepository.save(card);
        return new CardDTO(card);
    }

    @Override
    public LocalDateTime getExpirationDate(int year) {
        return LocalDateTime.now().plusYears(year);
    }

    @Override
    public CardColor getCardColor(String color) {
        return CardColor.valueOf(color.toUpperCase());
    }

    @Override
    public CardType getCardType(String type) {
        return type.equalsIgnoreCase("DEBIT") ? CardType.DEBIT : CardType.CREDIT;
    }
}
