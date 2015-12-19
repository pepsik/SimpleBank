package com.aleksandr.berezovyi;

import com.aleksandr.berezovyi.model.Account;
import com.aleksandr.berezovyi.model.Client;
import com.aleksandr.berezovyi.model.Payment;
import com.aleksandr.berezovyi.service.AccountService;
import com.aleksandr.berezovyi.service.AccountServiceImpl;
import com.aleksandr.berezovyi.service.ClientService;
import com.aleksandr.berezovyi.service.ClientServiceImpl;
import com.aleksandr.berezovyi.service.Exception.ClientNotFoundException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by pepsik on 12/18/2015.
 */
public class BankMain {
    private ClientService clientService;
    private AccountService accountService;
    private List<Payment> payments;

    public BankMain() {
        this.clientService = new ClientServiceImpl();
        this.accountService = new AccountServiceImpl();
        this.payments = new ArrayList<>();
    }

    public void newClient(String firstname, String lastname, Integer balance) {
        Client client = clientService.createClient(firstname, lastname);
        Account account = accountService.createAccount(balance);
        account.setOwnerId(client.getId());
        client.setAccountId(account.getId());
    }

    public void sendMoney(String senderName, String recipientName, int amount) {
        Payment payment = createPayment(senderName, recipientName, amount);
        payment = makePayment(payment);
        payments.add(payment);
    }

    private Payment createPayment(String senderName, String recipientName, int amount) {
        Client sender = clientService.getClientByName(senderName);
        if (sender == null) {
            throw new ClientNotFoundException("Sender with name " + "'" + senderName + "'" + " doesn't exist!");
        }
        Client recipient = clientService.getClientByName(recipientName);
        if (recipient == null) {
            throw new ClientNotFoundException("Recipient with name '" + recipientName + "' doesn't exist!");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be > 1");
        }
        Payment payment = new Payment();
        payment.setSenderAccountId(sender.getAccountId());
        payment.setRecipientAccountId(recipient.getAccountId());
        payment.setAmount(amount);
        return payment;
    }

    private Payment makePayment(Payment payment) {
        accountService.removeMoney(payment.getSenderAccountId(), payment.getAmount());
        accountService.addMoney(payment.getRecipientAccountId(), payment.getAmount());
        payment.setWhen(LocalDateTime.now());
        return payment;
    }

    public void printAllClients() {
        List<Account> accounts = accountService.getAllAccounts();
        Map<Long, Client> clients = clientService.getAllClients();
        System.out.println("----------------CLIENTS------------------------");
        for (Account account : accounts) {
            System.out.print("balance=" + account.getBalance() + " ");
            System.out.println(clients.get(account.getOwnerId()));
        }
        System.out.println("-----------------------------------------------");
    }

    public void showClientBalance(long clientId) {
        Client client = clientService.getClientById(clientId);
        List<Account> accounts = accountService.getAllAccounts();
        Double clientBalance = clientService.getClientBalance(client, accounts);
        System.out.print("Client balance with id=" + clientId + " is ");
        System.out.println(clientBalance);
    }

    public void showClientWithMaxBalance() {
        List<Account> accounts = accountService.getAllAccounts();
        Client result = clientService.getClientWithMaxBalance(accounts);
        System.out.print(result.getFirstname() + " " + result.getLastname());
        System.out.println(" has the highest balance");
    }

    public void showPaymentHistory(){
        System.out.println("----------------PAYMENT HISTORY----------------");
        System.out.println(payments);
        System.out.println("-----------------------------------------------");
    }

    public static void main(String[] args) {
        BankMain bank = new BankMain();
        bank.newClient("Chris", "Redfield", 99);
        bank.newClient("Ada", "Wong", 299);
        bank.newClient("Albert", "Wesker", 5);
        //add new clients...

        bank.printAllClients();
        bank.showClientBalance(1L);
        bank.showClientWithMaxBalance();
        bank.sendMoney("Ada", "Chris", 20);
        bank.sendMoney("Albert", "Albert", 5);
        bank.printAllClients();
        bank.showPaymentHistory();
    }
}
