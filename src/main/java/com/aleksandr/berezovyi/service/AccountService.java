package com.aleksandr.berezovyi.service;

import com.aleksandr.berezovyi.model.Account;
import com.aleksandr.berezovyi.model.Payment;

import java.util.Set;

/**
 * Created by pepsik on 12/19/2015.
 */
public interface AccountService {
    Account createAccount(Long ClientId, Integer balance);

    Set<Account> getAllAccounts();

    Account getAccountById(Long id);

    void addMoney(Long accountId, Double amount);

    void removeMoney(Long accountId, Double amount);

    Payment makePayment(Payment payment);

    Payment savePayment(Payment payment);

    Set<Payment> getAllPayments();
}
