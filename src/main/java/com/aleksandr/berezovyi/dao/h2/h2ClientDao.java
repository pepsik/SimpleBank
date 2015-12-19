package com.aleksandr.berezovyi.dao.h2;

import com.aleksandr.berezovyi.dao.ClientDao;
import com.aleksandr.berezovyi.dao.util.NamedParameterStatement;
import com.aleksandr.berezovyi.model.Client;
import org.h2.util.JdbcUtils;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by pepsik on 12/19/2015.
 */
public class h2ClientDao implements ClientDao {

    private static final String DB_URL = "jdbc:h2:~/bank";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";

    private static final String INSERT_SQL = "INSERT INTO Client (firstname,lastname) VALUES (?,?)";
    private static final String SELECT_ALL_SQL = "SELECT * FROM Client";
    private static final String SELECT_BY_LASTNAME_SQL = "SELECT * FROM Client WHERE lastname = ?";
    private static final String SELECT_BY_ID_SQL = "SELECT * FROM Client WHERE id = ?";
    private static final String UPDATE_SQL = "UPDATE Client SET firstname = :firstname, lastname = :lastname, accountid = :accountid WHERE id = :id";

    private Connection getConnection() {
        try {
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException("Troubles with getting connection");
        }
    }

    @Override
    public Client create(Client client) {
        PreparedStatement ps = null;
        Connection conn = null;
        try {
            conn = getConnection();
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            ps = conn.prepareStatement(INSERT_SQL);

            ps.setString(1, client.getFirstname());
            ps.setString(2, client.getLastname());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    client.setId(generatedKeys.getLong(1));
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
        return client;
    }

    @Override
    public Client getById(Long sentId) {
        PreparedStatement preparedStatement = null;
        Connection conn = null;
        ResultSet rs;
        Client client = new Client();
        try {
            conn = getConnection();
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            preparedStatement = conn.prepareStatement(SELECT_BY_ID_SQL);
            preparedStatement.setLong(1, sentId);
            rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Long id = rs.getLong("id");
                String firstname = rs.getString("firstname");
                String lastname = rs.getString("lastname");
                Long accountId = rs.getLong("accountid");
                client.setId(id);
                client.setFirstname(firstname);
                client.setLastname(lastname);
                client.setAccountId(accountId);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.closeSilently(conn);
            JdbcUtils.closeSilently(preparedStatement);
        }
        return client;
    }

    @Override
    public Client getByLastname(String sentLastname) {
        PreparedStatement preparedStatement = null;
        Connection conn = null;
        ResultSet rs;
        Set<Client> clients = new HashSet<>();
        try {
            conn = getConnection();
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            preparedStatement = conn.prepareStatement(SELECT_BY_LASTNAME_SQL);
            preparedStatement.setString(1, sentLastname);
            rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Long id = rs.getLong("id");
                String firstname = rs.getString("firstname");
                String lastname = rs.getString("lastname");
                Long accountId = rs.getLong("accountid");
                Client client = new Client();
                client.setId(id);
                client.setFirstname(firstname);
                client.setLastname(lastname);
                client.setAccountId(accountId);
                clients.add(client);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.closeSilently(conn);
            JdbcUtils.closeSilently(preparedStatement);
        }

        if (clients.size() == 0)
            return null;
        else
            return clients.iterator().next();
    }

    @Override
    public Set<Client> getAllClients() {
        Statement statement = null;
        Connection conn = null;
        ResultSet rs;
        Set<Client> result = new HashSet<>();
        try {
            conn = getConnection();
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            statement = conn.createStatement();
            rs = statement.executeQuery(SELECT_ALL_SQL);

            while (rs.next()) {
                Long id = rs.getLong("id");
                String firstname = rs.getString("firstname");
                String lastname = rs.getString("lastname");
                Client client = new Client();
                client.setId(id);
                client.setFirstname(firstname);
                client.setLastname(lastname);
                result.add(client);
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
    public Client update(Client client) {
        Connection conn = null;
        try {
            conn = getConnection();
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            NamedParameterStatement p = new NamedParameterStatement(conn, UPDATE_SQL);
            p.setLong("id", client.getId());
            p.setString("firstname", client.getFirstname());
            p.setString("lastname", client.getLastname());
            p.setLong("accountid", client.getAccountId());
            p.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.closeSilently(conn);
        }
        return client;
    }
}
