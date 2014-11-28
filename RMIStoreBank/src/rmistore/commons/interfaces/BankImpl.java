package rmistore.commons.interfaces;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import rmistore.commons.exceptions.Rejected;
import rmistorebank.helper.RMIStoreBankHelper;


@SuppressWarnings("serial")
public class BankImpl extends UnicastRemoteObject implements Bank {
    
    private PreparedStatement createAccountStatement;
    private PreparedStatement findAccountStatement;
    private PreparedStatement deleteAccountStatement;
    private Map<String, Account> accounts = new HashMap<>();
    private String datasource;
    private String dbms;

    public BankImpl(String datasource, String dbms)
            throws RemoteException, ClassNotFoundException, SQLException {
        super();
        this.datasource = datasource;
        this.dbms = dbms;
        Connection connection = createDatasource();
        prepareStatements(connection);
    }

    private Connection createDatasource() throws ClassNotFoundException, SQLException {
        Connection connection = getConnection();
        boolean exist = false;
        int tableNameColumn = 3;
        DatabaseMetaData dbm = connection.getMetaData();
        for (ResultSet rs = dbm.getTables(null, null, null, null); rs.next();) {
            if (rs.getString(tableNameColumn).equals(RMIStoreBankHelper.TABLE_ACCOUNT_NAME)) {
                exist = true;
                rs.close();
                break;
            }
        }
        if (!exist) {
            Statement statement = connection.createStatement();
            statement.executeUpdate("CREATE TABLE " + RMIStoreBankHelper.TABLE_ACCOUNT_NAME
                                            + " (name VARCHAR(32) PRIMARY KEY, balance FLOAT)");
        }
        return connection;
    }

    private Connection getConnection()
            throws ClassNotFoundException, SQLException {
        if (dbms.equalsIgnoreCase("cloudscape")) {
            Class.forName("COM.cloudscape.core.RmiJdbcDriver");
            return DriverManager.getConnection(
                    "jdbc:cloudscape:rmi://localhost:1099/" + datasource
                            + ";create=true;");
        } else if (dbms.equalsIgnoreCase("pointbase")) {
            Class.forName("com.pointbase.jdbc.jdbcUniversalDriver");
            return DriverManager.getConnection(
                    "jdbc:pointbase:server://localhost:9092/" + datasource
                            + ",new", "PBPUBLIC", "PBPUBLIC");
        } else if (dbms.equalsIgnoreCase("derby")) {
            Class.forName("org.apache.derby.jdbc.ClientXADataSource");
            return DriverManager.getConnection(
                    "jdbc:derby://localhost:1527/" + datasource + ";create=true",
                    "rmibank", "nescafe");
        } else if (dbms.equalsIgnoreCase("mysql")) {
            Class.forName("com.mysql.jdbc.Driver");
            return DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/" + datasource, "root", "javajava");
        } else {
            return null;
        }
    }

    private void prepareStatements(Connection connection) throws SQLException {
        createAccountStatement = connection.prepareStatement("INSERT INTO "
                                                                     + RMIStoreBankHelper.TABLE_ACCOUNT_NAME + " (name, balance) VALUES (?, 0)");
        findAccountStatement = connection.prepareStatement("SELECT * from "
                                                                   + RMIStoreBankHelper.TABLE_ACCOUNT_NAME + " WHERE NAME = ?");
        deleteAccountStatement = connection.prepareStatement("DELETE FROM "
                                                                     + RMIStoreBankHelper.TABLE_ACCOUNT_NAME
                                                                     + " WHERE name = ?");
    }

    @Override
    public synchronized String[] listAccounts() {
        return accounts.keySet().toArray(new String[1]);
    }

    @Override
    public synchronized Account newAccount(String name) throws RemoteException,
                                                               Rejected {
        AccountImpl account = (AccountImpl) accounts.get(name);
        if (account != null) {
            System.out.println("Account [" + name + "] exists!!!");
            throw new Rejected("Rejected: Bank:  Account for: "
                                                + name + " already exists: " + account);
        }
        ResultSet result = null;
        try {
            findAccountStatement.setString(1, name);
            result = findAccountStatement.executeQuery();

            if (result.next()) {
                // account exists, instantiate, put in cache and throw exception.
                account = new AccountImpl(name, result.getFloat("balance"),
                                          getConnection());
                accounts.put(name, account);
                throw new Rejected("Rejected: Account for: " + name
                                                    + " already exists");
            }
            result.close();

            // create account.
            createAccountStatement.setString(1, name);
            int rows = createAccountStatement.executeUpdate();
            if (rows == 1) {
                account = new AccountImpl(name, getConnection());
                accounts.put(name, account);
                System.out.println("Bank: Account: " + account
                                           + " has been created for " + name);
                return account;
            } else {
                throw new Rejected("Cannot create an account for " + name);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new Rejected("Cannot create an account for " + name + " " + e.getMessage());
        }
    }

    @Override
    public synchronized Account getAccount(String name) throws RemoteException,
                                                               Rejected {
        if (name == null) {
            return null;
        }

        Account acct = accounts.get(name);
        if (acct == null) {
            try {
                findAccountStatement.setString(1, name);
                ResultSet result = findAccountStatement.executeQuery();
                if (result.next()) {
                    acct = new AccountImpl(result.getString("name"),
                                           result.getFloat("balance"), getConnection());
                    result.close();
                    accounts.put(name, acct);
                } else {
                    return null;
                }
            } catch (SQLException | ClassNotFoundException e) {
                throw new Rejected("Unable to find account for " + name + " " + e.getMessage());
            }
        }
        return acct;
    }

    @Override
    public synchronized boolean deleteAccount(String name) throws Rejected {
        if (!hasAccount(name)) {
            return false;
        }
        accounts.remove(name);
        try {
            deleteAccountStatement.setString(1, name);
            int rows = deleteAccountStatement.executeUpdate();
            if (rows != 1) {
                throw new Rejected("Unable to delete account..." + name);
            }
        } catch (SQLException e) {
            System.out.println("Unable to delete account for " + name + ": "
                                       + e.getMessage());
            throw new Rejected("Unable to delete account..." + name + " " + e.getMessage());
        }
        System.out.println("Bank: Account for " + name + " has been deleted");
        return true;
    }

    private boolean hasAccount(String name) {
        return accounts.get(name) != null;
    }
}
