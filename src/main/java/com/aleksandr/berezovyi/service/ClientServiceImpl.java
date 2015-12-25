package com.aleksandr.berezovyi.service;

import com.aleksandr.berezovyi.model.Account;
import com.aleksandr.berezovyi.model.Client;
import com.aleksandr.berezovyi.model.Payment;
import com.aleksandr.berezovyi.mvc.exception.AccountDoesNotExistException;
import com.aleksandr.berezovyi.persistence.ClientDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by pepsik on 12/22/2015.
 */
@Transactional
@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientDao clientDao;

    @Autowired
    private AccountService accountService;

    @Override
    public Client createClient(Client client) {
        return clientDao.create(client);
    }

    @Override
    public Account createNewAccount(Client client) {
        Account account = accountService.createAccount(client);
        client.getAccounts().add(account);
        clientDao.update(client);
        return account;
    }

    @Override
    public Payment createPayment(Payment payment) {
        payment.setWhen(LocalDateTime.now());
        Account sender = accountService.getAccountById(payment.getSenderAccountId());
        Account recipient = accountService.getAccountById(payment.getRecipientAccountId());
        if (sender == null || recipient == null)
            throw new AccountDoesNotExistException();
        Double amount = payment.getAmount();
        accountService.removeMoney(sender, amount);
        accountService.addMoney(recipient, amount);
        return accountService.savePayment(payment);
    }

    @Override
    public Client getClientByAccountId(Long id) {
        if (accountService.getAccountById(id) == null)
            return null;
        else
            return accountService.getAccountById(id).getOwner();
    }

    @Override
    public List<Account> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @Override
    public Client getClientByFullname(String firstname, String lastname) {
        return clientDao.findByFullname(firstname, lastname);
    }

    @Override
    public List<Client> getClientWithMaxBalance(List<Client> clients) {
        Double max = 0.0;
        List<Client> result = new ArrayList<>();
        for (Client client : clients) {
            Double clientBalance = 0.0;
            for (Account account : client.getAccounts()) {
                clientBalance += account.getBalance();
            }
            int compare = Double.compare(clientBalance, max);
            if (compare == 0) {
                result.add(client);
                continue;
            }
            if (compare > 0) {
                result.clear();
                result.add(client);
                max = clientBalance;
            }
        }
        return result;
    }

    @Override
    public List<Client> getClientWithMinBalance(List<Client> clients) {
        Double min = 2_000_000.0; //crutch
        List<Client> result = new ArrayList<>();
        for (Client client : clients) {
            if (client.getAccounts().isEmpty())
                continue;
            Double clientBalance = 0.0;
            for (Account account : client.getAccounts()) {
                clientBalance += account.getBalance();
            }
            int compare = Double.compare(clientBalance, min);
            if (compare == 0) {
                result.add(client);
                continue;
            }
            if (compare < 0) {
                result.clear();
                result.add(client);
                min = clientBalance;
            }
        }
        return result;
    }

    @Override
    public List<Client> getAllClients() {
        return clientDao.findAll();
    }

    @Override
    public Account withdraw(Client client, Long accountId, Double amount) {
        Account account = accountService.getAccountById(accountId);
        amount = Math.abs(amount);
        if (account == null)
            return null; //account not found
        if (!client.getAccounts().contains(account))
            return null; //account exists but have different owner
        return accountService.removeMoney(account, amount);
    }

    @Override
    public Account deposit(Client client, Long accountId, Double amount) {
        Account account = accountService.getAccountById(accountId);
        amount = Math.abs(amount);
        if (account == null)
            return null; //account not found
        if (!client.getAccounts().contains(account))
            return null; //account exists but have different owner
        return accountService.addMoney(account, amount);
    }
}
