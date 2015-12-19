package com.aleksandr.berezovyi.service;

import com.aleksandr.berezovyi.model.Account;
import com.aleksandr.berezovyi.model.Client;
import com.aleksandr.berezovyi.service.Exception.AccountNotFoundException;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pepsik on 12/18/2015.
 */
public class ClientServiceImpl implements ClientService {
    public static Long clientIdGenerator = 1L;
    private Map<Long, Client> clients;

    public ClientServiceImpl() {
        this.clients = new HashMap<>();
    }

    public Client createClient(String firstname, String lastname) {
        Client client = new Client();
        client.setId(clientIdGenerator++);
        client.setFirstname(firstname);
        client.setLastname(lastname);
        clients.put(client.getId(), client);
        return client;
    }

    @Override
    public Client getClientById(Long id) {
        return clients.get(id);
    }

    @Override
    public Client getClientByName(String firstname) {
        Collection<Client> clientList = clients.values();
        for (Client client : clientList) {
            if (client.getFirstname().equals(firstname))
                return client;
        }
        return null;
    }

    @Override
    public double getClientBalance(Client client, List<Account> accounts) {
        for (Account account : accounts) {
            if (account.getId().equals(client.getAccountId())) {
                return account.getBalance();
            }
        }
        throw new AccountNotFoundException("Account does not exist for a given client " + client);
    }

    @Override
    public Client getClientWithMaxBalance(List<Account> accounts) {
        Account result = accounts.get(0);
        for (Account account : accounts) {
            if (account.getBalance() > result.getBalance()) {
                result = account;
            }
        }
        return clients.get(result.getOwnerId());
    }

    @Override
    public Map<Long, Client> getAllClients() {
        return clients;
    }
}
