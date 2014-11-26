package rmistore.commons.interfaces;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import rmistore.commons.exceptions.Rejected;

@SuppressWarnings("serial")
public class AccountImpl extends UnicastRemoteObject implements Account {
    private double balance = 0;
    private String name;

    /**
     * Constructs a persistently named object.
     */
    public AccountImpl(String name) throws RemoteException {
        super();
        this.name = name;
    }

    @Override
    public synchronized void deposit(float value) throws RemoteException,
                                                         Rejected {
        if (value < 0) {
            throw new Rejected("Rejected: Account " + name + ": Illegal value: " + value);
        }
        balance += value;
        System.out.println("Transaction: Account " + name + ": deposit: $" + value + ", balance: $"
                           + balance);
    }

    @Override
    public synchronized void withdraw(float value) throws RemoteException,
                                                          Rejected {
        if (value < 0) {
            throw new Rejected("Rejected: Account " + name + ": Illegal value: " + value);
        }
        if ((balance - value) < 0) {
            throw new Rejected("Rejected: Account " + name
                                        + ": Negative balance on withdraw: " + (balance - value));
        }
        balance -= value;
        System.out.println("Transaction: Account " + name + ": withdraw: $" + value + ", balance: $"
                           + balance);
    }

    @Override
    public synchronized double getBalance() throws RemoteException {
        return balance;
    }
}
