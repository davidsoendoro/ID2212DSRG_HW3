package rmistore.commons.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import rmistore.commons.exceptions.Rejected;

public interface Bank extends Remote {
    public Account newAccount(String name) throws RemoteException, Rejected;

    public Account getAccount(String name) throws RemoteException;

    public boolean deleteAccount(String name) throws RemoteException;

    public String[] listAccounts() throws RemoteException;
}
