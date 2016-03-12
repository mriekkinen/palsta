package palsta.db;

import java.sql.*;
import java.util.*;
import palsta.pojo.*;

public class AlueDao implements Dao<Alue, Integer> {

    private Database database;
    private DateHelper dateHelper;
    private QueryMaker<Alue> query1;
    private QueryMaker<Alue> query2;

    public AlueDao(Database d) throws SQLException {
        this.database = d;
        this.dateHelper = new DateHelper(d.hasTimestampType());
        UudenAlueenkeraaja uak = new UudenAlueenkeraaja();
        Aluekeraaja keraaja = new Aluekeraaja(this.dateHelper);
        this.query1 = new QueryMaker(database.getConnection(), uak);
        this.query2 = new QueryMaker(database.getConnection(), keraaja);
    }

    public int countDiscussions(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT COUNT(*) FROM Keskustelu "
                + "WHERE alue = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return 0;
        }

        int lkm = rs.getInt(1);

        rs.close();
        stmt.close();
        connection.close();

        return lkm;
    }

    public void insert(String webTunnus, String nimi) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("INSERT INTO Alue(webTunnus, nimi) VALUES (?, ?)");

        stmt.setObject(1, webTunnus);
        stmt.setObject(2, nimi);

        stmt.executeUpdate();

        stmt.close();
        connection.close();
    }

    public Alue findOne(String webTunnus) throws SQLException {
        List<Alue> lista = query1.queryAndCollect("SELECT * FROM Alue WHERE web_tunnus = ?", webTunnus);

        if (lista.isEmpty()) {
            return null;
        }

        return lista.get(0);
    }

    @Override
    public Alue findOne(Integer key) throws SQLException {
        List<Alue> lista = query1.queryAndCollect("SELECT * FROM Alue WHERE tunnus = ?", key);

        if (lista.isEmpty()) {
            return null;
        }

        return lista.get(0);
    }

    @Override
    public List<Alue> findAll() throws SQLException {
        return query2.queryAndCollect("SELECT a.tunnus, a.web_tunnus, a.nimi AS alue, "
                + "COUNT(v.tunnus) AS viesteja, MAX(v.pvm) AS viimeisin"
                + " FROM Alue a"
                + " LEFT JOIN Keskustelu k"
                + " ON a.tunnus = k.alue"
                + " LEFT JOIN Viesti v"
                + " ON k.tunnus = v.keskustelu"
                + " GROUP BY a.tunnus"
                + " ORDER BY alue");
    }
}
