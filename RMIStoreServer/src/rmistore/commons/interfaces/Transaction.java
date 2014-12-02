/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmistore.commons.interfaces;

import java.io.Serializable;

/**
 *
 * @author rohitgoyal
 */
public class Transaction implements Serializable{

    String itemName;
    float price;
    boolean isBuy;

    public Transaction(String itemName, float price, boolean isBuy) {
        this.itemName=itemName;
        this.price=price;
        this.isBuy=isBuy;
        
    }
}
