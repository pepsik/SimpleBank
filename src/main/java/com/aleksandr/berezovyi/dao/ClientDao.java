package com.aleksandr.berezovyi.dao;

import com.aleksandr.berezovyi.model.Client;

import java.util.Set;

/**
 * Created by pepsik on 12/19/2015.
 */
public interface ClientDao {
    Client create(Client account);

    Client getById(Long id);

    Client getByLastname(String lastname);

    Set<Client> getAll();

    Client update(Client client);
}
