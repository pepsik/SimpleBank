package com.aleksandr.berezovyi.dao;

import com.aleksandr.berezovyi.model.Account;
import com.aleksandr.berezovyi.model.Payment;

import java.util.Set;

/**
 * Created by pepsik on 12/19/2015.
 */
public interface AccountDao {
    Account create(Account account);

    Account get(Long id);

    Set<Account> getAllAccounts();

    Account update(Account account);

    Payment makePayment(Payment payment);

    Payment savePayment(Payment payment);

    Set<Payment> getAllPayments();
}
