package com.mainhub.homebanking.repositories;


import com.mainhub.homebanking.models.Card;
import com.mainhub.homebanking.models.type.CardColor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<Card,Long> {
}
