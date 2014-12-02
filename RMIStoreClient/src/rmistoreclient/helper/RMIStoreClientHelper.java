/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmistoreclient.helper;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.swing.JFrame;
import rmistore.commons.interfaces.ClientRemote;
import rmistoreclient.implementations.AccountThreadImpl;
import rmistoreclient.implementations.CustomerRemoteThreadImpl;

/**
 *
 * @author davidsoendoro
 */
public class RMIStoreClientHelper {
    
    public static final String RMIStoreName = "//localhost:1100/RMIStore";
    public static final String RMIBankName = "//localhost:1101/RMIStoreBank";
    
    // SERVER REMOTE COMMAND
    public final static int STARTRMI_COMMAND = 0;
    public final static int REGISTER_COMMAND = 1;
 
    // CUSTOMER REMOTE COMMAND
    public final static int SELLITEM_COMMAND = 10;
 
    public static CustomerRemoteThreadImpl customerRemoteObj;
    public static ClientRemote clientRemoteObj;
    public static AccountThreadImpl accountObj;
    public static JFrame currentFrame;
    
    public static String doMd5(String input) {

        String md5 = null;

        if (null == input) {
            return null;
        }

        try {

            //Create MessageDigest object for MD5
            MessageDigest digest = MessageDigest.getInstance("MD5");

            //Update input string in message digest
            digest.update(input.getBytes(), 0, input.length());

            //Converts message digest value in base 16 (hex) 
            md5 = new BigInteger(1, digest.digest()).toString(16);

        } catch (NoSuchAlgorithmException e) {
        }
        return md5;
    }

}
