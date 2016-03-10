package palsta.db;

import java.util.*;
import java.sql.*;
import palsta.pojo.Keskustelu;

public class KeskusteluDao implements Dao<Keskustelu, Integer> {

    private Database database;

    public KeskusteluDao(Database d) {
        this.database = d;
    }

    public List<Keskustelu> findTenNewest(String webtunnus) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT k.tunnus, k.alue, k.otsikko, COUNT(v.tunnus) AS viesteja, MAX(v.pvm) AS viimeisin "
                + "FROM Alue a "
                + "INNER JOIN Keskustelu k "
                + "ON a.tunnus = k.alue "
                + "LEFT JOIN Viesti v "
                + "ON k.tunnus = v.keskustelu "
                + "WHERE a.web_tunnus = ? "
                + "GROUP BY k.tunnus "
                + "ORDER BY viimeisin DESC "
                + "LIMIT 10");
        stmt.setObject(1, webtunnus);

        ResultSet rs = stmt.executeQuery();
        List<Keskustelu> keskustelut = new ArrayList<>();
        while (rs.next()) {
            Integer tunnus = rs.getInt("tunnus");
            Integer alue = rs.getInt("alue");
            String otsikko = rs.getString("otsikko");
            int viestejaYhteensa = rs.getInt("viesteja");
            Timestamp viimeisin = Timestamp.valueOf(rs.getString("viimeisin"));

            keskustelut.add(new Keskustelu(tunnus, alue, otsikko, viestejaYhteensa, viimeisin));
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
        String otsikko = rs.getString("otsikko");

        Keskustelu k = new Keskustelu(tunnus, alue, otsikko);

        rs.close();
        stmt.close();
        connection.close();

        return k;
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
            String otsikko = rs.getString("otsikko");

            keskustelut.add(new Keskustelu(tunnus, alue, otsikko));
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
        PreparedStatement stmt = connection.prepareStatement("INSERT INTO Keskustelu(alue, otsikko) VALUES (?, ?)");

        for (int i = 0; i < params.length; i++) {
            stmt.setObject(i + 1, params[i]);
        }

        int changes = stmt.executeUpdate();

        stmt.close();
        connection.close();
    }

}
