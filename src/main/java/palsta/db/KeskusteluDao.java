package palsta.db;

import java.util.*;
import java.sql.*;
import palsta.pojo.Keskustelu;

public class KeskusteluDao implements Dao<Keskustelu, Integer> {

    private Database database;
    private QueryMaker<Keskustelu> query;

    private final String selectQueryStart = ""
            + "SELECT k.tunnus, k.alue, k.otsikko, COUNT(v.tunnus) AS viesteja,"
            + " MAX(v.pvm) AS viimeisin "
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

    public int insert(int alue, String otsikko) throws SQLException {
        return query.insert("INSERT INTO Keskustelu(alue, otsikko) "
                + "VALUES (?, ?)", alue, otsikko);
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

}
