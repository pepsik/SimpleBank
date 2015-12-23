package com.aleksandr.berezovyi.model;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
public class Client {
    @Id @GeneratedValue
    private Long id;
    @Column
    private String firstname;
    @Column
    private String lastname;
    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER)
    private List<Account> accounts;

    public Client() {
    }

    public Client(String firstname, String lastname) {
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", accounts=" + accounts +
                '}';
    }
}
