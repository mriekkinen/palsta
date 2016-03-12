package palsta.db;

import java.sql.*;
import java.util.*;
import palsta.pojo.*;

public class ViestiDao implements Dao<Viesti, Integer> {

    private Database database;
    private DateHelper dateHelper;
    private QueryMaker<Viesti> query;

    public ViestiDao(Database d) throws SQLException {
        this.database = d;
        this.dateHelper = new DateHelper(d.hasTimestampType());
        Viestikeraaja keraaja = new Viestikeraaja(dateHelper);
        this.query = new QueryMaker(database.getConnection(), keraaja);
    }

    public List<Viesti> findDiscussion(Integer keskustelu, int limit, int offset) throws SQLException {
        List<Viesti> lista = query.queryAndCollect("SELECT * FROM Viesti "
                + "WHERE keskustelu = ? LIMIT ? OFFSET ?", keskustelu, limit, offset);

        if (lista.isEmpty()) {
            return null;
        }

        return lista;
    }

    public void insert(int keskustelu, String lahettaja, Timestamp pvm, String sisalto) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("INSERT INTO Viesti(keskustelu, lahettaja, pvm, sisalto) "
                + "VALUES (?, ?, ?, ?)");

        stmt.setObject(1, keskustelu);
        stmt.setObject(2, lahettaja);
        stmt.setObject(3, dateHelper.saveAs(pvm));
        stmt.setObject(4, sisalto);

        stmt.executeUpdate();

        stmt.close();
        connection.close();
    }

    @Override
    public Viesti findOne(Integer key) throws SQLException {
        List<Viesti> lista = query.queryAndCollect("SELECT * FROM Keskustelu WHERE tunnus = ?", key);

        if (lista.isEmpty()) {
            return null;
        }

        return lista.get(0);
    }

    @Override
    public List<Viesti> findAll() throws SQLException {
        return query.queryAndCollect("SELECT * FROM Viesti");
    }

}
