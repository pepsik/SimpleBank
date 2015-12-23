package com.aleksandr.berezovyi.service;

import com.aleksandr.berezovyi.model.Account;
import com.aleksandr.berezovyi.model.Client;
import com.aleksandr.berezovyi.persistence.ClientDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by pepsik on 12/22/2015.
 */
@Transactional
@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientDao clientDao;

    @Autowired
    private AccountService accountService;

    @Override
    public Client createClient(Client client) {
        return clientDao.create(client);
    }

    @Override
    public List<Account> getClientBalance(Long id) {
        return clientDao.getById(id).getAccounts();
    }

    @Override
    public Client getClientByFullname(String firstname, String lastname) {
        return clientDao.getByFullname(firstname, lastname);
    }

    @Override
    public List<Client> getClientWithMaxBalance() {
        return accountService.getAccountWithMaxBalance().stream().map(Account::getOwner).collect(Collectors.toList());
    }

    @Override
    public List<Client> getClientWithMinBalance() {
        return accountService.getAccountWithMinBalance().stream().map(Account::getOwner).collect(Collectors.toList());
    }

    @Override
    public List<Client> getAllClients() {
        return clientDao.getAll();
    }
}
