package rmistore.commons.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Account extends Remote {
    public double getBalance() throws RemoteException;

    public void deposit(float value) throws RemoteException, rmistore.commons.exceptions.Rejected;

    public void withdraw(float value) throws RemoteException, rmistore.commons.exceptions.Rejected;
}
