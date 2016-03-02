package palsta.db;

import java.util.*;
import java.sql.*;
import palsta.pojo.Keskustelu;

public class KeskusteluDao implements Dao<Keskustelu, Integer> {

    private Database database;

    public KeskusteluDao(Database d) {
        this.database = d;
    }
    
    public List<Keskustelu> findTenNewest() throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT k.tunnus, k.alue, k.web_tunnus, k.otsikko"
                + "FROM Alue a\n"
                + "  INNER JOIN Keskustelu k\n"
                + "    ON a.tunnus = k.alue\n"
                + "  LEFT JOIN Viesti v\n"
                + "    ON k.tunnus = v.keskustelu\n"
                + "WHERE alue.web_tunnus = ?\n"
                + "GROUP BY k.tunnus\n"
                + "ORDER BY viimeisin DESC\n"
                + "LIMIT 10");

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
        PreparedStatement stmt = connection.prepareStatement("SELECT * From Keskustelu");

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
    public void insert(Object... params) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("INSERT INTO Keskustelu(alue, webTunnus, otsikko) VALUES (?, ?, ?)");

        for (int i = 0; i < params.length; i++) {
            stmt.setObject(i + 1, params[i]);
        }

        int changes = stmt.executeUpdate();

        stmt.close();
        connection.close();
    }

}
