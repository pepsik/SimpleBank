package com.aleksandr.berezovyi.persistence;

import com.aleksandr.berezovyi.model.Client;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by pepsik on 12/22/2015.
 */
@Repository
public class ClientDaoImpl implements ClientDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Client create(Client client) {
        em.persist(client);
        return client;
    }

    @Override
    public Client findById(Long id) {
        return null;
    }

    @Override
    public Client findByFullname(String firstname, String lastname) {
        try {
            return (Client) em.createQuery("SELECT c from Client c where firstname=:firstname and lastname=:lastname")
                    .setParameter("firstname", firstname).setParameter("lastname", lastname).getSingleResult();
        } catch (Exception e) {
            return null; //No entity found, null returned
        }
    }

    @Override
    public List<Client> findAll() {
        Query query = em.createQuery("SELECT c from Client c order by c.id asc ");
        return query.getResultList();
    }

    @Override
    public Client update(Client client) {
        return em.merge(client);
    }
}
