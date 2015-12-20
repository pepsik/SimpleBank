package com.aleksandr.berezovyi.service.impl;

import com.aleksandr.berezovyi.dao.ClientDao;
import com.aleksandr.berezovyi.dao.h2.h2ClientDao;
import com.aleksandr.berezovyi.model.Account;
import com.aleksandr.berezovyi.model.Client;
import com.aleksandr.berezovyi.exception.AccountNotFoundException;
import com.aleksandr.berezovyi.service.ClientService;

import java.util.*;

/**
 * Created by pepsik on 12/18/2015.
 */
public class ClientServiceImpl implements ClientService {

    private ClientDao clientDao;

    public ClientServiceImpl() {
        clientDao = new h2ClientDao();
    }

    public Client createClient(String firstname, String lastname) {
        Client client = new Client();
        client.setFirstname(firstname);
        client.setLastname(lastname);
        return clientDao.create(client);
    }

    @Override
    public Client updateClient(Client client) {
        return clientDao.update(client);
    }

    @Override
    public Client getClientById(Long id) {
        return clientDao.getById(id);
    }

    @Override
    public Client getClientByLastname(String lastname) {
        return clientDao.getByLastname(lastname);
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
        return clientDao.getById(result.getOwnerId());
    }

    @Override
    public Client getClientWithMinBalance(List<Account> accounts) {
        Account result = accounts.get(0);
        for (Account account : accounts) {
            if (account.getBalance() < result.getBalance()) {
                result = account;
            }
        }
        return clientDao.getById(result.getOwnerId());
    }

    @Override
    public Set<Client> getAllClients() {
        return clientDao.getAllClients();
    }
}
