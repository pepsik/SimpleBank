package com.aleksandr.berezovyi.service;

import com.aleksandr.berezovyi.model.Account;
import com.aleksandr.berezovyi.model.Client;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by pepsik on 12/18/2015.
 */
public interface ClientService {
    Client createClient(String firstname, String lastname);

    Client updateClient(Client client);

    Client getClientById(Long id);

    Client getClientByLastname(String lastname);

    double getClientBalance(Client client, List<Account> accounts);

    Client getClientWithMaxBalance(List<Account> accounts);

    Client getClientWithMinBalance(List<Account> accounts);

    Set<Client> getAllClients();
}
