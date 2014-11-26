/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmistoreclient.ui.tabs;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import rmistore.commons.interfaces.Item;
import rmistoreclient.helper.RMIStoreClientHelper;
import rmistoreclient.interfaces.Callback;
import rmistoreclient.ui.tabs.items.RMIStoreClientBuyItem;

/**
 *
 * @author davidsoendoro
 */
public class RMIStoreClientBuyPanel extends RMIStoreClientGenericTab implements Callback {

    /**
     * Creates new form RMIStoreClientBuyPanel
     */
    public RMIStoreClientBuyPanel() {
        tabName = "Buy";
        
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPaneBuy = new javax.swing.JScrollPane();
        jPanelBuy = new javax.swing.JPanel();

        jPanelBuy.setLayout(new java.awt.GridLayout(0, 1));
        jScrollPaneBuy.setViewportView(jPanelBuy);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPaneBuy, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPaneBuy, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanelBuy;
    private javax.swing.JScrollPane jScrollPaneBuy;
    // End of variables declaration//GEN-END:variables

    @Override
    public void doCallback(Object arguments) {
        if(arguments != null && arguments.getClass() == ArrayList.class) {
            ArrayList<Item> items = (ArrayList<Item>) arguments;
            
            jPanelBuy.removeAll();
            for(Item item : items) {
                jPanelBuy.add(new RMIStoreClientBuyItem(item));
            }
            
            jPanelBuy.revalidate();
            jPanelBuy.repaint();
        }
    }

    public void refreshItemList() {
        try {
            RMIStoreClientHelper.customerRemoteObj.callback = this;
            RMIStoreClientHelper.customerRemoteObj.getOtherItems();
        } catch (RemoteException ex) {
            Logger.getLogger(RMIStoreClientBuyPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
