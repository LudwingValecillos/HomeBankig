package com.mainhub.homebanking.DTO;

import com.mainhub.homebanking.models.Card;
import com.mainhub.homebanking.models.type.CardColor;
import com.mainhub.homebanking.models.type.CardType;

import java.time.LocalDateTime;

public class CardDTO {

    private Long id;


    private String ClientHolder;

    private int cvv;
    private String number;

    private LocalDateTime fromDate, thruDate;

    private CardType type;
    private CardColor color;

    public CardDTO(Card card) {
        this.id = card.getId();
        this.ClientHolder = card.getCardHolder();
        this.number = card.getNumber();
        this.cvv = card.getCvv();
        this.fromDate = card.getFromDate();
        this.thruDate = card.getThruDate();
        this.type = card.getType();
        this.color = card.getColor();
    }

    public Long getId() {
        return id;
    }


    public String getClientHolder() {
        return ClientHolder;
    }

    public String getNumber() {
        return number;
    }

    public int getCvv() {
        return cvv;
    }

    public LocalDateTime getFromDate() {
        return fromDate;
    }

    public LocalDateTime getThruDate() {
        return thruDate;
    }

    public CardType getType() {
        return type;
    }

    public CardColor getColor() {
        return color;
    }
}
