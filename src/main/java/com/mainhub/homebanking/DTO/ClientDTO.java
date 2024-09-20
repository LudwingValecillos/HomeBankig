package com.mainhub.homebanking.DTO;

import com.mainhub.homebanking.models.Card;
import com.mainhub.homebanking.models.Client;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ClientDTO {

    private Long id;

    private String firstName;

    private String lastName;
    private String email;

    private Set<AccountDTO> accounts = new HashSet<>();

    private List<ClientLoanDTO> loans = new ArrayList<>();

    private Set<CardDTO> cards = new HashSet<>();


    public ClientDTO(Client client) {
        this.id = client.getId();
        this.firstName = client.getFirstName();
        this.lastName = client.getLastName();
        this.email = client.getEmail();
        this.accounts = client.getAccounts().stream().map(AccountDTO::new).collect(Collectors.toSet());
        this.loans = client.getLoans().stream().map(ClientLoanDTO::new).collect(Collectors.toList());
        this.cards = client.getCards().stream().map(CardDTO::new).collect(Collectors.toSet());
    }


    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getEmail() {
        return email;
    }

    public String getLastName() {
        return lastName;
    }

    public Set<AccountDTO> getAccounts() {
        return accounts;
    }

    public List<ClientLoanDTO> getLoans() {
        return loans;
    }

    public Set<CardDTO> getCards() {
        return cards;
    }
}
