package com.aleksandr.berezovyi.persistence;

import com.aleksandr.berezovyi.model.Account;

import java.util.List;

/**
 * Created by pepsik on 12/23/2015.
 */
public interface AccountDao {
    List<Account> getWithMaxBalance();
    List<Account> getWithMinBalance();
}
