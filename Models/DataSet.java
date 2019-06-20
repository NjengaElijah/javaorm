/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Connector.Models;

import Connector.DB;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author NE
 */
public class DataSet {

    private final ResultSet rs;
    Class  cl;
    public DataSet(ResultSet rs,Class cls) {
        this.rs = rs;
        this.cl = cls;
    }

    public  <T extends Object> ArrayList<T>  asArrayList() throws Exception {
        ArrayList data = new ArrayList<>();
        for (JsonElement e : DB.ResultSetToJSON(rs)) {
            data.add((T)new Gson().fromJson(e, cl));
        }
        return data;
    }
    public ResultSet asResultSet(){
        return  rs;
    }
    public DefaultTableModel asTableModel() throws Exception{
        return DB.RS2JTableModel(rs);
    }
    public Object[] rowData() throws Exception{
        return asTableModel().getDataVector().toArray();
    }
            
    
}
