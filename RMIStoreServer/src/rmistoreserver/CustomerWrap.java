/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmistoreserver;
import rmistore.commons.interfaces.ClientRemote;
/**
 *
 * @author rohitgoyal
 */
public class CustomerWrap {
    String name;
    ClientRemote clientRemoteObj;
    
    public CustomerWrap(String name, ClientRemote clientRemoteObj){
        this.name=name;
        this.clientRemoteObj=clientRemoteObj;
    }
    public  ClientRemote getClientRemoteObj(){
        return clientRemoteObj;
    }
    public String getName(){
        return name;
    }
    
}
