package com.aleksandr.berezovyi.service;

import com.aleksandr.berezovyi.model.Account;

import java.util.List;

/**
 * Created by pepsik on 12/23/2015.
 */
public interface AccountService {
    List<Account> getAccountWithMaxBalance();
    List<Account> getAccountWithMinBalance();
}
