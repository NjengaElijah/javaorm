# javaorm
Java ORM using googles GSON.jar 


```java

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Connector.Models;

import Connector.DB;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author NE
 */
public class Model {

    public String id, create_at, update_at;
    protected String table = getClass().getSimpleName().toLowerCase() + "s";
    protected String foreign_key = getClass().getSimpleName().toLowerCase() + "_id";
    private final List<Field> fields = Arrays.asList(getClass().getFields());
    private final List<Method> methods = Arrays.asList(getClass().getMethods());
    private HashMap<Object, Object> dataMap = new HashMap<>();
    private final Class cl = getClass();

    public Model() {
    }

    //return the table
    public String getTable() {
        return table.toLowerCase();
    }

    public void setTable(String table) {
        this.table = table;
    }

    //get savable fields in that class i.e remove id & stamps
    public List<Field> getSavableFields() throws IllegalArgumentException, IllegalAccessException {
        List<Field> mFields = new ArrayList<>();
        for (Field mField : fields) {
            if (mField.getName().equals("id") || mField.getName().equals("update_at") || mField.getName().equals("create_at")) {
                continue;
            }
            mFields.add(mField);
        }
        return mFields;
    }

    public HashMap<Object, Object> getDataMap() throws Exception {
        dataMap = new HashMap<>();
        for (Field savableField : getSavableFields()) {
            dataMap.put(savableField.getName(), savableField.get(this));
        }
        return dataMap;
    }

    public void save() throws Exception {
         DB.insert(getTable(), getDataMap());
    }

    public void update() throws Exception {
        DB.update(getTable(), getDataMap(), " `id` = "+id);
    }

    public boolean delete() throws Exception {
        boolean flag = DB.deleteI("DELETE FROM `" + getTable() + "` WHERE `id` = " + id, null);
        return flag;
    }

    public <T extends Object> T byId(Object id) throws Exception {
        try {
            return (T) find("WHERE `id` = " + id).asArrayList().get(0);
        } catch (Exception exception) {
            if (exception instanceof IndexOutOfBoundsException) {
                throw new Exception("No record of '" + getClass().getSimpleName() + "' Object was found ! ", exception);
            }
            throw exception;
        }

    }

    public DataSet all() throws Exception {
        return find("");
    }

    public DataSet find(String where) throws Exception {
        return new DataSet(DB.select("SELECT * FROM `" + getTable() + "` " + where), getClass());
    }

    public DataSet findAll(Class cl) throws Exception {
        String joinTable = ((Model) cl.newInstance()).getTable();
       return new DataSet(DB.select("SELECT * FROM `" + joinTable + "` WHERE `" + foreign_key + "` = " + id), cl);

    }
}

```
