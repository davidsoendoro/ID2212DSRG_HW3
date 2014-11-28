/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmistorebank.helper;

/**
 *
 * @author davidsoendoro
 */
public class RMIStoreBankHelper {
    public static final String RMIBankName = "//localhost:1101/RMIStoreBank";
    
    //Database Helper
    public static final String RMIDatasource = "rmibank";
    public static final String RMIDbms = "derby";

    public static final String TABLE_ACCOUNT_NAME = "ACCOUNT";
    
    /**
     * SQL Commands -- CREATE ACCOUNT
     * 
     * CREATE table ACCOUNT (
     * ID          INTEGER NOT NULL 
     *             PRIMARY KEY GENERATED ALWAYS AS IDENTITY 
     *             (START WITH 1, INCREMENT BY 1),
     * NAME        VARCHAR(128) UNIQUE NOT NULL, 
     * BALANCE     NUMERIC NOT NULL )
     */
}
