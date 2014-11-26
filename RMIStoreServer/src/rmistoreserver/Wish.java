/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmistoreserver;

/**
 *
 * @author rohitgoyal
 */
public class Wish {
    int customerId;
    double price;
    public Wish(int customerId,double price){
        this.customerId=customerId;
        this.price=price;
    }
    public  int getCustomerid(){
        return customerId;
    }
    public double getPrice(){
        return price;
    }
}
