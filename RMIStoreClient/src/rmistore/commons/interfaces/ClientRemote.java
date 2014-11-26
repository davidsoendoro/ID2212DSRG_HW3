/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmistore.commons.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author davidsoendoro
 */
public interface ClientRemote extends Remote {
    public boolean receiveMessage(String message)
          throws RemoteException;
    
    public void updateBalance(double balance)
            throws RemoteException;
}
