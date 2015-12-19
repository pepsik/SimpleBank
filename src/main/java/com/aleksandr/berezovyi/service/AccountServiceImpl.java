package com.aleksandr.berezovyi.service;

import com.aleksandr.berezovyi.dao.AccountDao;
import com.aleksandr.berezovyi.dao.h2.h2AccountDao;
import com.aleksandr.berezovyi.model.Account;
import com.aleksandr.berezovyi.exception.InsufficientFoundsException;

import java.util.List;

/**
 * Created by pepsik on 12/19/2015.
 */
public class AccountServiceImpl implements AccountService {
    private AccountDao accountDao;

    public AccountServiceImpl() {
        this.accountDao = new h2AccountDao();
    }

    public Account createAccount(Long clientId, Integer balance) {
        Account account = new Account();
        account.setOwnerId(clientId);
        account.deposit(balance);
        accountDao.create(account);
        return account;
    }

    @Override
    public List<Account> getAllAccounts() {
        return accountDao.getAllAccounts();
    }

    @Override
    public Account getAccountById(Long id) {
        return accountDao.get(id);
    }

    @Override
    public void addMoney(Long accountId, Integer amount) {
        Account account = getAccountById(accountId);
        account.deposit(amount);
        accountDao.update(account);
    }

    @Override
    public void removeMoney(Long accountId, Integer amount) {
        Account account = getAccountById(accountId);
        if (account.getBalance() < amount)
            throw new InsufficientFoundsException("Not enough money!");
        account.withdraw(amount);
        accountDao.update(account);
    }
}
