package com.aleksandr.berezovyi.persistence;

import com.aleksandr.berezovyi.model.Client;

import java.util.List;

/**
 * Created by pepsik on 12/22/2015.
 */
public interface ClientDao {
    Client create(Client client);
    Client getById(Long id);
    Client getByFullname(String firstname, String lastname);
    List<Client> getAll();
}
