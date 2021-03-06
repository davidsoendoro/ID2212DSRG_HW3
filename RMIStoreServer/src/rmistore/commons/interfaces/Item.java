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
public class Item implements Serializable{
    int itemId;
    int customerId;
    String name;
    double price;
    int quantity;
    
    public Item(int itemId,int customerId,String name,double price, int quantity){
        this.itemId=itemId;
        this.customerId=customerId;
        this.name=name;
        this.price=price; 
        this.quantity=quantity;
    }
    public int getCustomerId(){
        return customerId;
    }
    
    public String getName(){
        return name;
    }
    
    public double getPrice(){
        return price;
    }
    public int getItemId(){
        return itemId;
    }

    public int getQuantity() {
        return quantity;
    }
    
}
