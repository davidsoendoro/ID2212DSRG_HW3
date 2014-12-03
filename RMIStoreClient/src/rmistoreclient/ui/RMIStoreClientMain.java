/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmistoreclient.ui;

import javax.swing.JOptionPane;
import rmistoreclient.helper.RMIStoreClientHelper;
import rmistoreclient.implementations.ClientRemoteImpl;
import rmistoreclient.ui.tabs.RMIStoreClientAccountPanel;
import rmistoreclient.ui.tabs.RMIStoreClientBuyPanel;
import rmistoreclient.ui.tabs.RMIStoreClientHistoryPanel;
import rmistoreclient.ui.tabs.RMIStoreClientSellPanel;

/**
 *
 * @author davidsoendoro
 */
public class RMIStoreClientMain extends javax.swing.JFrame {

    private javax.swing.JFrame caller;
    private String username;
    
    private RMIStoreClientBuyPanel buyTab;
    private RMIStoreClientSellPanel sellTab;
    private RMIStoreClientAccountPanel accountTab;
    private RMIStoreClientHistoryPanel historyTab;
    
    /**
     * Creates new form RMIStoreClientMain
     */
    public RMIStoreClientMain() {
        initComponents();
        initTabs();        
    }
    
    public RMIStoreClientMain(javax.swing.JFrame caller, String username) {
        this.caller = caller;
        this.caller.setVisible(false);
        
        this.username = username;
        
        initComponents();
        initGUI();
        initTabs();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPaneMain = new javax.swing.JTabbedPane();
        jLabelGreetings = new javax.swing.JLabel();
        jProgressBar = new javax.swing.JProgressBar();
        jMenuBar = new javax.swing.JMenuBar();
        jMenuLogout = new javax.swing.JMenu();
        jMenuAbout = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTabbedPaneMain.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPaneMainStateChanged(evt);
            }
        });

        jLabelGreetings.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jLabelGreetings.setText("Hi, !");

        jMenuLogout.setText("Logout");
        jMenuLogout.addMenuListener(new javax.swing.event.MenuListener() {
            public void menuSelected(javax.swing.event.MenuEvent evt) {
                jMenuLogoutMenuSelected(evt);
            }
            public void menuDeselected(javax.swing.event.MenuEvent evt) {
            }
            public void menuCanceled(javax.swing.event.MenuEvent evt) {
            }
        });
        jMenuBar.add(jMenuLogout);

        jMenuAbout.setText("About");
        jMenuAbout.addMenuListener(new javax.swing.event.MenuListener() {
            public void menuSelected(javax.swing.event.MenuEvent evt) {
                jMenuAboutMenuSelected(evt);
            }
            public void menuDeselected(javax.swing.event.MenuEvent evt) {
            }
            public void menuCanceled(javax.swing.event.MenuEvent evt) {
            }
        });
        jMenuBar.add(jMenuAbout);

        setJMenuBar(jMenuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabelGreetings, javax.swing.GroupLayout.DEFAULT_SIZE, 420, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jProgressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jTabbedPaneMain, javax.swing.GroupLayout.DEFAULT_SIZE, 580, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jProgressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelGreetings, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(283, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(40, 40, 40)
                    .addComponent(jTabbedPaneMain, javax.swing.GroupLayout.DEFAULT_SIZE, 285, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTabbedPaneMainStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPaneMainStateChanged
        if(jTabbedPaneMain.getSelectedComponent() == buyTab) {
            buyTab.refreshItemList();
        }
        else if(jTabbedPaneMain.getSelectedComponent() == sellTab) {
            sellTab.refreshItemList();
        }
        else if(jTabbedPaneMain.getSelectedComponent() == accountTab) {
            accountTab.refreshBalance();
        }
        else if(jTabbedPaneMain.getSelectedComponent() == historyTab) {
            historyTab.refreshHistory();
        }
    }//GEN-LAST:event_jTabbedPaneMainStateChanged

    private void jMenuLogoutMenuSelected(javax.swing.event.MenuEvent evt) {//GEN-FIRST:event_jMenuLogoutMenuSelected
        // Send Logout message:
        
        // If logout success send this:
        this.setVisible(false);
        caller.setVisible(true);
        
        // If logout failed send this:
//        JOptionPane.showMessageDialog(RMIStoreClientMain.this, "???");
    }//GEN-LAST:event_jMenuLogoutMenuSelected

    private void jMenuAboutMenuSelected(javax.swing.event.MenuEvent evt) {//GEN-FIRST:event_jMenuAboutMenuSelected
        JOptionPane.showMessageDialog(RMIStoreClientMain.this, "RMIStore is a marketplace created with Java RMI.\n"
                + "Created by David Soendoro <soendoro@kth.se> and Rohit Goyal <rohitg@kth.se>\n"
                + "KTH 2014");
    }//GEN-LAST:event_jMenuAboutMenuSelected

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(RMIStoreClientMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(RMIStoreClientMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RMIStoreClientMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RMIStoreClientMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                RMIStoreClientMain main = new RMIStoreClientMain();
                main.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabelGreetings;
    private javax.swing.JMenu jMenuAbout;
    private javax.swing.JMenuBar jMenuBar;
    private javax.swing.JMenu jMenuLogout;
    private javax.swing.JProgressBar jProgressBar;
    private javax.swing.JTabbedPane jTabbedPaneMain;
    // End of variables declaration//GEN-END:variables

    private void initGUI() {
        jLabelGreetings.setText("Hi, " + username + "!");
    }
    
    private void initTabs() {
        RMIStoreClientHelper.customerRemoteObj.setLoader(jProgressBar);
        RMIStoreClientHelper.accountObj.setLoader(jProgressBar);

        // Init buy tab
        buyTab = new RMIStoreClientBuyPanel();
        jTabbedPaneMain.add(RMIStoreClientBuyPanel.tabName, buyTab);

        // Init sell tab
        sellTab = new RMIStoreClientSellPanel();
        jTabbedPaneMain.add(RMIStoreClientSellPanel.tabName, sellTab);

        // Init account tab
        accountTab = new RMIStoreClientAccountPanel();
        jTabbedPaneMain.add(RMIStoreClientAccountPanel.tabName, accountTab);
        
        // Init history tab
        historyTab = new RMIStoreClientHistoryPanel();
        jTabbedPaneMain.add(RMIStoreClientHistoryPanel.tabName, historyTab);
        
        ClientRemoteImpl clientRemoteImpl = (ClientRemoteImpl) 
                RMIStoreClientHelper.clientRemoteObj;
        clientRemoteImpl.setBalanceDisplayer(accountTab);
        
        RMIStoreClientHelper.currentFrame = this;
    }

}
