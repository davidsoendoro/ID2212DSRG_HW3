/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmistoreserver.helper;

/**
 *
 * @author davidsoendoro
 */
public class RMIStoreServerHelper {

    public static final String RMIStoreName = "//localhost:1100/RMIStore";
    public static final String RMIBankName = "//localhost:1101/RMIStoreBank";
    public static final String RMIStoreDatasource = "rmistore";
    public static final String RMIStoreDbms = "derby";

    public static final String TABLE_CUSTOMER = "CUSTOMER";
    public static final String TABLE_ITEM = "ITEM";
    public static final String TABLE_WISH = "WISH";
    public static final String TABLE_TRANSACTION = "TRANS";
    
    //Customer table
    public static final String CREATE_CUSTOMER_TABLE = "CREATE TABLE " + rmistoreserver.helper.RMIStoreServerHelper.TABLE_CUSTOMER
            + " (id INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY "
            + "(START WITH 1, INCREMENT BY 1), name VARCHAR(128) UNIQUE, pass VARCHAR(64), "
            + "account_number INTEGER NOT NULL)";
    public static final String INSERT_IN_CUSTOMER_TABLE="INSERT INTO "+rmistoreserver.helper.RMIStoreServerHelper.TABLE_CUSTOMER + " (name, pass, account_number) VALUES (?, ?,?)";
    public static final String CHECKIF_NAME_EXISTS_IN_CUSTOMER_TABLE="SELECT * from "+ rmistoreserver.helper.RMIStoreServerHelper.TABLE_CUSTOMER+ " WHERE name = ?";
    public static final String RETRIEVE_CUSTOMER_PASS="SELECT pass FROM "+rmistoreserver.helper.RMIStoreServerHelper.TABLE_CUSTOMER+ " WHERE name = ?";
    public static final String GET_CUSTOMER_NAME="SELECT name FROM "+rmistoreserver.helper.RMIStoreServerHelper.TABLE_CUSTOMER+ " WHERE id = ?";
    public static final String GET_CUSTOMER_ACCOUNT_NUMBER="SELECT account_no FROM "+ rmistoreserver.helper.RMIStoreServerHelper.TABLE_CUSTOMER +" WHERE id = ?";
    public static final String GET_CUSTOMER_ID="SELECT id FROM "+rmistoreserver.helper.RMIStoreServerHelper.TABLE_CUSTOMER+ " WHERE name = ?";

    


    //item table
    public static final String CREATE_ITEM_TABLE = "CREATE TABLE " + rmistoreserver.helper.RMIStoreServerHelper.TABLE_ITEM
            + " (id INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY "
            + "(START WITH 1, INCREMENT BY 1), name VARCHAR(128), "
            + "price FLOAT NOT NULL, quantity INTEGER, seller_id INTEGER NOT NULL, FOREIGN KEY (seller_id) REFERENCES CUSTOMER(id))";
    public static final String INSERT_IN_ITEM_TABLE="INSERT INTO "+rmistoreserver.helper.RMIStoreServerHelper.TABLE_ITEM + " (name, price,quantity, seller_id) VALUES (?, ?, ?, ?)";
    public static final String CHECK_ITEM=" SELECT * FROM "+ rmistoreserver.helper.RMIStoreServerHelper.TABLE_ITEM+ " WHERE id = ?";
    public static final String BUY_ITEM="UPDATE "+rmistoreserver.helper.RMIStoreServerHelper.TABLE_ITEM+ " SET quantity = quantity-1 WHERE id = ? and quantity > 0";    
    public static final String GET_USER_ITEMS="SELECT * FROM "+rmistoreserver.helper.RMIStoreServerHelper.TABLE_ITEM+ " WHERE seller_id = ?";
    public static final String GET_ALL_ITEMS="SELECT * FROM "+rmistoreserver.helper.RMIStoreServerHelper.TABLE_ITEM;
    public static final String GET_OTHER_ITEMS="SELECT * FROM "+rmistoreserver.helper.RMIStoreServerHelper.TABLE_ITEM+ " WHERE seller_id != ?";
    public static final String REMOVE_ITEM="UPDATE "+ rmistoreserver.helper.RMIStoreServerHelper.TABLE_ITEM+ " SET quantity = 0 WHERE id = ?";


//wish table
    public static final String CREATE_WISH_TABLE = "CREATE TABLE " + rmistoreserver.helper.RMIStoreServerHelper.TABLE_WISH
            + " (id INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY "
            + "(START WITH 1, INCREMENT BY 1), name VARCHAR(128), "
            + "price FLOAT NOT NULL, wisher_id INTEGER NOT NULL, FOREIGN KEY (wisher_id) REFERENCES CUSTOMER(id))";
    public static final String INSERT_IN_WISH_TABLE="INSERT INTO "+rmistoreserver.helper.RMIStoreServerHelper.TABLE_WISH + " (name, price,wisher_id) VALUES (?, ?, ?)";
    public static final String RETRIEVE_WISH="SELECT * FROM "+rmistoreserver.helper.RMIStoreServerHelper.TABLE_WISH + " WHERE name = ?";
    
    
    //transaction table
    public static final String CREATE_TRANSACTION_TABLE = "CREATE TABLE " + rmistoreserver.helper.RMIStoreServerHelper.TABLE_TRANSACTION
            + " (id INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY "
            + "(START WITH 1, INCREMENT BY 1), customer_id INTEGER NOT NULL, "
            + "item_name VARCHAR(128), item_price FLOAT, isBuy BOOLEAN)";
    public static final String INSERT_IN_TRANSACTION_TABLE="INSERT INTO "+rmistoreserver.helper.RMIStoreServerHelper.TABLE_TRANSACTION + " (customer_id, item_name, item_price, isBuy) VALUES (?, ?, ?,?)";
    
}
