package palsta.db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import palsta.pojo.*;

public class AlueDao implements Dao<Alue, Integer> {

    private Database database;

    public AlueDao(Database d) {
        this.database = d;
    }

    public int messagesInArea(Integer key) throws SQLException { //viestien määrä alueessa
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT COUNT(*) "
                + "FROM Alue a "
                + " JOIN Keskustelu k "
                + "     ON ? = k.alue "
                + " JOIN Viesti v "
                + "     ON k.tunnus = v.keskustelu "
                + "GROUP BY a.nimi");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return 0;
        }

        int viesteja = rs.getInt("COUNT(*)");

        rs.close();
        stmt.close();
        connection.close();

        return viesteja;
    }
    
    public Viesti newestMessageInArea(Integer key) throws SQLException { // alueen uusin viesti
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT v.tunnus, v.keskustelu, v.lahettaja, v.pvm, v.web_tunnus, v.sisalto FROM Alue a "
                + "JOIN Keskustelu k ON ? = k.alue "
                + "JOIN Viesti v ON k.tunnus = v.keskustelu "
                + "ORDER BY v.pvm "
                + "LIMIT 1");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        int tunnus = rs.getInt("tunnus");
        int keskustelu = rs.getInt("keskustelu");
        String lahettaja = rs.getString("lahettaja");
        Timestamp pvm = rs.getTimestamp("pvm");
        String sisalto = rs.getString("sisalto");
        String webTunnus = rs.getString("web_tunnus");
        String nimi = rs.getString("nimi");

        Viesti viesti = new Viesti(tunnus, keskustelu, lahettaja, pvm, webTunnus, sisalto);

        rs.close();
        stmt.close();
        connection.close();

        return viesti;
    }
    
    @Override
    public Alue findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Alue WHERE tunnus = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        int tunnus = rs.getInt("tunnus");
        String webTunnus = rs.getString("web_tunnus");
        String nimi = rs.getString("nimi");

        Alue alue = new Alue(tunnus, webTunnus, nimi);

        rs.close();
        stmt.close();
        connection.close();

        return alue;
    }

    @Override
    public List<Alue> findAll() throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Alue");

        ResultSet rs = stmt.executeQuery();

        List<Alue> lista = new ArrayList<>();
        while (rs.next()) {
            int tunnus = rs.getInt("tunnus");
            String webTunnus = rs.getString("web_tunnus");
            String nimi = rs.getString("nimi");

            lista.add(new Alue(tunnus, webTunnus, nimi));
        }
        rs.close();
        stmt.close();
        connection.close();

        return lista;
    }

    @Override
    public void delete(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("DELETE FROM Alue WHERE tunnus = ?");
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
        PreparedStatement stmt = connection.prepareStatement("INSERT INTO Viesti(webTunnus, nimi) VALUES (?, ?)");

        for (int i = 0; i < params.length; i++) {
            stmt.setObject(i + 1, params[i]);
        }

        int changes = stmt.executeUpdate();

        stmt.close();
        connection.close();
    }

}
