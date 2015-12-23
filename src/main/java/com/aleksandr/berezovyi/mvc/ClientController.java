package com.aleksandr.berezovyi.mvc;

import com.aleksandr.berezovyi.model.Client;
import com.aleksandr.berezovyi.model.Payment;
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
            return new ResponseEntity<>(HttpStatus.CONFLICT);
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

    @RequestMapping(value = "/{accountId}/deposite", method = RequestMethod.POST)
    public ResponseEntity<String> addMoney(@RequestBody Client sentClient, @PathVariable Long accountId, @RequestParam Double amount) {
        Client client = clientService.getClientByFullname(sentClient.getFirstname(), sentClient.getLastname());
        if (client == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        System.out.println(accountId);
        System.out.println(amount);
        return new ResponseEntity<>("norm", HttpStatus.OK);
    }

    public ResponseEntity<Payment> createPayment(@RequestBody Payment payment) {

        return new ResponseEntity<>(payment, HttpStatus.OK);
    }
}
