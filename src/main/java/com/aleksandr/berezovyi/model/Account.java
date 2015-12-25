package com.aleksandr.berezovyi.model;

import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;

@Entity
//@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@owner")
public class Account {
    @Id @GeneratedValue
    private Long id;
    private volatile double balance;
    @ManyToOne
    @JoinColumn(name = "ownerid")
    @JsonBackReference
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

    public void addMoney(double balance) {
        this.balance += balance;
    }

    public void removeMoney(double balance) {
        this.balance -= balance;
    }

    public Client getOwner() {
        return owner;
    }

    public void setOwner(Client owner) {
        this.owner = owner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        return id.equals(account.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
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
