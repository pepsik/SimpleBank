package com.aleksandr.berezovyi.service;

import com.aleksandr.berezovyi.model.Account;
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
    public List<Account> getAccountWithMaxBalance() {
        return accountDao.getWithMaxBalance();
    }

    @Override
    public List<Account> getAccountWithMinBalance() {
        return accountDao.getWithMinBalance();
    }
}
