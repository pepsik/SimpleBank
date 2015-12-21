package com.aleksandr.berezovyi;

import com.aleksandr.berezovyi.exceptions.InsufficientFundsException;
import com.aleksandr.berezovyi.model.Account;
import com.aleksandr.berezovyi.model.Client;
import com.aleksandr.berezovyi.model.Payment;
import com.aleksandr.berezovyi.service.AccountService;
import com.aleksandr.berezovyi.service.impl.AccountServiceImpl;
import com.aleksandr.berezovyi.service.ClientService;
import com.aleksandr.berezovyi.service.impl.ClientServiceImpl;
import com.aleksandr.berezovyi.exceptions.ClientNotFoundException;

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

    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RESET = "\u001B[0m";

    //Create and populate DB
    static {
        try {
            Class.forName(DB_DRIVER);
            System.out.println("Connecting to database...");
            DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("Populating database...");
        } catch (SQLException e) {
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private ClientService clientService;
    private AccountService accountService;

    public BankMain() {
        this.clientService = new ClientServiceImpl();
        this.accountService = new AccountServiceImpl();
    }

    public void newClient(String firstname, String lastname, Integer balance) {
        System.out.println("Add new client...");
        Client client = clientService.createClient(firstname, lastname);
        Account account = accountService.createAccount(client.getId(), balance);
        account.setOwnerId(client.getId());
        client.setAccountId(account.getId());
        clientService.updateClient(client);
    }

    public void addMoney(String clientLastname, Double amount) {
        pause(200);
        System.out.println("ADDING MONEY...");
        try {
            Client client = checkClientByLastname(clientLastname);
            System.out.println(clientLastname + " is trying to deposit " + amount);
            validationAmount(amount);
            accountService.addMoney(client.getAccountId(), amount);
            System.out.println(ANSI_GREEN + "SUCCESSFULLY! " + clientLastname + " put the money" + ANSI_RESET);
        } catch (Exception e) {
            pause(100);
            System.err.println("FAILURE! " + clientLastname + "  could not add money");
            System.err.println("(cause: " + e.getMessage() + ")");
        } finally {
            pause(50);
            System.out.println();
        }
    }

    public void getMoney(String clientLastname, Double amount) {
        pause(200);
        System.out.println("GETTING MONEY...");
        try {
            Client client = checkClientByLastname(clientLastname);
            System.out.println(clientLastname + " is trying to withdraw " + amount);
            accountService.removeMoney(client.getAccountId(), amount);
            System.out.println(ANSI_GREEN + "SUCCESSFULLY! " + clientLastname + " got the money" + ANSI_RESET);
        } catch (Exception e) {
            pause(100);
            System.err.println("FAILURE! " + clientLastname + " did not received the money");
            System.err.println("(cause: " + e.getMessage() + ")");
        } finally {
            pause(50);
            System.out.println();
        }
    }

    public void sendMoney(String senderName, String recipientName, Double amount) {
        pause(200);
        try {
            System.out.println("SENDING MONEY...  ");
            Payment payment = createPayment(senderName, recipientName, amount);
            payment.setWhen(LocalDateTime.now());
            accountService.makePayment(payment);
            accountService.savePayment(payment);
            System.out.println(ANSI_GREEN + "SUCCESSFULLY! " + senderName + " send money to " + recipientName + ANSI_RESET);
        } catch (Exception e) {
            pause(100);
            System.err.println("FAILURE! " + senderName + " can't send money to " + recipientName);
            System.err.println("(cause: " + e.getMessage() + ")");
        } finally {
            pause(50);
            System.out.println();
        }
    }

    private Payment createPayment(String senderLastname, String recipientLastname, double amount) {
        Client sender = checkClientByLastname(senderLastname);
        Client recipient = checkClientByLastname(recipientLastname);
        System.out.println("AccountID " + sender.getAccountId() + " sending " + amount + " to " + recipient.getAccountId());
        pause(100);
        checkIfSenderIsRecipient(sender, recipient);
        checkAmountForWithdrawal(sender, amount);
        Payment payment = new Payment();
        payment.setSenderAccountId(sender.getAccountId());
        payment.setRecipientAccountId(recipient.getAccountId());
        payment.setAmount(amount);
        return payment;
    }

    private Client checkClientByLastname(String lastname) {
        Client client = clientService.getClientByLastname(lastname);
        if (client == null) {
            throw new ClientNotFoundException("Client with lastname " + "'" + lastname + "'" + " doesn't exist!");
        }
        return client;
    }

    private void checkIfSenderIsRecipient(Client sender, Client recipient) {
        if (sender.equals(recipient)) {
            throw new IllegalArgumentException("Can't send money to yourself");
        }
    }

    private void checkAmountForWithdrawal(Client client, Double amount) {
        Account account = accountService.getAccountById(client.getAccountId());
        validationAmount(amount);
        if (amount > account.getBalance())
            throw new InsufficientFundsException("Not enough money! Trying to send " + amount + " but was " + account.getBalance());
    }

    private void validationAmount(Double amount) {
        if (amount <= 0)
            throw new IllegalArgumentException("Amount must be > 0");
    }

    private void pause(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void showAllClientsWithAccounts() {
        pause(100);
        List<Account> accounts = new ArrayList<>(accountService.getAllAccounts());
        Set<Client> clients = clientService.getAllClients();
        String leftAlignFormat = "| %-9d | %-8s | %-9s | %-9s | %-7d |%n";
        System.out.format("+-----------+----------+-----------+-----------+---------|%n");
        System.out.format("| AccountID | Balance  | Lastname  | Firstname | ownerID |%n");
        System.out.format("+-----------+----------+-----------+-----------+---------|%n");
        for (Client client : clients) //bad
            for (Account account : accounts)
                if (client.getId().equals(account.getOwnerId()))
                    System.out.format(leftAlignFormat, account.getId(), account.getBalance(),
                            client.getLastname(), client.getFirstname(), account.getOwnerId());

        System.out.format("+-----------+----------+-----------+-----------+---------|%n");
        System.out.println();
    }

    public void showClientBalance(Long clientId) {
        Client client = clientService.getClientById(clientId);
        if (client == null) {
            System.err.println("Client not found with id - " + clientId);
            return;
        }
        System.out.println("---------SHOW-CLIENT-BALANCE-WITH-ID-" + clientId + "---------");
        List<Account> accounts = new ArrayList<>(accountService.getAllAccounts());
        Double clientBalance = clientService.getClientBalance(client, accounts);
        System.out.println(client.getFirstname() + " " + client.getLastname() + " balance" + " is " + clientBalance);
        System.out.println();
    }

    public void showClientWithMaxBalance() {
        System.out.println("----------SHOW-CLIENT-WITH-MAX-BALANCE----------");
        List<Account> accounts = new ArrayList<>(accountService.getAllAccounts());
        Set<Client> clients = clientService.getClientWithMaxBalance(accounts);
        for (Client client : clients) {
            Account account = accountService.getAccountById(client.getAccountId());
            System.out.print(client.getFirstname() + " " + client.getLastname());
            System.out.println(" has the highest balance=" + account.getBalance());
        }
        System.out.println();
    }

    public void showClientWithMinBalance() {
        System.out.println("----------SHOW-CLIENT-WITH-MIN-BALANCE----------");
        List<Account> accounts = new ArrayList<>(accountService.getAllAccounts());
        Set<Client> clients = clientService.getClientWithMinBalance(accounts);
        for (Client client : clients) {
            Account account = accountService.getAccountById(client.getAccountId());
            System.out.print(client.getFirstname() + " " + client.getLastname());
            System.out.println(" has the lowest balance=" + account.getBalance());
        }
        System.out.println();
    }

    public void showPaymentHistory() {
        System.out.println("----------------PAYMENT HISTORY----------------");
        accountService.getAllPayments().forEach(System.out::println);
        System.out.println("-----------------------------------------------");
        System.out.println();
    }

    public void showAllClients() {
        System.out.println("-----------------------CLIENTS-----------------");
        Set<Client> clients = clientService.getAllClients();
        clients.forEach(System.out::println);
        System.out.println("----------------------------------------------");
    }

    public static void main(String[] args) {
        BankMain bank = new BankMain();

        bank.newClient("Ada", "Wong", 299);
        bank.newClient("Albert", "Wesker", 10);
        //add new clients...

        bank.showAllClientsWithAccounts();
        bank.showClientWithMaxBalance();
        bank.showClientBalance(5L);

        bank.sendMoney("Wong", "Wong", 5.0);
        bank.sendMoney("Chambers", "Valentine", 0.0);
        bank.sendMoney("Valentine", "Gionne", 1000.0);
        bank.sendMoney("Wesker", "Valentine", 15.0);
        bank.sendMoney("Wong", "Cha231rs", 15.0);
        //for testing payment transaction, when Wesker sending money to ... , trying withdraw some money from his acc
        Runnable runnable = () -> bank.sendMoney("Wesker", "Valentine", 10.0);
        runnable.run();
        bank.showAllClientsWithAccounts();

        bank.getMoney("Wesker", 5.0);
        bank.getMoney("Wo1ng", 100800.0);
        bank.getMoney("Gionne", 99.0);
        bank.showAllClientsWithAccounts();

        bank.addMoney("Wong", 3711.0);
        bank.addMoney("Wong", 0.0);
        bank.showAllClientsWithAccounts();

        bank.showClientWithMaxBalance();
        bank.showClientWithMinBalance();
        bank.showPaymentHistory();
    }
}
