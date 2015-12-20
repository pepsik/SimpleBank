package com.aleksandr.berezovyi.service.impl;

import com.aleksandr.berezovyi.dao.AccountDao;
import com.aleksandr.berezovyi.dao.h2.h2AccountDao;
import com.aleksandr.berezovyi.exception.InsufficientFundsException;
import com.aleksandr.berezovyi.model.Account;
import com.aleksandr.berezovyi.model.Payment;
import com.aleksandr.berezovyi.service.AccountService;

import java.util.Set;

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
    public Set<Account> getAllAccounts() {
        return accountDao.getAllAccounts();
    }

    @Override
    public Account getAccountById(Long id) {
        return accountDao.get(id);
    }

    @Override
    public void addMoney(Long accountId, Double amount) {
        Account account = getAccountById(accountId);
        account.deposit(amount);
        accountDao.update(account);
    }

    @Override
    public void removeMoney(Long accountId, Double amount) {
        Account account = getAccountById(accountId);
        if (account.getBalance() < amount) {
            throw new InsufficientFundsException("Not enough money! Trying to get " + amount + " but was " + account.getBalance() + " AccountID=" + accountId);
        }
        account.withdraw(amount);
        accountDao.update(account);
    }

    @Override
    public Payment makePayment(Payment payment) {
        return accountDao.makePayment(payment);
    }

    @Override
    public Payment savePayment(Payment payment) {
        return accountDao.savePayment(payment);
    }

    @Override
    public Set<Payment> getAllPayments() {
        return accountDao.getAllPayments();
    }
}
