package com.aleksandr.berezovyi.mvc;

import com.aleksandr.berezovyi.model.Account;
import com.aleksandr.berezovyi.model.Client;
import com.aleksandr.berezovyi.model.Payment;
import com.aleksandr.berezovyi.mvc.exception.AccountDoesNotExistException;
import com.aleksandr.berezovyi.mvc.exception.ClientDoesNotExistException;
import com.aleksandr.berezovyi.mvc.exception.ClientExistException;
import com.aleksandr.berezovyi.service.AccountService;
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
    public ResponseEntity<String> showClients(@RequestParam(required = false) String firstname, @RequestParam(required = false) String lastname) {
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
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<table border=\"1\" style=\"border: 1px solid black;border-collapse: collapse;\">");
        for (Client client : clients) {
            stringBuilder.append("<tr>");
            stringBuilder.append("<td style=\"padding: 15px;\">").append(client.getId()).append("</td>");
            stringBuilder.append("<td style=\"padding: 15px;\">").append(client.getFirstname()).append("</td>");
            stringBuilder.append("<td style=\"padding: 15px;\">").append(client.getLastname()).append("</td>");
            if (client.getAccounts().size() != 0)
                stringBuilder.append("<td style=\"padding: 15px;\">").append(client.getAccounts().iterator().next().getBalance()).append("</td>");
            stringBuilder.append("</tr>");
        }
        stringBuilder.append("</table>");
        return new ResponseEntity<>(stringBuilder.toString(), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Client> createNewClient(@RequestBody Client sentClient) {
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
        }
        return new ResponseEntity<>(accounts, HttpStatus.FOUND);
    }

    @RequestMapping(value = "/max", method = RequestMethod.GET)
    public ResponseEntity<List<Client>> findClientWithMaxBalance() {
        List<Client> clients = clientService.getClientWithMaxBalance();
        return new ResponseEntity<>(clients, HttpStatus.FOUND);
    }

    @RequestMapping(value = "/min", method = RequestMethod.GET)
    public ResponseEntity<List<Client>> findClientWithMinBalance() {
        List<Client> clients = clientService.getClientWithMinBalance();
        return new ResponseEntity<>(clients, HttpStatus.FOUND);
    }

    //must be https
    @RequestMapping(value = "/deposit", method = RequestMethod.POST)
    public ResponseEntity<Account> deposit(@RequestBody Client sentClient, @RequestParam Long accountId, @RequestParam Double amount) {
        Client client = clientService.getClientByFullname(sentClient.getFirstname(), sentClient.getLastname());
        if (client == null)
            throw new ClientDoesNotExistException();
        Account account = clientService.deposit(client, accountId, amount);
        if (account == null)
            throw new AccountDoesNotExistException();
        return new ResponseEntity<>(account, HttpStatus.OK);
    }

    @RequestMapping(value = "/withdraw", method = RequestMethod.POST)
    public ResponseEntity<Account> withdraw(@RequestBody Client sentClient, @RequestParam Long accountId, @RequestParam Double amount) {
        Client client = clientService.getClientByFullname(sentClient.getFirstname(), sentClient.getLastname());
        if (client == null)
            throw new ClientDoesNotExistException();
        Account account = clientService.withdraw(client, accountId, amount);
        if (account == null)
            throw new AccountDoesNotExistException();
        return new ResponseEntity<>(account, HttpStatus.OK);
    }

    @RequestMapping(value = "/payment", method = RequestMethod.POST)
    public ResponseEntity<Payment> createPayment(@RequestBody Payment sentPayment) {
        System.out.println(sentPayment);
        if (sentPayment.getRecipientAccountId() == null || sentPayment.getSenderAccountId() == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Payment payment = clientService.createPayment(sentPayment);
        return new ResponseEntity<>(payment, HttpStatus.OK);
    }
}
