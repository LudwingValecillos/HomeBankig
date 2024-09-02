package com.mainhub.homebanking.DTO;

import com.mainhub.homebanking.models.type.CardColor;
import com.mainhub.homebanking.models.type.CardType;

public record NewCardDTO(String color, String type) {
}
