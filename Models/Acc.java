/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Connector.Models;

import Connector.DB;

/**
 *
 * @author NE
 */
public class Acc extends Model {
       
    public Acc(){
        super.setTable("jorm_accounts");
    }
    public String user_id;
    public double balance;
    public String names;

    @Override
    public String toString() {
        return "Acc{" + "user_id=" + user_id + ", balance=" + balance + ", names=" + names + '}';
    }
    

}
