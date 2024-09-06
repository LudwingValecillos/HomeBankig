package com.mainhub.homebanking.services.implement;

import com.mainhub.homebanking.DTO.CardDTO;
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
    CardRepository cardRepository;

    @Autowired
    ClientRepository clientRepository;



    @Override
    public List<Card> getAllCards() {
        return cardRepository.findAll();
    }


    @Override
    public List<CardDTO> getAllCardsDTO() {
        return getAllCards().stream().map(card -> new CardDTO(card)).collect(Collectors.toList());
    }

    @Override
    public ResponseEntity<?> createCard(Authentication authentication, NewCardDTO newCardDTO) {

        if(validateNewCarDto(newCardDTO) != null) {
            return validateNewCarDto(newCardDTO);
        }

        Client client = clientRepository.findByEmail(authentication.getName());

        CardColor color;
        try {
            color = CardColor.valueOf(newCardDTO.color().toUpperCase());
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Invalid color", HttpStatus.BAD_REQUEST);
        }
        if(validateDetailsCard(client, newCardDTO, color) != null) {
            return validateDetailsCard(client, newCardDTO, color);
        }

        return generateCard(client, color, newCardDTO);
    }

    private ResponseEntity<?> validateNewCarDto(NewCardDTO card) {
        if(card.type().isBlank() || card.color().isBlank() ) {
            return new ResponseEntity<>("All fields are required", HttpStatus.BAD_REQUEST);
        }
        return null;
    }

    private ResponseEntity<?> validateDetailsCard(Client client, NewCardDTO newCardDTO, CardColor color) {
        List<Card> cardsDebit = client.getCards().stream().filter(card -> card.getType() == CardType.DEBIT).toList();

        List<Card> cardsCredit = client.getCards().stream().filter(card -> card.getType() == CardType.CREDIT).toList();


        if(newCardDTO.type().equalsIgnoreCase("DEBIT")){
            if(cardsDebit.size() >= 3 ) {
                return new ResponseEntity<>("You can't have more than 3 debit cards", HttpStatus.FORBIDDEN);
            }
        }else{
            if(cardsCredit.size() >= 3){
                return new ResponseEntity<>("You can't have more than 3 credit cards", HttpStatus.FORBIDDEN);
            }
        }

        if (cardsDebit.stream().anyMatch(card -> card.getColor() == color) ||
                cardsCredit.stream().anyMatch(card -> card.getColor() == color)) {
            return new ResponseEntity<>("You already have a card with this color", HttpStatus.BAD_REQUEST);
        }

        return null;
    }

    private ResponseEntity<?> generateCard(Client client, CardColor color, NewCardDTO newCardDTO) {

        CardType type = newCardDTO.type().equalsIgnoreCase("DEBIT") ? CardType.DEBIT : CardType.CREDIT;

        Card card = new Card(LocalDateTime.now(), LocalDateTime.now().plusYears(5), type, color);

        client.addCard(card);

        cardRepository.save(card);
        return new ResponseEntity<>("Card created", HttpStatus.CREATED);
    }
}
