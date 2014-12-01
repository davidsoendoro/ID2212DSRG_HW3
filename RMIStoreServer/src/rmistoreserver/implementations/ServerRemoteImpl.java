/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmistoreserver.implementations;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import rmistore.commons.exceptions.Rejected;
import rmistore.commons.interfaces.Account;
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

    Bank bankRMIObj;
    HashMap<Integer, CustomerWrap> customerHash = new HashMap<>();
    private PreparedStatement insertCustomerStatement;
    private PreparedStatement checkNameExistStatement;
    private PreparedStatement getCustomerPassStatement;
    private PreparedStatement getCustomerNameStatement;
    private PreparedStatement getCustomerAccountStatement;
    private PreparedStatement getCustomerIdStatement;
    private PreparedStatement insertItemStatement;
    private PreparedStatement buyItemStatement;
    private PreparedStatement checkItemStatement;
    private PreparedStatement getUserItemsStatement;
    private PreparedStatement getAllItemsStatement;
    private PreparedStatement getOtherItemsStatement;
    private PreparedStatement removeItemStatement;
    private PreparedStatement insertWishStatement;
    private PreparedStatement retrieveWisherStatement;

    private Map<String, Account> accounts = new HashMap<>();

    public ServerRemoteImpl(Bank bankRMIObj) throws RemoteException {
        try {
            this.bankRMIObj = bankRMIObj;
            Connection connection = createDatasource();
            prepareStatements(connection);
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(ServerRemoteImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Connection createDatasource() throws ClassNotFoundException, SQLException {
        Connection connection = getConnection();
        boolean exist = false;
        int tableNameColumn = 3;
        DatabaseMetaData dbm = connection.getMetaData();
        for (ResultSet rs = dbm.getTables(null, null, null, null); rs.next();) {
            if (rs.getString(tableNameColumn).equals(rmistoreserver.helper.RMIStoreServerHelper.TABLE_CUSTOMER)) {
                exist = true;
                rs.close();
                break;
            }
        }
        if (!exist) {
            Statement statement = connection.createStatement();
            statement.executeUpdate(rmistoreserver.helper.RMIStoreServerHelper.CREATE_CUSTOMER_TABLE);
            statement.executeUpdate(rmistoreserver.helper.RMIStoreServerHelper.CREATE_ITEM_TABLE);
            statement.executeUpdate(rmistoreserver.helper.RMIStoreServerHelper.CREATE_TRANSACTION_TABLE);
            statement.executeUpdate(rmistoreserver.helper.RMIStoreServerHelper.CREATE_WISH_TABLE);
        }
        return connection;
    }

    private Connection getConnection()
            throws ClassNotFoundException, SQLException {
        if (rmistoreserver.helper.RMIStoreServerHelper.RMIStoreDbms.equalsIgnoreCase("cloudscape")) {
            Class.forName("COM.cloudscape.core.RmiJdbcDriver");
            return DriverManager.getConnection(
                    "jdbc:cloudscape:rmi://localhost:1099/" + rmistoreserver.helper.RMIStoreServerHelper.RMIStoreDatasource
                    + ";create=true;");
        } else if (rmistoreserver.helper.RMIStoreServerHelper.RMIStoreDbms.equalsIgnoreCase("pointbase")) {
            Class.forName("com.pointbase.jdbc.jdbcUniversalDriver");
            return DriverManager.getConnection(
                    "jdbc:pointbase:server://localhost:9092/" + rmistoreserver.helper.RMIStoreServerHelper.RMIStoreDatasource
                    + ",new", "PBPUBLIC", "PBPUBLIC");
        } else if (rmistoreserver.helper.RMIStoreServerHelper.RMIStoreDbms.equalsIgnoreCase("derby")) {
            Class.forName("org.apache.derby.jdbc.ClientXADataSource");
            return DriverManager.getConnection(
                    "jdbc:derby://localhost:1527/" + rmistoreserver.helper.RMIStoreServerHelper.RMIStoreDatasource + ";create=true",
                    "rmistore", "nescafe");
        } else if (rmistoreserver.helper.RMIStoreServerHelper.RMIStoreDbms.equalsIgnoreCase("mysql")) {
            Class.forName("com.mysql.jdbc.Driver");
            return DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/" + rmistoreserver.helper.RMIStoreServerHelper.RMIStoreDatasource, "root", "javajava");
        } else {
            return null;
        }
    }

    private void prepareStatements(Connection connection) throws SQLException {
        insertCustomerStatement = connection.prepareStatement(rmistoreserver.helper.RMIStoreServerHelper.INSERT_IN_CUSTOMER_TABLE);
        checkNameExistStatement = connection.prepareStatement(rmistoreserver.helper.RMIStoreServerHelper.CHECKIF_NAME_EXISTS_IN_CUSTOMER_TABLE);
        getCustomerNameStatement = connection.prepareStatement(rmistoreserver.helper.RMIStoreServerHelper.GET_CUSTOMER_NAME);
        getCustomerPassStatement = connection.prepareStatement(rmistoreserver.helper.RMIStoreServerHelper.RETRIEVE_CUSTOMER_PASS);
        getCustomerIdStatement = connection.prepareStatement(rmistoreserver.helper.RMIStoreServerHelper.GET_CUSTOMER_ID);
        insertItemStatement = connection.prepareStatement(rmistoreserver.helper.RMIStoreServerHelper.INSERT_IN_ITEM_TABLE);
        buyItemStatement = connection.prepareStatement(rmistoreserver.helper.RMIStoreServerHelper.BUY_ITEM);
        checkItemStatement = connection.prepareStatement(rmistoreserver.helper.RMIStoreServerHelper.CHECK_ITEM);
        getUserItemsStatement = connection.prepareStatement(rmistoreserver.helper.RMIStoreServerHelper.GET_USER_ITEMS);
        getAllItemsStatement = connection.prepareStatement(rmistoreserver.helper.RMIStoreServerHelper.GET_ALL_ITEMS);
        getOtherItemsStatement = connection.prepareStatement(rmistoreserver.helper.RMIStoreServerHelper.GET_OTHER_ITEMS);
        removeItemStatement = connection.prepareStatement(rmistoreserver.helper.RMIStoreServerHelper.REMOVE_ITEM);
        insertWishStatement = connection.prepareStatement(rmistoreserver.helper.RMIStoreServerHelper.INSERT_IN_WISH_TABLE);
        retrieveWisherStatement = connection.prepareStatement(rmistoreserver.helper.RMIStoreServerHelper.RETRIEVE_WISH);

    }

    @Override
    public synchronized void register(String name, String pass)
            throws RemoteException, rmistore.commons.exceptions.Rejected {
        int id = 0;
        //check uniqueness of name
        try {
            checkNameExistStatement.setString(1, name);
            ResultSet rs = checkNameExistStatement.executeQuery();
            if (rs.next()) {
                throw new Rejected("Name already in use. Please choose another name.");
            } else {
                //open account
                Account acc = bankRMIObj.newAccount(name);
                //insert customer in DB
                insertCustomerStatement.setString(1, name);
                insertCustomerStatement.setString(2, pass);
                insertCustomerStatement.setLong(3, acc.getAccountNumber());
                int rowsChanged = insertCustomerStatement.executeUpdate();
                System.out.println("Customer " + name + " registered in db");
            }

            getCustomerIdStatement.setString(1, name);
            ResultSet customerIdRes = getCustomerIdStatement.executeQuery();
            while (customerIdRes.next()) {
                id = customerIdRes.getInt(1);
            }
//            customerHash.put(id, new CustomerWrap(name, clientRemote));
//            rmistore.commons.interfaces.CustomerRemote client = new CustomerRemoteImpl(id, this);
//            clientRemote.receiveMessage("Welcome!");
//            return client;
        } catch (SQLException sq) {

//            return null;
        }
    }

    public synchronized boolean unregister(int customerId) {
        customerHash.remove(customerId);
        return true;
    }

    public synchronized rmistore.commons.interfaces.CustomerRemote login(String name, String pass,
            rmistore.commons.interfaces.ClientRemote clientRemote)
            throws RemoteException, rmistore.commons.exceptions.Rejected {
        try {
            //check username and password
            getCustomerPassStatement.setString(1, name);
            ResultSet rs = getCustomerPassStatement.executeQuery();
            while (rs.next()) {
                if (!rs.getString("pass").equals(pass)) {
                    throw new Rejected("Username or password is incorrect.");
                }
            }
            int id = 0;
            getCustomerIdStatement.setString(1, name);
            ResultSet customerIdRes = getCustomerIdStatement.executeQuery();
            while (customerIdRes.next()) {
                id = customerIdRes.getInt(1);
            }
            customerHash.put(id, new CustomerWrap(name, clientRemote));
            rmistore.commons.interfaces.CustomerRemote client = new CustomerRemoteImpl(id, this);
            clientRemote.receiveMessage("Welcome!");
            return client;
        } catch (SQLException ex) {
            Logger.getLogger(ServerRemoteImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public synchronized boolean addItem(String itemName, Float price, int quantity, int seller_id) {
        try {
            try {
                insertItemStatement.setString(1, itemName);
                insertItemStatement.setFloat(2, price);
                insertItemStatement.setInt(3, quantity);
                insertItemStatement.setInt(4, seller_id);
                int rowschanged = insertItemStatement.executeUpdate();
                if (rowschanged == 1) {
                    System.out.println("added");
                } else {
                    System.out.println("not added");
                }
            } catch (SQLException sq) {

            }
            //notify wishList customers
            retrieveWisherStatement.setString(1, itemName);
            ResultSet rs = retrieveWisherStatement.executeQuery();
            while (rs.next()) {
                if (itemName.contains(rs.getString("name"))) {
                    if (rs.getFloat("price") <= price) {
                        Thread notificationThread = new NotificationThread(rs.getInt("wisher_id"),
                                rs.getString("name"), rs.getFloat("price"));
                        notificationThread.start();
                    }
                }
            }
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ServerRemoteImpl.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public synchronized boolean removeItem(int itemId) {
        try {
            //itemHash.remove(itemId);
            removeItemStatement.setInt(1, itemId);
            int rowschanged = removeItemStatement.executeUpdate();
            return rowschanged == 1;
        } catch (SQLException ex) {
            Logger.getLogger(ServerRemoteImpl.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public synchronized boolean buyItem(int customerId, int itemId) {
        float itemPrice = 0;
        int seller_id = 0;
        String itemName = null;
        try {
            checkItemStatement.setInt(1, itemId);
            ResultSet rs = checkItemStatement.executeQuery();
            while (rs.next()) {
                itemPrice = rs.getInt("price");
                seller_id = rs.getInt("seller_id");
                itemName = rs.getString("name");
            }
            // the thread function requires these variable to be 'final' :(
            final int seller = seller_id;
            final String name_of_item = itemName;
            if (bankRMIObj.getAccount(customerHash.get(customerId).getName()).getBalance() >= itemPrice) {
                //final Item item = itemHash.get(itemId);
                buyItemStatement.setInt(1, itemId);
                int rowsChanged = buyItemStatement.executeUpdate();

                //credit to seller and debit to buyer
                Thread sellNotificationThread = new Thread() {

                    @Override
                    public void run() {

                        try {
                            ServerRemoteImpl.this.getClientObj(seller).receiveMessage("Your item " + name_of_item + " sold. Money credited to your account.");
                        } catch (RemoteException ex) {
                            Logger.getLogger(ServerRemoteImpl.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                };
                sellNotificationThread.start();
                try {
                    bankRMIObj.getAccount(customerHash.get(seller_id).getName()).deposit(itemPrice);
                } catch (Rejected r) {
                    this.getClientObj(seller_id).receiveMessage("Exception: " + r);
                }
                try {
                    bankRMIObj.getAccount(customerHash.get(customerId).getName()).withdraw(itemPrice);
                } catch (Rejected r) {
                    this.getClientObj(customerId).receiveMessage("Exception: " + r);
                }
                return true;
            } else {
                this.getClientObj(customerId).receiveMessage("Insufficient balance");
                return false;
            }
        } catch (SQLException | RemoteException ex) {
            System.out.println("Remote Exception: " + ex);

            return false;
        }
    }

    public ClientRemote getClientObj(int customerId) {
        return customerHash.get(customerId).getClientRemoteObj();
    }

    public ArrayList<Item> getUserItemsFromHash(int customerId) {
        try {
            ArrayList<Item> items = new ArrayList<>();
            getUserItemsStatement.setInt(1, customerId);
            ResultSet rs = getUserItemsStatement.executeQuery();
            while (rs.next()) {
                Item i = new Item(rs.getInt("id"), rs.getInt("seller_id"), rs.getString("name"), rs.getFloat("price"));
                items.add(i);
            }
            return items;
        } catch (SQLException ex) {
            Logger.getLogger(ServerRemoteImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public ArrayList<Item> getAllItemsFromHash() {
        try {
            ArrayList<Item> items = new ArrayList<>();
            ResultSet rs = getUserItemsStatement.executeQuery();
            while (rs.next()) {
                Item i = new Item(rs.getInt("id"), rs.getInt("seller_id"), rs.getString("name"), rs.getFloat("price"));
                items.add(i);
            }
            return items;
        } catch (SQLException ex) {
            Logger.getLogger(ServerRemoteImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public void wishItemForCustomer(String name, int customerId, float price) {
        try {
            insertWishStatement.setString(1, name);
            insertWishStatement.setFloat(2, price);
            insertWishStatement.setInt(3, customerId);
            int rowsChanged = insertWishStatement.executeUpdate();
            if (rowsChanged == 1) {
                try {
                    ServerRemoteImpl.this.getClientObj(customerId).receiveMessage(
                            "Your wish for " + name + " for $" + price + " has been added!");
                } catch (RemoteException ex) {
                    Logger.getLogger(ServerRemoteImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServerRemoteImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    ArrayList<Item> getOtherItems(int customerId) {
        try {
            ArrayList<Item> items = new ArrayList<>();
            getOtherItemsStatement.setInt(1, customerId);
            ResultSet rs = getUserItemsStatement.executeQuery();
            while (rs.next()) {
                Item i = new Item(rs.getInt("id"), rs.getInt("seller_id"), rs.getString("name"), rs.getFloat("price"));
                items.add(i);
            }
            return items;
        } catch (SQLException ex) {
            Logger.getLogger(ServerRemoteImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public double checkCustomerBalance(int customerId) {
        try {
            return bankRMIObj.getAccount(customerHash.get(customerId).getName()).getBalance();
        } catch (RemoteException r) {
            System.out.println("Exception: " + r);
        }
        return 0;
    }

    private void ArrayList() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private class NotificationThread extends Thread {

        private final int wisherId;
        private final String itemName;
        private final float itemPrice;

        public NotificationThread(int wisherId, String itemName, float itemPrice) {
            this.wisherId = wisherId;
            this.itemName = itemName;
            this.itemPrice = itemPrice;
        }

        @Override
        public void run() {
            try {
                ServerRemoteImpl.this.getClientObj(wisherId).
                        receiveMessage("Item " + itemName + " available for price " + itemPrice);
            } catch (RemoteException r) {
            }
        }

    }
}
