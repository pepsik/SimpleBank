package com.aleksandr.berezovyi.dao;

import com.aleksandr.berezovyi.model.Account;

import java.sql.ResultSet;
import java.util.List;

/**
 * Created by pepsik on 12/19/2015.
 */
public interface AccountDao {
    Account create(Account account);

    Account get(Long id);

    List<Account> getAllAccounts();

    Account update(Account account);
}
