package com.aleksandr.berezovyi.persistence;

import com.aleksandr.berezovyi.model.Account;
import com.aleksandr.berezovyi.model.Payment;

import java.util.List;

/**
 * Created by pepsik on 12/23/2015.
 */
public interface AccountDao {
    Account create(Account account);
    Payment savePayment(Payment payment);
    Account findById(Long id);
    List<Account> findAll();
    Account update(Account account);
    List<Account> findWithMaxBalance();
    List<Account> findWithMinBalance();
}
