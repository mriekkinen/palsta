package palsta.db;

import java.util.*;
import java.sql.*;
import palsta.pojo.Keskustelu;

public class KeskusteluDao implements Dao<Keskustelu, Integer> {

    private Database database;
    private QueryMaker<Keskustelu> query;

    private final String selectQueryStart = ""
            + "SELECT k.tunnus, k.alue, k.otsikko, COUNT(v.tunnus) AS viesteja, MAX(v.pvm) AS viimeisin "
            + "FROM Alue a "
            + "INNER JOIN Keskustelu k "
            + "ON a.tunnus = k.alue "
            + "LEFT JOIN Viesti v "
            + "ON k.tunnus = v.keskustelu ";

    public KeskusteluDao(Database d) throws SQLException {
        this.database = d;
        DateHelper dateHelper = new DateHelper(d.hasTimestampType());
        Keskustelukeraaja keraaja = new Keskustelukeraaja(dateHelper);
        this.query = new QueryMaker<>(database.getConnection(), keraaja);
    }

    public List<Keskustelu> findOffset(String webtunnus, int limit, int offset) throws SQLException {
        return query.queryAndCollect(selectQueryStart
                + "WHERE a.web_tunnus = ? "
                + "GROUP BY k.tunnus "
                + "ORDER BY viimeisin DESC "
                + "LIMIT ? OFFSET ?",
                webtunnus, limit, offset);
    }

    @Override
    public Keskustelu findOne(Integer key) throws SQLException {
        List<Keskustelu> lista = query.queryAndCollect(selectQueryStart
                + "WHERE k.tunnus = ? "
                + "GROUP BY k.tunnus", key);

        if (lista.isEmpty()) {
            return null;
        }

        return lista.get(0);
    }

    @Override
    public List<Keskustelu> findAll() throws SQLException {
        return query.queryAndCollect(selectQueryStart
                + "GROUP BY k.tunnus "
                + "ORDER BY viimeisin DESC");
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

    public void insert(int alue, String otsikko) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("INSERT INTO Keskustelu(alue, otsikko) VALUES (?, ?)");

        stmt.setObject(1, alue);
        stmt.setObject(2, otsikko);

        int changes = stmt.executeUpdate();

        stmt.close();
        connection.close();
    }

    public int findPrimaryKey(String otsikko) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT tunnus FROM Keskustelu WHERE otsikko = ?");
        stmt.setObject(1, otsikko);
        ResultSet rs = stmt.executeQuery();

        rs.next();
        Integer tunnus = rs.getInt("tunnus");

        rs.close();
        stmt.close();
        connection.close();

        return tunnus;

    }

}
