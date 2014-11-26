/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmistore.commons.interfaces;
import rmistore.commons.exceptions.Rejected;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 *
 * @author davidsoendoro
 */
public interface CustomerRemote extends Remote{
    public void sellItem(String itemName, double price) throws Rejected,RemoteException;
    public void removeItem(int itemId) throws Rejected,RemoteException;
    public void buyItem(int itemId)throws Rejected,RemoteException;
    public void wishItem(String name, double price)throws Rejected,RemoteException;
    public ArrayList<Item> getUserItems()throws Rejected,RemoteException;
    public ArrayList<Item> getOtherItems()throws Rejected,RemoteException;
    public double checkBalance() throws RemoteException;
    public boolean unRegister()throws Rejected,RemoteException;
}
