package com.mainhub.homebanking.repositories;

import com.mainhub.homebanking.models.Client;
import org.springframework.data.jpa.repository.JpaRepository; // JPA Repository es la interfaz que nos permite interactuar con la base de datos que lo implementa hibernate/jpa.
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    //Jpa nos permite declarar metodos que nos permitan interactuar con la base de datos.

    //Jpa revisa todas las propiedades de Client y tiene que condicionar con el nombre del metodo
    Client findByEmail(String email);

//    List<Client> findAllbyActive(boolean active);

    //Devuelve el cliente que coincide con el email proporcionado.

    //JPA Repository es la interfaz que nos permite interactuar con la base de datos que lo implementa hibernate/jpa.

    //Una interfaz es una clase abstracta que solo define los métodos abstractos que nos proporciona la jerarquía de una clase.

}