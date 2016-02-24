/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package palsta.db;

import java.util.*;
import java.sql.*;
import palsta.pojo.Keskustelu;

/**
 *
 * @author akiirikk@cs
 */
public class KeskusteluDao implements Dao<Keskustelu, Integer> {

    private Database database;

    public KeskusteluDao(Database d) {
        this.database = d;
    }

    @Override
    public Keskustelu findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Keskustelu WHERE tunnus = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Integer tunnus = rs.getInt("tunnus");
        Integer alue = rs.getInt("alue");
        String webTunnus = rs.getString("web_tunnus");
        String otsikko = rs.getString("otsikko");

        Keskustelu k = new Keskustelu(tunnus, alue, webTunnus, otsikko);

        rs.close();
        stmt.close();
        connection.close();

        return k; //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Keskustelu> findAll() throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Keskustelu");

        ResultSet rs = stmt.executeQuery();
        List<Keskustelu> keskustelut = new ArrayList<>();
        while (rs.next()) {
            Integer tunnus = rs.getInt("tunnus");
            Integer alue = rs.getInt("alue");
            String webTunnus = rs.getString("web_tunnus");
            String otsikko = rs.getString("otsikko");

            keskustelut.add(new Keskustelu(tunnus, alue, webTunnus, otsikko));
        }

        rs.close();
        stmt.close();
        connection.close();

        return keskustelut;
    }

    @Override
    public void delete(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("DELETE FROM Keskustelu WHERE tunnus = ?");
        stmt.setObject(1, key);
        ResultSet rs = stmt.executeQuery();
        rs.next();
        rs.close();
        stmt.close();
        connection.close();

    }
    
    @Override
    public void insert() throws SQLException { // Ei miään hajua miten tää lisäys hoituu
        Connection connection = database.getConnection();
        String sql = "INSERT INTO EMPLOYEE "
                + "(tunnus, alue, web_tunnus, otsikko) VALUES ("
                + s(keskustelu.getTunnus()) + ", "
                + keskustelu.getAlue() + ", "
                + s(keskustelu.getWebTunnus()) + ", "
                + keskustelu.getSalary() + " );";

        db.update(sql);
        
    }

}
