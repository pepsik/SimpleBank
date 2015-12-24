package com.aleksandr.berezovyi.service;

import com.aleksandr.berezovyi.model.Account;
import com.aleksandr.berezovyi.model.Client;
import com.aleksandr.berezovyi.model.Payment;

import java.util.List;
import java.util.Set;

/**
 * Created by pepsik on 12/18/2015.
 */
public interface ClientService {
    Client createClient(Client client);

    Account createNewAccount(Client client);

    Payment createPayment(Payment payment);

    List<Account> getClientBalance(Long id);

    List<Account> getAllAccounts();

    Client getClientByFullname(String firstname, String lastname);

    List<Client> getClientWithMaxBalance();

    List<Client> getClientWithMinBalance();

    List<Client> getAllClients();

    Account withdraw(Client client, Long accountId, Double amount);

    Account deposit(Client client, Long accountId, Double amount);
}
