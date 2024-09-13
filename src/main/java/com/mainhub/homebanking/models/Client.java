package com.mainhub.homebanking.models;

import com.mainhub.homebanking.utils.GenerateNumber;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.*;

//Anotaciones
@Entity //Le estamos idicando a spring que genere una tabla en la base de datos
public class Client {
    @Id//Indica que va a ser la clave primaria en la base de datos
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Indica que va a ser una clave primaria autoincremental en la base de datos (1,2,3,4,5...)
    private long id;
    private String firstName;
    private String lastName;
    private String email;

    private boolean active = true;

    private String password;

    @OneToMany(mappedBy="client", fetch=FetchType.EAGER)
    private Set<Account> accounts = new HashSet<>();

    @OneToMany(mappedBy="client", fetch=FetchType.EAGER)
    private List<ClientLoan> loans = new ArrayList<>();

    @OneToMany(mappedBy="client", fetch=FetchType.EAGER)
    private Set<Card> cards = new HashSet<>();

    public Client() {
    }

    public Client(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }


// Getters and setters

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<ClientLoan> getLoans() {
        return loans;
    }

    public void setLoans(List<ClientLoan> loans) {
        this.loans = loans;
    }

    public Set<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(Set<Account> accounts) {
        this.accounts = accounts;
    }


    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Set<Card> getCards() {
        return cards;
    }

    public void setCards(Set<Card> cards) {
        this.cards = cards;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void addAccount(Account account) {
        this.accounts.add(account);
        account.setClient(this);
    }

    public void addClientLoan(ClientLoan clientLoan) {
        this.loans.add(clientLoan);
        clientLoan.setClient(this);
    }



    public void addCard(Card card){
        this.cards.add(card);
        card.setFromDate(LocalDateTime.now());
        card.setNumber(new GenerateNumber().generateNumber());
        card.setClient(this);
        card.setCardHolder(this.firstName + " " + this.lastName);
        card.setCvv(new Random().nextInt((999 - 100) + 1) + 100);
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", accounts=" + accounts +
                '}';
    }
}
