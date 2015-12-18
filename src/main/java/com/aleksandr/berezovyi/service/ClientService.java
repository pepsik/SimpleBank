package com.aleksandr.berezovyi.service;

import com.aleksandr.berezovyi.model.Account;
import com.aleksandr.berezovyi.model.Client;

import java.util.List;

/**
 * Created by pepsik on 12/18/2015.
 */
public interface ClientService {
    double getClientBalance(Client client, List<Account> accounts);

    Client getClientWithMaxBalance(List<Account> accounts);
}
