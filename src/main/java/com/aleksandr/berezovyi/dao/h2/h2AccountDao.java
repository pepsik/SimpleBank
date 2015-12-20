package com.aleksandr.berezovyi.dao.h2;

import com.aleksandr.berezovyi.dao.AccountDao;
import com.aleksandr.berezovyi.dao.util.NamedParameterStatement;
import com.aleksandr.berezovyi.model.Account;
import com.aleksandr.berezovyi.model.Payment;
import org.h2.util.JdbcUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by pepsik on 12/19/2015.
 */
public class h2AccountDao implements AccountDao {
    private static final String DB_URL = "jdbc:h2:~/bank";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";

    private static final String INSERT_SQL = "INSERT INTO ACCOUNT (balance,ownerid) VALUES (?,?)";
    private static final String INSERT_PAYMENT_SQL = "INSERT INTO PAYMENT (SENDERACCID,RECIPIENTACCID,AMOUNT,WHEN) VALUES (?,?,?,?)";
    private static final String SELECT_ALL_SQL = "SELECT * FROM ACCOUNT";
    private static final String SELECT_ALL_PAYMENTS_SQL = "SELECT * FROM PAYMENT";
    private static final String SELECT_BY_ID_SQL = "SELECT * FROM ACCOUNT WHERE id = ?";
    private static final String UPDATE_SQL = "UPDATE Account SET balance = :balance WHERE id = :id";

    private Connection getConnection() {
        try {
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException("Troubles with getting connection", e);
        }
    }

    @Override
    public Account create(Account account) {
        PreparedStatement preparedStatement = null;
        Connection connection = null;
        try {
            connection = getConnection();
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            preparedStatement = connection.prepareStatement(INSERT_SQL);
            preparedStatement.setDouble(1, account.getBalance());
            preparedStatement.setLong(2, account.getOwnerId());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    account.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.closeSilently(preparedStatement);
            JdbcUtils.closeSilently(connection);
        }
        return account;
    }

    @Override
    public Account get(Long sentId) {
        PreparedStatement preparedStatement = null;
        Connection connection = null;
        Account account = null;
        try {
            connection = getConnection();
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            preparedStatement = connection.prepareStatement(SELECT_BY_ID_SQL);
            preparedStatement.setLong(1, sentId);
            ResultSet rs = preparedStatement.executeQuery();
            account = parseResultSet(rs).iterator().next();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.closeSilently(preparedStatement);
            JdbcUtils.closeSilently(connection);
        }
        return account;
    }

    @Override
    public Set<Account> getAll() {
        Statement statement = null;
        Connection connection = null;
        Set<Account> result = new HashSet<>();
        try {
            connection = getConnection();
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SELECT_ALL_SQL);
            result = parseResultSet(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.closeSilently(statement);
            JdbcUtils.closeSilently(connection);
        }
        return result;
    }

    @Override
    public Account update(Account account) {
        Connection connection = null;
        NamedParameterStatement namedParameterStatement = null;
        try {
            connection = getConnection();
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            namedParameterStatement = new NamedParameterStatement(connection, UPDATE_SQL);
            namedParameterStatement.setLong("id", account.getId());
            namedParameterStatement.setDouble("balance", account.getBalance());
            namedParameterStatement.executeUpdate();
            namedParameterStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.closeSilently(connection);
            closeSilently(namedParameterStatement);
        }
        return account;
    }

    @Override
    public Payment makePayment(Payment payment) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        NamedParameterStatement namedParameterStatement = null;
        try {
            connection = getConnection();
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            connection.setAutoCommit(false);//begin transaction

            //get sender account
            preparedStatement = connection.prepareStatement(SELECT_BY_ID_SQL);
            preparedStatement.setLong(1, payment.getSenderAccountId());
            ResultSet resultSet = preparedStatement.executeQuery();
            Account sender = parseResultSet(resultSet).iterator().next();

            //get recipient
            preparedStatement.setLong(1, payment.getRecipientAccountId());
            resultSet = preparedStatement.executeQuery();
            Account recipient = parseResultSet(resultSet).iterator().next();

            //transfer money
            sender.withdraw(payment.getAmount());
            recipient.deposit(payment.getAmount());

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //save sender
            namedParameterStatement = new NamedParameterStatement(connection, UPDATE_SQL);
            namedParameterStatement.setLong("id", sender.getId());
            namedParameterStatement.setDouble("balance", sender.getBalance());
            namedParameterStatement.executeUpdate();
            //save recipient
            namedParameterStatement.setLong("id", recipient.getId());
            namedParameterStatement.setDouble("balance", recipient.getBalance());
            namedParameterStatement.executeUpdate();

            connection.commit(); //end transaction
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.closeSilently(preparedStatement);
            JdbcUtils.closeSilently(connection);
            closeSilently(namedParameterStatement);
        }
        return payment;
    }

    @Override
    public Payment savePayment(Payment payment) {
        PreparedStatement preparedStatement = null;
        Connection connection = null;
        try {
            connection = getConnection();
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            preparedStatement = connection.prepareStatement(INSERT_PAYMENT_SQL);
            preparedStatement.setLong(1, payment.getSenderAccountId());
            preparedStatement.setLong(2, payment.getRecipientAccountId());
            preparedStatement.setDouble(3, payment.getAmount());
            preparedStatement.setTimestamp(4, Timestamp.valueOf(payment.getWhen()));
            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    payment.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.closeSilently(preparedStatement);
            JdbcUtils.closeSilently(connection);
        }
        return payment;
    }

    @Override
    public Set<Payment> getAllPayments() {
        Statement statement = null;
        Connection connection = null;
        Set<Payment> result = new HashSet<>();
        try {
            connection = getConnection();
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SELECT_ALL_PAYMENTS_SQL);
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                Long senderAccId = resultSet.getLong("senderaccid");
                Long recipientAccId = resultSet.getLong("recipientaccid");
                Double amount = resultSet.getDouble("amount");
                Timestamp timestamp = resultSet.getTimestamp("when");
                Payment payment = new Payment();
                payment.setId(id);
                payment.setSenderAccountId(senderAccId);
                payment.setRecipientAccountId(recipientAccId);
                payment.setAmount(amount);
                payment.setWhen(timestamp.toLocalDateTime());
                result.add(payment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.closeSilently(statement);
            JdbcUtils.closeSilently(connection);
        }
        return result;
    }

    private Set<Account> parseResultSet(ResultSet rs) throws SQLException {
        Set<Account> accounts = new HashSet<>();
        while (rs.next()) {
            Long id = rs.getLong("id");
            double balance = rs.getDouble("balance");
            Long ownerid = rs.getLong("ownerid");
            Account account = new Account();
            account.setId(id);
            account.deposit(balance);
            account.setOwnerId(ownerid);
            accounts.add(account);
        }
        return accounts;
    }

    private void closeSilently(NamedParameterStatement namedParameterStatement) {
        if (namedParameterStatement != null)
            try {
                namedParameterStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }
}
