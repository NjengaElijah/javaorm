/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Connector;

import Connector.Models.Acc;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author NE
 */
public class Main {

    public static void main(String[] args) {
        //<editor-fold defaultstate="collapsed" desc="comment">
//        try {
//            DB.setDATABASE("shop");
//            DB.ConnectDb();
////            HashMap<Object, Object> data = new HashMap<>();
////            data.put("age", "5000");
//
////            DB.update("test", data, "id = 1");
////            ArrayList<Object[]> datai = new ArrayList();
////            datai.add(new Object[]{"njenga", "21"});
////            datai.add(new Object[]{"siwa", "22"});
//// DB.insert("test", new Object[]{"names", "age"}, new Object[][]{{"Kangogo", "22"}, {"Sese", "22"}});
//System.out.println(ResultSetToJSON(select("select * from staff_details")));
//
////            Collection<Acc> list = (Collection<Acc>)parseResultSetToCollection(select("select * from test"),Acc.class);
//
///*System.err.println("Size:" + list.size());
//list.forEach((e) -> {
//System.out.println(e.toString());
//});*/
////            list.forEach((acc)->{
////
////            });
//        } catch (Exception ex) {
//            // System.err.println("Error :: \n" + ex);
//        }
//</editor-fold>
        DB.setDATABASE("test");
        
        Acc ac = new Acc();
        try {
            List<Acc> accs = ac.find("").asArrayList();
            System.out.println("" + accs.size());
            System.out.println(ac.toString());
            //ac.save();
        } catch (Exception ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
