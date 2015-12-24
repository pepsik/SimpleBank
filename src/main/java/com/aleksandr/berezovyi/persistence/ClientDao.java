package com.aleksandr.berezovyi.persistence;

import com.aleksandr.berezovyi.model.Client;

import java.util.List;

/**
 * Created by pepsik on 12/22/2015.
 */
public interface ClientDao {
    Client create(Client client);
    Client findById(Long id);
    Client findByFullname(String firstname, String lastname);
    List<Client> findAll();
    Client update(Client client);
}
