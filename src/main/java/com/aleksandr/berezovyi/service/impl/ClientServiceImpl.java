package com.aleksandr.berezovyi.service.impl;

import com.aleksandr.berezovyi.dao.ClientDao;
import com.aleksandr.berezovyi.dao.h2.h2ClientDao;
import com.aleksandr.berezovyi.model.Account;
import com.aleksandr.berezovyi.model.Client;
import com.aleksandr.berezovyi.exceptions.AccountNotFoundException;
import com.aleksandr.berezovyi.service.ClientService;

import java.util.*;
import java.util.stream.Collectors;

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
    public Set<Client> getClientWithMaxBalance(List<Account> accounts) {
        List<Account> resultAccounts = new ArrayList<>();
        resultAccounts.add(accounts.get(0));
        for (Account account : accounts) {
            if (account.getBalance() < resultAccounts.get(0).getBalance())
                continue;
            if (account.getBalance() > resultAccounts.get(0).getBalance())
                resultAccounts.clear();

            resultAccounts.add(account);
        }
        return resultAccounts.stream().map(account -> clientDao.getById(account.getOwnerId())).collect(Collectors.toSet());
    }

    @Override
    public Set<Client> getClientWithMinBalance(List<Account> accounts) {
        List<Account> resultAccounts = new ArrayList<>();
        resultAccounts.add(accounts.get(0));
        for (Account account : accounts) {
            if (account.getBalance() > resultAccounts.get(0).getBalance())
                continue;
            if (account.getBalance() < resultAccounts.get(0).getBalance())
                resultAccounts.clear();

            resultAccounts.add(account);
        }
        return resultAccounts.stream().map(account -> clientDao.getById(account.getOwnerId())).collect(Collectors.toSet());
    }

    @Override
    public Set<Client> getAllClients() {
        return clientDao.getAll();
    }
}
