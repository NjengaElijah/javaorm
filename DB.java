package Connector;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Njenga Elijah
 */
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.swing.table.DefaultTableModel;
import com.google.gson.*;
import com.mysql.jdbc.MySQLConnection;
import java.util.Arrays;

public class DB {

    static MySQLConnection conn = null;
    private static String HOST = "localhost", USER = "root", PASSWORD, DATABASE, PORT = "3306";

    public static MySQLConnection ConnectDb() throws Exception {
        if (conn == null) {
            if (getDATABASE() != null) {
                conn = (MySQLConnection) DriverManager.getConnection("jdbc:mysql://" + getHOST() + ":" + getPORT() + "/" + getDATABASE(), getUSER(), getPASSWORD());
            } else {
                conn = (MySQLConnection) DriverManager.getConnection("jdbc:mysql://" + getHOST() + ":" + getPORT(), getUSER(), getPASSWORD());
            }
        }
        return conn;

    }

    public static String getHOST() {
        return HOST;
    }

    public static void setHOST(String HOST) {
        DB.HOST = HOST;
    }

    public static String getDATABASE() {
        return DATABASE;
    }

    public static void setDATABASE(String DATABASE) {
        DB.DATABASE = DATABASE;
    }

    public static String getUSER() {
        return USER;
    }

    public static void setUSER(String USER) {
        DB.USER = USER;
    }

    public static String getPASSWORD() {
        return PASSWORD;
    }

    public static void setPASSWORD(String PASSWORD) {
        DB.PASSWORD = PASSWORD;
    }

    public static String getPORT() {
        return PORT;
    }

    public static void setPORT(String PORT) {
        DB.PORT = PORT;
    }

    public static void nonQuery(String querry) throws Exception {

        Statement st = ConnectDb().createStatement();
        st.execute(querry);

    }

    public static boolean nonQueryI(String querry, Object[] params) throws Exception {

        PreparedStatement st = ConnectDb().prepareStatement(querry);
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                Object param = params[i];
                st.setObject((i + 1), param);
            }
        }
        return st.execute();
    }

    @Deprecated
    public static void insert(String querry) throws Exception {

        nonQuery(querry);
    }

    public static int insertI(String querry, Object[] params) throws Exception {
        PreparedStatement st = ConnectDb().prepareStatement(querry,Statement.RETURN_GENERATED_KEYS);
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                Object param = params[i];
                st.setObject((i + 1), param);
            }
        }
        return st.executeUpdate();
    }

    @Deprecated
    public static void update(String querry) throws Exception {

        nonQuery(querry);
    }

    public static void updateI(String querry, Object[] params) throws Exception {

        nonQueryI(querry, params);
    }

    @Deprecated
    public static void delete(String querry) throws Exception {

        nonQuery(querry);
    }

    public static boolean deleteI(String querry, Object[] params) throws Exception {

        return nonQueryI(querry, params);
    }

    public static ResultSet select(String querry) throws Exception {
        return ConnectDb().prepareStatement(querry).executeQuery();

    }

    public static DefaultTableModel RS2JTableModel(ResultSet rs) throws Exception {
        return RtM(rs);
    }

    public static DefaultTableModel Querry2JTableModel(String querry) throws Exception {
        return RtM(select(querry));
    }

    private static DefaultTableModel RtM(ResultSet rs) throws Exception {
        //GET THE COLUMN COUNT FROM METADATA.WE'LL NEED IT TO CREATE NO OF COLUMNS AND GENERATE COLUMN TITLES
        int columnCount = rs.getMetaData().getColumnCount();
        //Logger.getLogger("RStoModel").log(Level.WARNING, "" + rs.getMetaData().toString());
        String[] titles = new String[columnCount];
        for (int c = 0; c < columnCount; c++) {
            titles[c] = rs.getMetaData().getColumnLabel(c + 1);
           // System.err.println("Col : " + rs.getMetaData().getColumnLabel(c+1));
        }

        //r/c
        //Object[][] rows = new Object[students.size()][4];
        ArrayList<Object[]> objects = new ArrayList();
        int rCount = 0;
        //loop rows
        while (rs.next()) {
            objects.add(new Object[columnCount]);
            //loop columns
            for (int i = 0; i < columnCount; i++) {
                objects.get(rCount)[i] = rs.getObject(i + 1);
            }
            rCount++;
        }
        rs.close();

        Object[][] rObjectses = new Object[rCount][columnCount];

        for (int r = 0; r < rObjectses.length; r++) {
            rObjectses[r] = objects.get(r);
        }
        return new DefaultTableModel(rObjectses, titles);
    }

    //improved methodscollection
    /**
     *
     * @param tableName
     * @param data
     * @param whereEquation
     * @throws java.lang.Exception
     */
    public static void update(String tableName, HashMap<Object, Object> data, String whereEquation) throws Exception {
        String updPartQuery = " SET ";

        //we need to add the ? as value for the key in map
        updPartQuery
                = data
                        .entrySet()
                        .stream()
                        .map((entry) -> "`" + entry.getKey().toString() + "`" + " = ? ,")
                        .reduce(updPartQuery, String::concat);

        updPartQuery = updPartQuery.substring(0, updPartQuery.length() - 1);
        updPartQuery = " UPDATE `" + tableName + "` " + updPartQuery + " WHERE " + whereEquation;
        
        try {
            nonQueryI(updPartQuery, data.values().toArray());
        } catch (Exception exception) {
            throw new Exception("Update :: " + exception);
        }
        //System.err.println(updPartQuery);

    }

    /**
     * LIMITATIONS this works for only one insert record
     *
     * @param tableName
     * @param data
     * @throws java.lang.Exception
     */
    public static int insert(String tableName, HashMap<Object, Object> data) throws Exception {
        String insertQuery = "";
        String columns = "(", values = "(";

        for (Map.Entry<Object, Object> entry : data.entrySet()) {
            columns += "`" + entry.getKey().toString() + "`,";
            values += " ?,";
        }
        columns = columns.substring(0, columns.length() - 1) + ") ";
        values = values.substring(0, values.length() - 1) + ") ";
        //insertQuery = insertQuery.substring(0, insertQuery.length() - 1);
        insertQuery = "INSERT INTO `" + tableName + "` " + columns + " VALUES " + values;
        //System.err.println(insertQuery);
        Object[] params =  data.values().toArray();
        //execute here other than from nonqueryi to get tha last insert id
        PreparedStatement st = ConnectDb().prepareStatement(insertQuery,Statement.RETURN_GENERATED_KEYS);
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                Object param = params[i];
                st.setObject((i + 1), param);
            }
        }
        return st.executeUpdate();
        
    }

    public static void insert(String tableName, Object[] columns, Object[][] data) throws Exception {
        //Map the data from (Object[][]) to ArrayList<Object[]>
        ArrayList<Object[]> dList = new ArrayList<>();
        dList.addAll(Arrays.asList(data));
        inserts(tableName, columns, dList);
    }

    public static void inserts(String tableName, Object[] columns, ArrayList<Object[]> data) throws Exception {
        String query;
        String columnS = "";
        ArrayList<String> queries = new ArrayList<>();
        //append the columns to form a structured sql query
        for (Object column : columns) {
            columnS += "`" + column + "`,";
        }
        //remove the trailing , from the columns , its the last character
        columnS = columnS.substring(0, columnS.length() - 1);
        //loop through all the data rows, it will give us the number of inerts statements so we store to the querries list

        for (Object[] objects : data) {
            //inner loop to get the number of columns to insert
            String mVal = "(";
            for (Object object : objects) {
                mVal += "?,";
            }
            //add the half value querry to the queries list
            queries.add(mVal.substring(0, mVal.length() - 1) + "),");
        }
        System.out.println(queries.size());
        //<editor-fold defaultstate="collapsed" desc="Improved Looping Method">
        /* data.stream().map((objects) -> {
            //inner loop to get the number of columns to insert
            String mVal = "(";
            for (Object object : objects) {
                mVal += "?,";
            }
            return mVal;
        }).forEachOrdered((mVal) -> {
            //add the half value querry to the queries list
            queries.add(mVal.substring(0, mVal.length() - 1) + "),");
        });*/
//</editor-fold>
        //execute each query one by one
        for (int i = 0; i < queries.size(); i++) {
            String next = queries.get(i);
            query = "INSERT INTO `" + tableName + "` (" + columnS + ") VALUES " + next.substring(0, next.length() - 1) + ";";
            System.err.println(query);
            nonQueryI(query, data.get(i));
        }

    }

    public static Object parseResultSetToModelClass(Class<?> modelClass, ResultSet resultSet) throws Exception {

        return new Gson().fromJson(ResultSetToJSON(resultSet).get(0), modelClass);
    }

    public static Collection<?> parseResultSetToCollection(ResultSet resultSet, Class cl) throws Exception {
        ArrayList<Object> collection = new ArrayList();
        for (JsonElement element : ResultSetToJSON(resultSet)) {
            collection.add(new Gson().fromJson(element, cl));
        }

        //<editor-fold defaultstate="collapsed" desc="other">
        /*return   new Gson().fromJson(ResultSetToJSON(resultSet),new TypeAdapter<ArrayList<?>>(){
       @Override
       public void write(JsonWriter writer, ArrayList<?> t) throws IOException {
       throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
       }
       
       @Override
       public ArrayList<?> read(JsonReader reader) throws IOException {
       throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
       }
       
       }.getClass());*/
//</editor-fold>
        return collection;
    }

    public static JsonArray ResultSetToJSON(ResultSet rs) throws Exception {
        JsonArray array = new JsonArray();
        ResultSetMetaData rsmd = rs.getMetaData();
        while (rs.next()) {
            JsonObject obj = new JsonObject();
            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                String colName = rsmd.getColumnName(i);
                obj.addProperty(colName, rs.getObject(i).toString());
            }
            array.add(obj);
        }
        rs.close();
        return array;
    }

    public static void displayToJTable(Object[] columns, Collection<?> data) throws Exception {

    }

}
