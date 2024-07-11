package cinemafinal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class DBInsert_Data {

    PreparedStatement PRD;
    ResultSet rst;

    private String table_name;
    private ArrayList<Object> table_data = new ArrayList<>(); // Changed to hold objects instead of just strings

    // Modified constructor to accept objects instead of strings
    public DBInsert_Data(String table_name, Object... table_data) {
        this.table_name = table_name;
        for (Object data : table_data) {
            this.table_data.add(data);
        }
    }

    // Modified to handle both strings and integers
    public String get_information_value() {
        StringBuilder queryBuilder = new StringBuilder();
        for (int i = 0; i < table_data.size(); i++) {
            Object value = table_data.get(i);
            if (value instanceof String) { // Check if the value is a string
                queryBuilder.append("'" + value + "'"); // If string, enclose in single quotes
            } else {
                queryBuilder.append(value); // If not a string (i.e., integer), append directly
            }
            if (i < table_data.size() - 1) {
                queryBuilder.append(", ");
            }
        }
        return queryBuilder.toString();
    }

    public void insert_data_to_data_base() {
        DBDatabase_Connection connection_data = new DBDatabase_Connection();
        connection_data.connection_data();
        try {
            // Constructing SQL query with the correct handling of string and integer values
            PRD = connection_data.con.prepareStatement("INSERT INTO " + table_name + " VALUES (" + get_information_value() + ")");
            PRD.execute();
            System.out.println("The data has been inserted successfully");
        } catch (Exception er) {
            System.out.println(er.getMessage());
        }
    }
}