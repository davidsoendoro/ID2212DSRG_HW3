/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmistorebank;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.logging.Level;
import java.util.logging.Logger;
import rmistore.commons.interfaces.Bank;
import rmistore.commons.interfaces.BankImpl;
import rmistorebank.helper.RMIStoreBankHelper;

/**
 *
 * @author davidsoendoro
 */
public class RMIStoreBank extends Thread {

    public RMIStoreBank(String[] args){
        
        try {
            Runtime.getRuntime().exec("rmiregistry -J-Djava.rmi.server.useCodebaseOnly=false");
            LocateRegistry.createRegistry(1101);
            LocateRegistry.getRegistry();
        } catch (IOException ex) {
            System.out.println("Port already opened!");
        }

        try {            
            Bank rmiBankObj = new BankImpl(RMIStoreBankHelper.RMIBankName);
            Naming.rebind(RMIStoreBankHelper.RMIBankName, rmiBankObj);
        } catch (RemoteException | MalformedURLException ex) {
            Logger.getLogger(RMIStoreBank.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        new RMIStoreBank(args).run();
    }
    
}
