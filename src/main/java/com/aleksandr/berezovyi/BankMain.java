package com.aleksandr.berezovyi;

import com.aleksandr.berezovyi.model.Account;
import com.aleksandr.berezovyi.model.Client;
import com.aleksandr.berezovyi.model.Payment;
import com.aleksandr.berezovyi.service.AccountService;
import com.aleksandr.berezovyi.service.AccountServiceImpl;
import com.aleksandr.berezovyi.service.ClientService;
import com.aleksandr.berezovyi.service.ClientServiceImpl;
import com.aleksandr.berezovyi.exception.ClientNotFoundException;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by pepsik on 12/18/2015.
 */
public class BankMain {

    private static final String DB_DRIVER = "org.h2.Driver";
    private static final String DB_URL = "jdbc:h2:~/bank;" +
            "INIT=RUNSCRIPT FROM 'classpath:db/create.sql'\\;" +
            "RUNSCRIPT FROM 'classpath:db/populate.sql'";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";

    //Create and populate DB
    static {
        try {
            Class.forName(DB_DRIVER);
            System.out.println("Connecting to database...");
            DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private ClientService clientService;
    private AccountService accountService;
    private List<Payment> payments;

    public BankMain() {
        this.clientService = new ClientServiceImpl();
        this.accountService = new AccountServiceImpl();
        this.payments = new ArrayList<>();
    }

    public void newClient(String firstname, String lastname, Integer balance) {
        System.out.print("Add new client...");
        Client client = clientService.createClient(firstname, lastname);
        System.out.println(" and his account...");
        Account account = accountService.createAccount(client.getId(), balance);
        account.setOwnerId(client.getId());
        client.setAccountId(account.getId());
        clientService.updateClient(client);
    }

    public void sendMoney(String senderName, String recipientName, int amount) {
        Payment payment = createPayment(senderName, recipientName, amount);
        System.out.println("Account " + payment.getSenderAccountId() + " send " + payment.getAmount() + " to " + payment.getRecipientAccountId());
        accountService.removeMoney(payment.getSenderAccountId(), payment.getAmount()); //TODO: add/remove must be in transaction
        accountService.addMoney(payment.getRecipientAccountId(), payment.getAmount());
        payment.setWhen(LocalDateTime.now());
        payments.add(payment); //TODO: add new service and db interactions
    }

    private Payment createPayment(String senderName, String recipientName, int amount) {
        Client sender = clientService.getClientByLastname(senderName);
        if (sender == null) {
            throw new ClientNotFoundException("Sender with lastname " + "'" + senderName + "'" + " doesn't exist!");
        }
        Client recipient = clientService.getClientByLastname(recipientName);
        if (recipient == null) {
            throw new ClientNotFoundException("Recipient with lastname '" + recipientName + "' doesn't exist!");
        }
        if (sender.equals(recipient)) {
            throw new IllegalArgumentException("Can't send money to yourself");
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

    public void showAllClientsWithAccounts() {
        List<Account> accounts = accountService.getAllAccounts();
        Set<Client> clients = clientService.getAllClients();
        System.out.println("-------------CLIENTS-AND-BALANCES-------------");
        for (Client client : clients) {
            for (Account account : accounts) {
                if (client.getId().equals(account.getOwnerId())) {
                    System.out.print("AccoundID: " + account.getId());
                    System.out.print(", balance: " + account.getBalance());
                    System.out.print(", ownerID: " + account.getOwnerId());
                    System.out.print(", firstname: " + client.getFirstname());
                    System.out.print(", lastname: " + client.getLastname());
                    System.out.println();
                }
            }
        }
        System.out.println("-----------------------------------------------");
    }

    public void showClientBalance(Long clientId) {
        Client client = clientService.getClientById(clientId);
        if (client == null) {
            System.err.println("Client not found with id - " + clientId);
            return;
        }
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

    public void showClientWithMinBalance(){
        List<Account> accounts = accountService.getAllAccounts();
        Client result = clientService.getClientWithMinBalance(accounts);
        System.out.print(result.getFirstname() + " " + result.getLastname());
        System.out.println(" has the lowest balance");
    }

    public void showPaymentHistory() {
        System.out.println("----------------PAYMENT HISTORY----------------");
        payments.forEach(System.out::println);
        System.out.println("-----------------------------------------------");
    }

    public void showAllClients() {
        System.out.println("-----------------------CLIENTS-----------------");
        Set<Client> clients = clientService.getAllClients();
        clients.forEach(System.out::println);
        System.out.println("----------------------------------------------");
    }

    public static void main(String[] args) {
        BankMain bank = new BankMain();

        bank.newClient("Chris", "Redfield", 99);
        bank.newClient("Ada", "Wong", 299);
        bank.newClient("Albert", "Wesker", 5);
        bank.newClient("Akwa", "Gler", 5123);
        //add new clients...

        bank.showAllClientsWithAccounts();
        bank.showClientBalance(1L);
        bank.showClientWithMaxBalance();
        bank.sendMoney("Wong", "Redfield", 20);
        bank.sendMoney("Valentine", "Wesker", 5);
        bank.sendMoney("Gler", "Gionne", 2600);
        bank.showAllClientsWithAccounts();
        bank.showClientWithMaxBalance();
        bank.showClientWithMinBalance();
        bank.showPaymentHistory();
    }
}
