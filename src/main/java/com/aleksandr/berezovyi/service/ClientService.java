package com.aleksandr.berezovyi.service;

import com.aleksandr.berezovyi.model.Account;
import com.aleksandr.berezovyi.model.Client;

import java.util.List;
import java.util.Set;

/**
 * Created by pepsik on 12/18/2015.
 */
public interface ClientService {
    Client createClient(Client client);

    List<Account> getClientBalance(Long id);

    Client getClientByFullname(String firstname, String lastname);

    List<Client> getClientWithMaxBalance();

    List<Client> getClientWithMinBalance();

    List<Client> getAllClients();
}
