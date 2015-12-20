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
        PreparedStatement preparedStatement = null;
        Connection conn = null;
        try {
            conn = getConnection();
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            preparedStatement = conn.prepareStatement(INSERT_SQL);

            preparedStatement.setString(1, client.getFirstname());
            preparedStatement.setString(2, client.getLastname());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
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
            JdbcUtils.closeSilently(preparedStatement);
        }
        return client;
    }

    @Override
    public Client getById(Long sentId) {
        PreparedStatement preparedStatement = null;
        Connection conn = null;
        Client client = new Client();
        try {
            conn = getConnection();
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            preparedStatement = conn.prepareStatement(SELECT_BY_ID_SQL);
            preparedStatement.setLong(1, sentId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String firstname = resultSet.getString("firstname");
                String lastname = resultSet.getString("lastname");
                Long accountId = resultSet.getLong("accountid");
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
        Set<Client> clients = new HashSet<>();
        try {
            conn = getConnection();
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            preparedStatement = conn.prepareStatement(SELECT_BY_LASTNAME_SQL);
            preparedStatement.setString(1, sentLastname);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String firstname = resultSet.getString("firstname");
                String lastname = resultSet.getString("lastname");
                Long accountId = resultSet.getLong("accountid");
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
    public Set<Client> getAll() {
        Statement statement = null;
        Connection connection = null;
        ResultSet resultSet;
        Set<Client> result = new HashSet<>();
        try {
            connection = getConnection();
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            statement = connection.createStatement();
            resultSet = statement.executeQuery(SELECT_ALL_SQL);

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String firstname = resultSet.getString("firstname");
                String lastname = resultSet.getString("lastname");
                Client client = new Client();
                client.setId(id);
                client.setFirstname(firstname);
                client.setLastname(lastname);
                result.add(client);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.closeSilently(connection);
            JdbcUtils.closeSilently(statement);
        }
        return result;
    }

    @Override
    public Client update(Client client) {
        Connection conn = null;
        NamedParameterStatement namedParameterStatement = null;
        try {
            conn = getConnection();
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            namedParameterStatement = new NamedParameterStatement(conn, UPDATE_SQL);
            namedParameterStatement.setLong("id", client.getId());
            namedParameterStatement.setString("firstname", client.getFirstname());
            namedParameterStatement.setString("lastname", client.getLastname());
            namedParameterStatement.setLong("accountid", client.getAccountId());
            namedParameterStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.closeSilently(conn);
            closeSilently(namedParameterStatement);
        }
        return client;
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
