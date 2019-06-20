/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Connector.Models;

/**
 *
 * @author NE
 */
public class User extends Model {
    public String names,nat_id;

    public User() {
        this.table = "jorm_users";
    }
}
