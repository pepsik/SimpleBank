package com.aleksandr.berezovyi.service;

import com.aleksandr.berezovyi.model.Account;
import com.aleksandr.berezovyi.model.Client;
import com.aleksandr.berezovyi.model.Payment;
import com.aleksandr.berezovyi.mvc.exception.InsufficientFundsException;
import com.aleksandr.berezovyi.persistence.AccountDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by pepsik on 12/23/2015.
 */
@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountDao accountDao;

    @Override
    public Account createAccount(Client client) {
        Account account = new Account();
        account.setOwner(client);
        return accountDao.create(account);
    }

    @Override
    public Payment savePayment(Payment payment) {
        return accountDao.savePayment(payment);
    }

    @Override
    public Account getAccountById(Long id) {
        return accountDao.findById(id);
    }

    @Override
    public List<Account> getAllAccounts() {
        return accountDao.findAll();
    }

    @Override
    public List<Account> getAccountWithMaxBalance() {
        return accountDao.findWithMaxBalance();
    }

    @Override
    public List<Account> getAccountWithMinBalance() {
        return accountDao.findWithMinBalance();
    }

    @Override
    public Account addMoney(Account account, Double amount) {
        account.addMoney(amount);
        return accountDao.update(account);
    }

    @Override
    public Account removeMoney(Account account, Double amount) {
        if (account.getBalance() < amount)
            throw new InsufficientFundsException();
        account.removeMoney(amount);
        return accountDao.update(account);
    }
}
