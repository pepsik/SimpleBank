package com.aleksandr.berezovyi.service;

import com.aleksandr.berezovyi.model.Account;
import com.aleksandr.berezovyi.model.Client;
import com.aleksandr.berezovyi.model.Payment;

import java.util.List;

/**
 * Created by pepsik on 12/23/2015.
 */
public interface AccountService {
    Account createAccount(Client client);
    Payment savePayment(Payment payment);
    Account getAccountById(Long id);
    List<Account> getAllAccounts();
    List<Account> getAccountWithMaxBalance();
    List<Account> getAccountWithMinBalance();
    Account addMoney(Account account, Double amount);
    Account removeMoney(Account account, Double amount);
}
