package com.mainhub.homebanking.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.mainhub.homebanking.models.type.TransactionType;
import com.mainhub.homebanking.repositories.AccountRepository;
import com.mainhub.homebanking.repositories.AccountRepository;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    //private modificador de acceso (public, private, protected, etc.)
    private String number;
    private LocalDate creationDate = LocalDate.now();
    private double balance;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    //La referencia con el client se llama una referencia foreign key / clave foranea
    private Client client; // Propiedad para establecer la relación con Client

    @OneToMany(mappedBy="account", fetch=FetchType.EAGER)
    private Set<Transaction> transactions = new HashSet<>();



    public Account() {
    }

    public Account(String number, LocalDate creationDate, double balance) {
        this.number = number;
        this.creationDate = creationDate;
        this.balance = balance;
    }

    // Getters y setters

    public long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
        transaction.setAccount(this);

        if (transaction.getType() == TransactionType.DEBIT) {
            this.balance += transaction.getAmount();
        } else {
            this.balance += transaction.getAmount();
        }

    }

    public Set<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(Set<Transaction> transactions) {
        this.transactions = transactions;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", creationDate=" + creationDate +
                ", balance=" + balance +
                '}';
    }
}
