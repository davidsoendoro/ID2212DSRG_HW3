/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmistoreclient.implementations;

import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import rmistore.commons.exceptions.Rejected;
import rmistore.commons.interfaces.Account;
import rmistoreclient.helper.RMIStoreClientHelper;
import rmistoreclient.interfaces.Callback;

/**
 *
 * @author davidsoendoro
 */
public class AccountThreadImpl implements Account {

    private final Account accountObj;
    private boolean isCalling = false;
    public Callback callback;
    private JProgressBar jProgressBar;
    
    public AccountThreadImpl(Account accountObj) {
        this.accountObj = accountObj;
    }

    public void setLoader(JProgressBar jProgressBar) {
        this.jProgressBar = jProgressBar;
    }
 
    public JProgressBar getLoader() {
        return this.jProgressBar;
    }

    @Override
    public double getBalance() throws RemoteException {
        if(!isCalling) {
            new Thread() {

                @Override
                public void run() {
                    try {
                        RMIStoreClientHelper.accountObj.getLoader().setIndeterminate(true);
                        isCalling = true;
                        double returnValue = accountObj.getBalance();
                        callback.doCallback(returnValue);
                        RMIStoreClientHelper.accountObj.getLoader().setIndeterminate(false);
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
    public void deposit(final float value) throws RemoteException, Rejected {
        if(!isCalling) {
            new Thread() {

                @Override
                public void run() {
                    try {
                        RMIStoreClientHelper.accountObj.getLoader().setIndeterminate(true);
                        isCalling = true;
                        accountObj.deposit(value);
                        callback.doCallback("deposit");
                        RMIStoreClientHelper.accountObj.getLoader().setIndeterminate(false);
                    } catch (Rejected | RemoteException ex) {
                        Logger.getLogger(CustomerRemoteThreadImpl.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    isCalling = false;
                }

            }.start();
        }
    }

    @Override
    public void withdraw(final float value) throws RemoteException, Rejected {
        if(!isCalling) {
            new Thread() {

                @Override
                public void run() {
                    try {
                        RMIStoreClientHelper.accountObj.getLoader().setIndeterminate(true);
                        isCalling = true;
                        accountObj.withdraw(value);
                        callback.doCallback("withdraw");
                        RMIStoreClientHelper.accountObj.getLoader().setIndeterminate(false);
                    } catch (Rejected ex) {
                        JOptionPane.showMessageDialog(RMIStoreClientHelper.currentFrame, 
                                ex.getMessage());
                        RMIStoreClientHelper.accountObj.getLoader().setIndeterminate(false);
                    } catch (RemoteException ex) {
                        Logger.getLogger(AccountThreadImpl.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    isCalling = false;
                }

            }.start();
        }
    }
}
