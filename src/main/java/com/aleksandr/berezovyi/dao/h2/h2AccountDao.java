package com.aleksandr.berezovyi.dao.h2;

import com.aleksandr.berezovyi.dao.AccountDao;
import com.aleksandr.berezovyi.dao.util.NamedParameterStatement;
import com.aleksandr.berezovyi.model.Account;
import org.h2.util.JdbcUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pepsik on 12/19/2015.
 */
public class h2AccountDao implements AccountDao {
    private static final String DB_URL = "jdbc:h2:~/bank";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";

    private static final String INSERT_SQL = "INSERT INTO ACCOUNT (balance,ownerid) VALUES (?,?)";
    private static final String SELECT_ALL_SQL = "SELECT * FROM ACCOUNT";
    private static final String SELECT_BY_ID_SQL = "SELECT * FROM ACCOUNT WHERE id = ?";
    private static final String UPDATE_SQL = "UPDATE Account SET balance = :balance, ownerid = :ownerid WHERE id = :id";

    private Connection getConnection() {
        try {
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException("Troubles with getting connection", e);
        }
    }

    @Override
    public Account create(Account account) {
        PreparedStatement ps = null;
        Connection conn = null;

        try {
            conn = getConnection();
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            ps = conn.prepareStatement(INSERT_SQL);
            ps.setDouble(1, account.getBalance());
            ps.setLong(2, account.getOwnerId());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    account.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.closeSilently(conn);
            JdbcUtils.closeSilently(ps);
        }
        return account;
    }

    @Override
    public Account get(Long sentId) {
        PreparedStatement preparedStatement = null;
        Connection conn = null;
        ResultSet rs;
        Account account = null;
        try {
            conn = getConnection();
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            preparedStatement = conn.prepareStatement(SELECT_BY_ID_SQL);
            preparedStatement.setLong(1, sentId);
            rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Long id = rs.getLong("id");
                double balance = rs.getDouble("balance");
                Long ownerid = rs.getLong("ownerid");
                account = new Account();
                account.setId(id);
                account.deposit(balance);
                account.setOwnerId(ownerid);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.closeSilently(conn);
            JdbcUtils.closeSilently(preparedStatement);
        }
        return account;
    }

    @Override
    public List<Account> getAllAccounts() {
        Statement statement = null;
        Connection conn = null;
        ResultSet rs;
        List<Account> result = new ArrayList<>();
        try {
            conn = getConnection();
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            statement = conn.createStatement();
            rs = statement.executeQuery(SELECT_ALL_SQL);

            while (rs.next()) {
                Long id = rs.getLong("id");
                Double balance = rs.getDouble("balance");
                Long ownerId = rs.getLong("ownerid");
                Account account = new Account();
                account.setId(id);
                account.setOwnerId(ownerId);
                account.deposit(balance);
                result.add(account);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.closeSilently(conn);
            JdbcUtils.closeSilently(statement);
        }
        return result;
    }

    @Override
    public Account update(Account account) {
        Connection conn = null;
        try {
            conn = getConnection();
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            NamedParameterStatement p = new NamedParameterStatement(conn, UPDATE_SQL);
            p.setLong("id", account.getId());
            p.setDouble("balance", account.getBalance());
            p.setLong("ownerid", account.getOwnerId());
            p.executeUpdate();
            p.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.closeSilently(conn);
        }
        return account;
    }
}
