package com.aleksandr.berezovyi.persistence;

import com.aleksandr.berezovyi.model.Account;
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
    public List<Account> getWithMaxBalance() {
        return em.createQuery("SELECT a from Account a where a.balance = (select max (a.balance) from Account a)").getResultList();
    }

    @Override
    public List<Account> getWithMinBalance() {
        return em.createQuery("SELECT a from Account a where a.balance = (select min (a.balance) from Account a)").getResultList();
    }
}
