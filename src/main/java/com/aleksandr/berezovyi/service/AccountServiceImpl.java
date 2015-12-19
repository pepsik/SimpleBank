package com.aleksandr.berezovyi.service;

import com.aleksandr.berezovyi.model.Account;
import com.aleksandr.berezovyi.service.Exception.InsufficientFoundsException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pepsik on 12/19/2015.
 */
public class AccountServiceImpl implements AccountService {
    private Long accountIdGenerator = 1000L;
    private List<Account> accounts;

    public AccountServiceImpl() {
        this.accounts = new ArrayList<>();
    }

    public Account createAccount(Integer balance) {
        Account account = new Account();
        account.setId(accountIdGenerator++);
        account.deposit(balance);
        accounts.add(account);
        return account;
    }

    @Override
    public List<Account> getAllAccounts() {
        return accounts;
    }

    @Override
    public Account getAccountById(Long id) {
        for (Account account : accounts) {
            if (account.getId().equals(id))
                return account;
        }
        return null;
    }

    @Override
    public void addMoney(Long accountId, Integer amount) {
        Account account = getAccountById(accountId);
        account.deposit(amount);
    }

    @Override
    public void removeMoney(Long accountId, Integer amount) {
        Account account = getAccountById(accountId);
        if (account.getBalance() < amount)
            throw new InsufficientFoundsException("Not enough money!");

        account.withdraw(amount);
    }
}
