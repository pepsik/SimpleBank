package com.aleksandr.berezovyi.service;

import com.aleksandr.berezovyi.model.Account;

import java.util.List;

/**
 * Created by pepsik on 12/19/2015.
 */
public interface AccountService {
    Account createAccount(Long ClientId, Integer balance);

    List<Account> getAllAccounts();

    Account getAccountById(Long id);

    void addMoney(Long accountId, Integer amount);

    void removeMoney(Long accountId, Integer amount);
}
