package rmistore.commons.interfaces;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import rmistore.commons.exceptions.Rejected;
import rmistorebank.helper.RMIStoreBankHelper;

@SuppressWarnings("serial")
public class AccountImpl extends UnicastRemoteObject implements Account {
    
    private double balance = 0;
    private String name;
    private long id;
    private PreparedStatement updateStatement;

    public AccountImpl(long id, String name, float balance, Connection connection)
            throws RemoteException, Rejected {
        super();
        this.id = id;
        this.name = name;
        this.balance = balance;
        try {
            updateStatement = connection.prepareStatement("UPDATE "
                                                          + RMIStoreBankHelper.TABLE_ACCOUNT_NAME
                                                          + " SET balance = ? WHERE name= ? ");
            updateStatement.setString(2, name);
        } catch (SQLException sqle) {
            throw new Rejected("Unable to instantiate account " + sqle.getMessage());
        }
    }

    public AccountImpl(long id, String name, Connection connection)
            throws RemoteException, Rejected {
        this(id, name, 0, connection);
    }

    @Override
    public synchronized void deposit(float value) throws RemoteException,
                                                         Rejected {
        if (value < 0) {
            throw new Rejected("Rejected: Account " + name
                                                + ": Illegal value: " + value);
        }

        boolean success = false;
        try {
            balance += value;
            updateStatement.setDouble(1, balance);
            int rows = updateStatement.executeUpdate();
            if (rows != 1) {
                throw new Rejected("Unable to deposit into account: " + name);
            } else {
                success = true;
            }
            System.out.println("Transaction: Account " + name + ": deposit: $"
                                       + value + ", balance: $" + balance);
        } catch (SQLException sqle) {
            throw new Rejected("Unable to deposit into account: " + name + " " + sqle.getMessage());
        } finally {
            if (!success) {
                balance -= value;
            }
        }
    }

    @Override
    public synchronized void withdraw(float value) throws RemoteException,
                                                          Rejected {
        if (value < 0) {
            throw new Rejected("Rejected: Account " + name
                                                + ": Illegal value: " + value);
        }

        if ((balance - value) < 0) {
            throw new Rejected("Rejected: Account " + name
                                                + ": Negative balance on withdraw: "
                                                + (balance - value));
        }

        boolean success = false;
        try {
            balance -= value;
            updateStatement.setDouble(1, balance);
            int rows = updateStatement.executeUpdate();
            if (rows != 1) {
                throw new Rejected("Unable to deposit into account: " + name);
            } else {
                success = true;
            }
            System.out.println("Transaction: Account " + name + ": deposit: $"
                                       + value + ", balance: $" + balance);
        } catch (SQLException sqle) {
            throw new Rejected("Unable to deposit into account: " + name + " " + sqle.getMessage());
        } finally {
            if (!success) {
                balance += value;
            }
        }
    }

    @Override
    public synchronized double getBalance() throws RemoteException {
        return balance;
    }

    @Override
    public long getAccountNumber() throws RemoteException {
        return id;
    }
}
