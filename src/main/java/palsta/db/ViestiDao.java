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

    public int insert(int keskustelu, String lahettaja, Timestamp pvm, String sisalto) throws SQLException {
        return query.insert("INSERT INTO Viesti(keskustelu, lahettaja, pvm, sisalto) "
                + "VALUES (?, ?, ?, ?)",
                keskustelu, lahettaja, dateHelper.saveAs(pvm), sisalto);
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
