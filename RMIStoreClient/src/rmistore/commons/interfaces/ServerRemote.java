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
public interface ServerRemote extends Remote {
    public void register(String name, String pass)
          throws RemoteException, rmistore.commons.exceptions.Rejected;
    public rmistore.commons.interfaces.CustomerRemote login(String name,String pass,
            rmistore.commons.interfaces.ClientRemote clientRemote)
          throws RemoteException, rmistore.commons.exceptions.Rejected;
}
