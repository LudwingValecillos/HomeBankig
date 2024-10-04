package com.mainhub.homebanking.services.implement;

import com.mainhub.homebanking.DTO.AddCardDTO;
import com.mainhub.homebanking.DTO.CardDTO;
import com.mainhub.homebanking.DTO.ClientDTO;
import com.mainhub.homebanking.DTO.NewCardDTO;
import com.mainhub.homebanking.models.Account;
import com.mainhub.homebanking.models.Card;
import com.mainhub.homebanking.models.Client;
import com.mainhub.homebanking.models.Transaction;
import com.mainhub.homebanking.models.type.CardColor;
import com.mainhub.homebanking.models.type.CardType;
import com.mainhub.homebanking.models.type.TransactionType;
import com.mainhub.homebanking.repositories.AccountRepository;
import com.mainhub.homebanking.repositories.CardRepository;
import com.mainhub.homebanking.repositories.ClientRepository;
import com.mainhub.homebanking.repositories.TransactionRepository;
import com.mainhub.homebanking.services.CardServices;
import jakarta.transaction.Transactional;
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

     @Autowired
    private AccountRepository accountRepository;
    @Autowired
    public TransactionRepository transactionRepository;
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

    @Transactional
    @Override
    public ResponseEntity<?> apply(AddCardDTO addCardDTO) {
        System.out.println("GOLAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");

        Client client = clientRepository.findByCardsNumber(addCardDTO.number())
                .orElseThrow(() -> new RuntimeException("Client not found for card number: " + addCardDTO.number()));


        Card card = client.getCards().stream().filter(card1 -> card1.getNumber().equals(addCardDTO.number())).findFirst().orElse(null);

        if(validatePayment(addCardDTO,client,card) != null) {
            return new ResponseEntity<>(validatePayment(addCardDTO,client,card), HttpStatus.BAD_REQUEST);
        }

        Account account = client.getAccounts().stream()
                .filter(account1 -> account1.getBalance() >= addCardDTO.amount())
                .findFirst()
                .orElse(null);

        Transaction transaction = new Transaction(TransactionType.DEBIT, -addCardDTO.amount(), "Debito WaveCompany");
        account.addTransaction(transaction);
        transactionRepository.save(transaction);
        accountRepository.save(account);

        return ResponseEntity.ok().body("Payment successful");

    }

    public String validatePayment(AddCardDTO addCardDTO, Client client,Card card) {


      if (addCardDTO.number().isBlank()) {
          return "The 'number' field is required.";
      }

      if (addCardDTO.cvv() == null) {
          return "The 'cvv' field is required.";
      }

      if (addCardDTO.thruDate() == null) {
          return "The 'thruDate' field is required.";
      }


      if (client == null) {
          return "The card number is not registered.";
      }

      if(!client.getAccounts().stream().anyMatch(account -> account.getBalance() >= addCardDTO.amount())) {
          return "You don't have enough money in your account.";
      }
      if (card == null) {
          return "The card number is not registered.";
      }

//      if (card.getThruDate().isBefore(LocalDateTime.now())) {
//          return "The card has expired.";
//      }

      if (card.getCvv() != addCardDTO.cvv()) {
          return "The cvv is incorrect.";
      }


        return null;
    }
}
