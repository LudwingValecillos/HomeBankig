package com.mainhub.homebanking.models;

import jakarta.persistence.*;

@Entity //Le estamos idicando a spring que genere una tabla en la base de datos
public class Client {
    @Id//Indica que va a ser la clave primaria de la clase
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Indica que va a ser una clave primaria autoincremental en la base de datos (1,2,3,4,5...)
    private long id;

    private String firstName;
    private String lastName;
    private String email;


    public Client() {
    }

    public Client(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
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


}
