/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmistoreserver.implementations;

import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import rmistore.commons.exceptions.Rejected;
import rmistore.commons.interfaces.ClientRemote;
import rmistore.commons.interfaces.Item;
import rmistoreserver.CustomerWrap;
import rmistoreserver.Wish;
import rmistore.commons.interfaces.Bank;
/**
 *
 * @author davidsoendoro
 */
public class ServerRemoteImpl extends UnicastRemoteObject 
implements rmistore.commons.interfaces.ServerRemote {
    int itemId=200;
    int customerId=1;
    Bank bankRMIObj;
    HashMap<Integer, Item> itemHash= new HashMap<>();
    HashMap<Integer,CustomerWrap> customerHash=new HashMap<>();
    HashMap<String, ArrayList<Wish>> wishHash=new HashMap<>();

    public ServerRemoteImpl(Bank bankRMIObj) throws RemoteException {
        this.bankRMIObj = bankRMIObj;
    }

    @Override
    public synchronized rmistore.commons.interfaces.CustomerRemote register(String name,
            rmistore.commons.interfaces.ClientRemote clientRemote) 
            throws RemoteException, rmistore.commons.exceptions.Rejected {
        int id=this.getCustomerId();
        //check uniqueness of name
        for(int key:customerHash.keySet()){
            if(customerHash.get(key).getName().equals(name)){
                throw new Rejected("Name already in use. Please choose another name.");
            }
        }
        customerHash.put(id,new CustomerWrap(name, clientRemote));
        rmistore.commons.interfaces.CustomerRemote client = new CustomerRemoteImpl(id,this);
        //open account
        bankRMIObj.newAccount(name);
        //credit initial balance
        bankRMIObj.getAccount(name).deposit(200);
        System.out.println("Register called! " + name);
        clientRemote.receiveMessage("Welcome!");
        return client;
    }
    
    public synchronized boolean unregister(int customerId){
        customerHash.remove(customerId);
        return true;
    }
    public synchronized int getCustomerId(){
        return customerId++;
    }
    
    public synchronized int getItemId(){
        return itemId++;
    }
    
    public synchronized boolean addItem(int itemId, Item item){
       itemHash.put(itemId, item);
       // System.out.println("status of item hash "+ itemHash.toString());
       //notify wishList customers
       for(String key:wishHash.keySet()){
            if(item.getName().contains(key)==true){
                for(Wish wishObj:wishHash.get(key)){
                    if(item.getPrice()<=wishObj.getPrice()){
                        try{
                        this.getClientObj(wishObj.getCustomerid()).receiveMessage("Item "+ key+ " available for price "+item.getPrice());
                        }
                        catch(RemoteException r){
                                
                        }
                    }
                    }
                
            }
        }
       return true;
    }
    
    public synchronized boolean buyItem(int customerId, int itemId){
        try{
        if(itemHash.get(itemId)!=null){
            if(bankRMIObj.getAccount(customerHash.get(customerId).getName()).getBalance()>=itemHash.get(itemId).getPrice()){
            Item item= itemHash.get(itemId);
            //credit to seller and debit to buyer
                this.getClientObj(item.getCustomerId()).receiveMessage("Your item "+item.getName()+" sold. Money credited to your account."); 
                bankRMIObj.getAccount(customerHash.get(item.getCustomerId()).getName()).deposit((float)item.getPrice());
                bankRMIObj.getAccount(customerHash.get(customerId).getName()).withdraw((float)item.getPrice());
            return true;
        }
            else{
                this.getClientObj(customerId).receiveMessage("Insufficient balance");
                return false;
            }
                
        }
        else{
           this.getClientObj(customerId).receiveMessage("Item is not available"); 
           return false;
        }
        }
        catch(RemoteException ex){
            System.out.println("Remote Exception: "+ ex);
            return false;
        }
    }
    public ClientRemote getClientObj(int customerId){
        return customerHash.get(customerId).getClientRemoteObj();
    }
    public ArrayList<Item> getUserItemsFromHash(int customerId){
        ArrayList<Item> items=new ArrayList<>();
        for(int key:itemHash.keySet()){
            if(itemHash.get(key).getCustomerId()==customerId){
                items.add(itemHash.get(key));
            }
        }
        return items;
    }
    
    public ArrayList<Item> getAllItemsFromHash(){
        ArrayList<Item> items=new ArrayList<>();
        for(int key:itemHash.keySet()){
                items.add(itemHash.get(key));
        }
        return items;
    }
    
    public void wishItemForCustomer(String name,Wish wishObj){
        ArrayList<Wish> wishList;
        if(wishHash.get(name)!=null){
            wishList= wishHash.get(name);
        }
        else{
            wishList=new ArrayList();
        }
        wishList.add(wishObj);
        wishHash.put(name,wishList);
    }

    ArrayList<Item> getOtherItems(int customerId) {
        ArrayList<Item> items=new ArrayList<>();
        for(int key:itemHash.keySet()){
            if(itemHash.get(key).getCustomerId() != customerId){
                items.add(itemHash.get(key));
            }
        }
        return items;
    }
    
    public double checkCustomerBalance(int customerId){
        try{
        return bankRMIObj.getAccount(customerHash.get(customerId).getName()).getBalance();
        }
        catch(RemoteException r){
            System.out.println("Exception: "+r);
        }
        return 0;
    }
}