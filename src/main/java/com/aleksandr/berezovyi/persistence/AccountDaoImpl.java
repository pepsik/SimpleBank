package com.aleksandr.berezovyi.persistence;

import com.aleksandr.berezovyi.model.Account;
import com.aleksandr.berezovyi.model.Payment;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by pepsik on 12/23/2015.
 */
@Repository
public class AccountDaoImpl implements AccountDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Account create(Account account) {
        em.persist(account);
        return account;
    }

    @Override
    public Payment savePayment(Payment payment) {
        em.persist(payment);
        return payment;
    }

    @Override
    public Account findById(Long id) {
        return em.find(Account.class, id);
    }

    @Override
    public List<Account> findAll() {
        return em.createQuery("select a from Account a order by a.id").getResultList();
    }

    @Override
    public Account update(Account account) {
        return em.merge(account);
    }

    @Override
    public List<Account> findWithMaxBalance() {
        return em.createQuery("SELECT a from Account a where a.balance = (select max (a.balance) from Account a)").getResultList();
    }

    @Override
    public List<Account> findWithMinBalance() {
        return em.createQuery("SELECT a from Account a where a.balance = (select min (a.balance) from Account a)").getResultList();
    }
}
