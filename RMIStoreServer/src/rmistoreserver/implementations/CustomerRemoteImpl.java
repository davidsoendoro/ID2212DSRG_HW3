/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmistoreserver.implementations;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import rmistore.commons.exceptions.Rejected;
import rmistore.commons.interfaces.Item;
import rmistore.commons.interfaces.Transaction;
/**
 *
 * @author davidsoendoro
 */
public class CustomerRemoteImpl extends UnicastRemoteObject 
implements rmistore.commons.interfaces.CustomerRemote {
    int myId;
    ServerRemoteImpl serverRemoteObj;
    
    public CustomerRemoteImpl(int myId, ServerRemoteImpl serverRemoteObj) throws RemoteException {
        this.myId=myId;
        this.serverRemoteObj=serverRemoteObj;
    }

    @Override
    public synchronized void sellItem(String itemName, float price, int quantity) throws Rejected,RemoteException{
        if(this.serverRemoteObj.addItem(itemName, price, quantity,myId)==false){
            throw new Rejected("Could not be added to sell list");
        }
        else
           this.serverRemoteObj.getClientObj(myId).receiveMessage("Item listed for selling");
    }
    
    @Override
    public synchronized void buyItem(int itemId)throws Rejected,RemoteException{
        if(this.serverRemoteObj.buyItem(myId,itemId)==true){
            this.serverRemoteObj.getClientObj(myId).receiveMessage("Transaction successful. Money debited from your account");
        }
        else{
            this.serverRemoteObj.getClientObj(myId).receiveMessage("You could not buy!");
        } 
    }
    @Override
    public synchronized void wishItem(String name, float price)throws Rejected,RemoteException {
        this.serverRemoteObj.wishItemForCustomer(name, myId,price);
    }

    @Override
    public synchronized ArrayList<Item> getOtherItems()throws Rejected,RemoteException {
        return this.serverRemoteObj.getOtherItems(myId);
    }
    
    @Override
    public synchronized ArrayList<Item> getUserItems()throws Rejected,RemoteException {
        return this.serverRemoteObj.getUserItemsFromHash(myId);
    }

    @Override
    public synchronized boolean unRegister()throws Rejected,RemoteException{
        this.serverRemoteObj.unregister(myId);
        return true; 
    }

    @Override
    public void removeItem(int itemId) throws Rejected, RemoteException {
        this.serverRemoteObj.removeItem(itemId);
    }
    
    @Override
    public double checkBalance(){
        return this.serverRemoteObj.checkCustomerBalance(myId);
    }
    
    
    @Override
    public ArrayList<Transaction> getUserTransactions() throws Rejected, RemoteException {
         //To change body of generated methods, choose Tools | Templates.
        return this.serverRemoteObj.retrieveUserTransaction(myId);
    }
}
