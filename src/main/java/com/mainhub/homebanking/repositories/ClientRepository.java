package com.mainhub.homebanking.repositories;

import com.mainhub.homebanking.models.Client;
import org.springframework.data.jpa.repository.JpaRepository; // JPA Repository es la interfaz que nos permite interactuar con la base de datos que lo implementa hibernate/jpa.
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> { // Long es el tipo de dato de la clave primaria, Alumno es el tipo de dato de la clase.

    //JPA Repository es la interfaz que nos permite interactuar con la base de datos que lo implementa hibernate/jpa.

    //Una interfaz es una clase abstracta que solo define los métodos abstractos que nos proporciona la jerarquía de una clase.
}