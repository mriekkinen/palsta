package palsta.db;

import java.sql.*;
import java.util.*;
import palsta.pojo.*;

public class AlueDao implements Dao<Alue, Integer> {

    private Database database;
    private DateHelper dateHelper;
    private QueryMaker<Alue> query;

    private final String selectQueryStart = ""
            + "SELECT a.tunnus, a.web_tunnus, a.nimi,"
            + " COUNT(v.tunnus) AS viesteja, MAX(v.pvm) AS viimeisin "
            + "FROM Alue a "
            + "LEFT JOIN Keskustelu k "
            + "ON a.tunnus = k.alue "
            + "LEFT JOIN Viesti v "
            + "ON k.tunnus = v.keskustelu ";

    public AlueDao(Database d) throws SQLException {
        this.database = d;
        this.dateHelper = new DateHelper(d.hasTimestampType());
        Aluekeraaja keraaja = new Aluekeraaja(this.dateHelper);
        this.query = new QueryMaker<>(database.getConnection(), keraaja);
    }

    public int countDiscussions(Integer key) throws SQLException {
        return query.queryInteger("SELECT COUNT(*) FROM Keskustelu WHERE alue = ?", key);
    }

    public int insert(String webTunnus, String nimi) throws SQLException {
        return query.insert("INSERT INTO Alue(webTunnus, nimi) VALUES (?, ?)", webTunnus, nimi);
    }

    public Alue findOne(String webTunnus) throws SQLException {
        List<Alue> lista = query.queryAndCollect(selectQueryStart
                + "WHERE a.web_tunnus = ? "
                + "GROUP BY a.tunnus",
                webTunnus);

        if (lista.isEmpty()) {
            return null;
        }

        return lista.get(0);
    }

    @Override
    public Alue findOne(Integer key) throws SQLException {
        List<Alue> lista = query.queryAndCollect(selectQueryStart
                + "WHERE a.tunnus = ? "
                + "GROUP BY a.tunnus",
                key);

        if (lista.isEmpty()) {
            return null;
        }

        return lista.get(0);
    }

    @Override
    public List<Alue> findAll() throws SQLException {
        return query.queryAndCollect(selectQueryStart
                + "GROUP BY a.tunnus "
                + "ORDER BY a.nimi");
    }
}
