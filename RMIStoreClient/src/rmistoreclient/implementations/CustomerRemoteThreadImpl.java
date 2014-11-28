/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmistoreclient.implementations;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JProgressBar;
import rmistore.commons.exceptions.Rejected;
import rmistore.commons.interfaces.CustomerRemote;
import rmistore.commons.interfaces.Item;
import rmistoreclient.helper.RMIStoreClientHelper;
import rmistoreclient.interfaces.Callback;

/**
 *
 * @author davidsoendoro
 */
public class CustomerRemoteThreadImpl implements CustomerRemote {

    private final CustomerRemote customerRemoteObj;
    private boolean isCalling = false;
    public Callback callback;
    private JProgressBar jProgressBar;
    
    public CustomerRemoteThreadImpl(CustomerRemote customerRemoteObj) {
        this.customerRemoteObj = customerRemoteObj;
    }

    @Override
    public void sellItem(final String itemName, final double price) throws Rejected, RemoteException {
        if(!isCalling) {
            new Thread() {

                @Override
                public void run() {
                    try {
                        RMIStoreClientHelper.customerRemoteObj.getLoader().setIndeterminate(true);
                        isCalling = true;
                        customerRemoteObj.sellItem(itemName, price);
                        callback.doCallback("sellItem");
                        RMIStoreClientHelper.customerRemoteObj.getLoader().setIndeterminate(false);
                    } catch (Rejected | RemoteException ex) {
                        Logger.getLogger(CustomerRemoteThreadImpl.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    isCalling = false;
                }

            }.start();
        }
    }

    @Override
    public synchronized void removeItem(final int itemId) throws Rejected, RemoteException {
        if(!isCalling) {
            new Thread() {

                @Override
                public void run() {
                    try {
                        RMIStoreClientHelper.customerRemoteObj.getLoader().setIndeterminate(true);
                        isCalling = true;
                        customerRemoteObj.removeItem(itemId);
                        callback.doCallback("removeItem");
                        RMIStoreClientHelper.customerRemoteObj.getLoader().setIndeterminate(false);
                    } catch (Rejected | RemoteException ex) {
                        Logger.getLogger(CustomerRemoteThreadImpl.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    isCalling = false;
                }

            }.start();
        }
    }

    @Override
    public void buyItem(final int itemId) throws Rejected, RemoteException {
        if(!isCalling) {
            new Thread() {

                @Override
                public void run() {
                    try {
                        RMIStoreClientHelper.customerRemoteObj.getLoader().setIndeterminate(true);
                        isCalling = true;
                        customerRemoteObj.buyItem(itemId);
                        callback.doCallback(null);
                        RMIStoreClientHelper.customerRemoteObj.getLoader().setIndeterminate(false);
                    } catch (Rejected | RemoteException ex) {
                        Logger.getLogger(CustomerRemoteThreadImpl.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    isCalling = false;
                }

            }.start();
        }
    }

    @Override
    public void wishItem(final String name, final double price) throws Rejected, RemoteException {
        if(!isCalling) {
            new Thread() {

                @Override
                public void run() {
                    try {
                        RMIStoreClientHelper.customerRemoteObj.getLoader().setIndeterminate(true);
                        isCalling = true;
                        customerRemoteObj.wishItem(name, price);
                        callback.doCallback(null);
                        RMIStoreClientHelper.customerRemoteObj.getLoader().setIndeterminate(false);
                    } catch (Rejected | RemoteException ex) {
                        Logger.getLogger(CustomerRemoteThreadImpl.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    isCalling = false;                        
                }

            }.start();
        }
    }

    @Override
    public ArrayList<Item> getOtherItems() throws Rejected, RemoteException {
        if(!isCalling) {
            new Thread() {

                @Override
                public void run() {
                    try {
                        RMIStoreClientHelper.customerRemoteObj.getLoader().setIndeterminate(true);
                        isCalling = true;
                        ArrayList<Item> returnItem = customerRemoteObj.getOtherItems();
                        callback.doCallback(returnItem);
                        RMIStoreClientHelper.customerRemoteObj.getLoader().setIndeterminate(false);
                    } catch (Rejected | RemoteException ex) {
                        Logger.getLogger(CustomerRemoteThreadImpl.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    isCalling = false;
                }

            }.start();
        }
        return null;
    }

    @Override
    public ArrayList<Item> getUserItems() throws Rejected, RemoteException {
        if(!isCalling) {
            new Thread() {

                @Override
                public void run() {
                    try {
                        RMIStoreClientHelper.customerRemoteObj.getLoader().setIndeterminate(true);
                        isCalling = true;
                        ArrayList<Item> returnItem = customerRemoteObj.getUserItems();
                        callback.doCallback(returnItem);
                        RMIStoreClientHelper.customerRemoteObj.getLoader().setIndeterminate(false);
                    } catch (Rejected | RemoteException ex) {
                        Logger.getLogger(CustomerRemoteThreadImpl.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    isCalling = false;
                }

            }.start();
        }
        return null;
    }

    @Override
    public double checkBalance() throws RemoteException {
        if(!isCalling) {
            new Thread() {

                @Override
                public void run() {
                    try {
                        RMIStoreClientHelper.customerRemoteObj.getLoader().setIndeterminate(true);
                        isCalling = true;
                        double returnValue = customerRemoteObj.checkBalance();
                        callback.doCallback(returnValue);
                        RMIStoreClientHelper.customerRemoteObj.getLoader().setIndeterminate(false);
                    } catch (RemoteException ex) {
                        Logger.getLogger(CustomerRemoteThreadImpl.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    isCalling = false;
                }

            }.start();
        }
        return 0;
    }
    
    @Override
    public boolean unRegister() throws Rejected, RemoteException {
        new Thread() {

            @Override
            public void run() {
                try {
                    boolean returnValue = customerRemoteObj.unRegister();
                    callback.doCallback(returnValue);
                } catch (Rejected | RemoteException ex) {
                    Logger.getLogger(CustomerRemoteThreadImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        }.start();
        return false;
    }

    public void setLoader(JProgressBar jProgressBar) {
        this.jProgressBar = jProgressBar;
    }
 
    public JProgressBar getLoader() {
        return this.jProgressBar;
    }
}
