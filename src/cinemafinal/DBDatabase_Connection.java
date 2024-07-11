package cinemafinal;


import java.sql.Connection;
import java.sql.DriverManager;

public class DBDatabase_Connection {
   Connection con;

   public DBDatabase_Connection() {
   }

   public void connection_data() {
      try {
         Class.forName("com.mysql.cj.jdbc.Driver");
         this.con = DriverManager.getConnection("jdbc:mysql://localhost:3306/cinema", "root", "22820044");
         if (this.con != null) {
            System.out.println("connection done ");
         } else {
            System.out.println("there is a issue when connection to data base");
         }
      } catch (Exception var2) {
         System.out.println(var2);
      }

   }
}

