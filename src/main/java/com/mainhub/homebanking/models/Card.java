package com.mainhub.homebanking.models;

import com.mainhub.homebanking.models.type.CardColor;
import com.mainhub.homebanking.models.type.CardType;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client client;

    private String ClientHolder;

    private int number,cvv;

    private LocalDateTime fromDate, thruDate;

    @Enumerated(EnumType.STRING) //Enumerated indica que el tipo de dato es una cadena
    private CardType type;
    @Enumerated(EnumType.STRING) //Enumerated indica que el tipo de dato es una cadena
    private CardColor color;

    public Card() {
    }



    public Card(int number, int cvv, LocalDateTime fromDate, LocalDateTime thruDate, CardType type, CardColor color) {
        this.number = number;
        this.cvv = cvv;
        this.fromDate = fromDate;
        this.thruDate = thruDate;
        this.type = type;
        this.color = color;
    }

    public long getId() {
        return id;
    }

    public int getNumber() {
        return number;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getClientHolder() {
        return ClientHolder;
    }

    public void setClientHolder(String clientHolder) {
        ClientHolder = clientHolder;
    }
    public void setNumber(int number) {
        this.number = number;
    }

    public int getCvv() {
        return cvv;
    }

    public void setCvv(int cvv) {
        this.cvv = cvv;
    }

    public LocalDateTime getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDateTime fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDateTime getThruDate() {
        return thruDate;
    }

    public void setThruDate(LocalDateTime thruDate) {
        this.thruDate = thruDate;
    }

    public CardType getType() {
        return type;
    }

    public void setType(CardType type) {
        this.type = type;
    }

    public CardColor getColor() {
        return color;
    }

    public void setColor(CardColor color) {
        this.color = color;
    }
}
