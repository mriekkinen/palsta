package palsta;

import java.sql.*;

public class Main {

    public static void main(String[] args) throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:sqlite:../tietokannat/keskustelut.db");
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM Alue");

        System.out.println("rs.next() palauttaa " + rs.next());
        
//        while (rs.next()) {
//            System.out.println(rs.getString("nimi"));
//        }

        rs.close();
        stmt.close();
        connection.close();
    }

}
