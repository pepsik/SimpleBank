package com.aleksandr.berezovyi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
public class Account {
    @Id @GeneratedValue
    private Long id;
    @Column
    private volatile double balance;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "ownerid")
    private Client owner;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Client getOwner() {
        return owner;
    }

    public void setOwner(Client owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", balance=" + balance +
                ", owner=" + owner.getLastname() +
                '}';
    }
}
