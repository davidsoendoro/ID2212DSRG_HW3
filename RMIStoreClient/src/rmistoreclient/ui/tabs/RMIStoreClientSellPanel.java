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
import static javax.swing.JOptionPane.showMessageDialog;
import rmistore.commons.interfaces.Item;
import rmistoreclient.helper.RMIStoreClientHelper;
import rmistoreclient.interfaces.Callback;
import rmistoreclient.ui.tabs.items.RMIStoreClientSellItem;

/**
 *
 * @author davidsoendoro
 */
public class RMIStoreClientSellPanel extends RMIStoreClientGenericTab implements Callback{

    /**
     * Creates new form RMIStoreClientBuyPanel
     */
    public RMIStoreClientSellPanel() {
        tabName = "Sell";
        
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

        jScrollPaneItems = new javax.swing.JScrollPane();
        jPanelItems = new javax.swing.JPanel();
        jLabelItems = new javax.swing.JLabel();
        jSeparator = new javax.swing.JSeparator();
        jLabelItemName = new javax.swing.JLabel();
        jLabelItemPrice = new javax.swing.JLabel();
        jTextFieldItemName = new javax.swing.JTextField();
        jTextFieldItemPrice = new javax.swing.JTextField();
        jButtonAdd = new javax.swing.JButton();
        jLabelCurrency = new javax.swing.JLabel();

        jPanelItems.setLayout(new java.awt.GridLayout(0, 1));
        jScrollPaneItems.setViewportView(jPanelItems);

        jLabelItems.setText("Your Shop");

        jLabelItemName.setText("Item Name");

        jLabelItemPrice.setText("Item Price");

        jButtonAdd.setText("Add Item");
        jButtonAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddActionPerformed(evt);
            }
        });

        jLabelCurrency.setText("USD");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPaneItems)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabelItemPrice)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextFieldItemPrice))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabelItemName)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextFieldItemName, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelCurrency)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 61, Short.MAX_VALUE)
                        .addComponent(jButtonAdd))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabelItems)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jSeparator))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelItems)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPaneItems, javax.swing.GroupLayout.DEFAULT_SIZE, 182, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonAdd, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelItemName)
                            .addComponent(jTextFieldItemName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelItemPrice)
                            .addComponent(jTextFieldItemPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelCurrency))))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddActionPerformed
        String alert = "";
        if(jTextFieldItemName.getText().length() <= 0) {
            alert += "Name must not be empty!\n";
        }
        try {
            Double.valueOf(jTextFieldItemPrice.getText());
        }
        catch (NumberFormatException ex) {
            alert += "Put correct number format!";
        }
        if(alert.length() > 0) {
            showMessageDialog(null, alert);  
        }
        else {
            try {
                RMIStoreClientHelper.customerRemoteObj.sellItem(jTextFieldItemName.getText(),
                        Double.valueOf(jTextFieldItemPrice.getText()));
            } catch (RemoteException ex) {
                Logger.getLogger(RMIStoreClientSellPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }        
    }//GEN-LAST:event_jButtonAddActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAdd;
    private javax.swing.JLabel jLabelCurrency;
    private javax.swing.JLabel jLabelItemName;
    private javax.swing.JLabel jLabelItemPrice;
    private javax.swing.JLabel jLabelItems;
    private javax.swing.JPanel jPanelItems;
    private javax.swing.JScrollPane jScrollPaneItems;
    private javax.swing.JSeparator jSeparator;
    private javax.swing.JTextField jTextFieldItemName;
    private javax.swing.JTextField jTextFieldItemPrice;
    // End of variables declaration//GEN-END:variables

    public void refreshItemList() {
        try {
            RMIStoreClientHelper.customerRemoteObj.callback = this;
            RMIStoreClientHelper.customerRemoteObj.getUserItems();          
            System.out.println("Called");
        } catch (RemoteException ex) {
            Logger.getLogger(RMIStoreClientSellPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public synchronized void doCallback(Object arguments) {
        if(arguments != null && arguments.getClass() == ArrayList.class) {
            System.out.println("Listed");
            ArrayList<Item> items = (ArrayList<Item>) arguments;
            
            jPanelItems.removeAll();
            for(Item item : items) {
                jPanelItems.add(new RMIStoreClientSellItem(item));
            }
            
            jPanelItems.revalidate();
            jPanelItems.repaint();
        }
        else if(arguments != null && arguments.getClass() == String.class) {
            String argumentsString = (String) arguments;
            if(argumentsString.equals("sellItem")) {
                System.out.println("Calling");
                refreshItemList();
            }
        }
    }
}