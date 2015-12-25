package com.aleksandr.berezovyi.mvc;

import com.aleksandr.berezovyi.model.Account;
import com.aleksandr.berezovyi.model.Client;
import com.aleksandr.berezovyi.model.Payment;
import com.aleksandr.berezovyi.mvc.exception.*;
import com.aleksandr.berezovyi.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by pepsik on 12/23/2015.
 */
@Controller
@RequestMapping("/client")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Client>> showClients(@RequestParam(required = false) String firstname, @RequestParam(required = false) String lastname) {
        List<Client> clients;
        if (lastname != null && firstname != null) {
            Client client = clientService.getClientByFullname(firstname, lastname);
            if (client != null)
                clients = new ArrayList<>(Collections.singletonList(client));
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            clients = clientService.getAllClients();
        }
        return new ResponseEntity<>(clients, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Client> createNewClient(@RequestBody Client sentClient) {
        if (sentClient.getFirstname().equals("") || sentClient.getLastname().equals(""))
            throw new WrongClientNameException();
        Client client = clientService.getClientByFullname(sentClient.getFirstname(), sentClient.getLastname());
        if (client == null) {
            client = clientService.createClient(sentClient);
            return new ResponseEntity<>(client, HttpStatus.CREATED);
        } else
            throw new ClientExistException();
    }

    @RequestMapping(value = "/account", method = RequestMethod.POST)
    public ResponseEntity<Account> createNewAccount(@RequestBody Client sentClient) {
        Client client = clientService.getClientByFullname(sentClient.getFirstname(), sentClient.getLastname());
        if (client == null)
            throw new ClientDoesNotExistException();
        Account account = clientService.createNewAccount(client);
        return new ResponseEntity<>(account, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/accounts", method = RequestMethod.GET)
    public ResponseEntity<List<Account>> findAccounts(@RequestParam(required = false) String firstname, @RequestParam(required = false) String lastname) {
        List<Account> accounts;
        if (lastname != null && firstname != null) {
            Client client = clientService.getClientByFullname(firstname, lastname);
            if (client == null)
                throw new ClientDoesNotExistException();
            accounts = client.getAccounts();
        } else {
            accounts = clientService.getAllAccounts();
            for (Account account : accounts)
                account.getOwner().setAccounts(null);
        }
        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }

    @RequestMapping(value = "/max", method = RequestMethod.GET)
    public ResponseEntity<List<Client>> findClientWithMaxBalance() {
        List<Client> clients = clientService.getAllClients();
        List<Client> result = clientService.getClientWithMaxBalance(clients);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/min", method = RequestMethod.GET)
    public ResponseEntity<List<Client>> findClientWithMinBalance() {
        List<Client> clients = clientService.getAllClients();
        List<Client> result = clientService.getClientWithMinBalance(clients);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    //must be https
    @RequestMapping(value = "/deposit", method = RequestMethod.GET)
    public ResponseEntity<Account> deposit(@RequestParam Long accountId, @RequestParam Double amount) {
        if (amount <= 0)
            throw new WrongAmountException();
        Client client = clientService.getClientByAccountId(accountId);
        if (client == null)
            throw new AccountDoesNotExistException();
        Account account = clientService.deposit(client, accountId, amount);
        return new ResponseEntity<>(account, HttpStatus.OK);
    }

    @RequestMapping(value = "/withdraw", method = RequestMethod.GET)
    public ResponseEntity<Account> withdraw(@RequestParam Long accountId, @RequestParam Double amount) {
        if (amount <= 0)
            throw new WrongAmountException();
        Client client = clientService.getClientByAccountId(accountId);
        if (client == null)
            throw new AccountDoesNotExistException();
        Account account = clientService.withdraw(client, accountId, amount);
        return new ResponseEntity<>(account, HttpStatus.OK);
    }

    @RequestMapping(value = "/payment", method = RequestMethod.POST)
    public ResponseEntity<Payment> createPayment(@RequestBody Payment sentPayment) {
        if (sentPayment.getAmount() <= 0)
            throw new WrongAmountException();
        if (sentPayment.getRecipientAccountId() == null || sentPayment.getSenderAccountId() == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Payment payment = clientService.createPayment(sentPayment);
        return new ResponseEntity<>(payment, HttpStatus.OK);
    }
}
