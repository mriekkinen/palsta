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
        PreparedStatement stmt = connection.prepareStatement("SELECT v.tunnus, v.keskustelu, v.lahettaja, v.pvm, v.sisalto FROM Alue a "
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
        Timestamp pvm = Timestamp.valueOf(rs.getString("pvm"));
        String sisalto = rs.getString("sisalto");
        String webTunnus = rs.getString("web_tunnus");
        String nimi = rs.getString("nimi");

        Viesti viesti = new Viesti(tunnus, keskustelu, lahettaja, pvm, sisalto);

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
    
   

    public Alue findOne(String webTunnus) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Alue WHERE web_tunnus = ?");
        stmt.setObject(1, webTunnus);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        int tunnus = rs.getInt("tunnus");
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
        PreparedStatement stmt = connection.prepareStatement(
                "SELECT a.tunnus, a.web_tunnus, a.nimi AS alue, COUNT(v.tunnus) AS viesteja, MAX(v.pvm) AS viimeisin"
                + " FROM Alue a"
                + " LEFT JOIN Keskustelu k"
                + " ON a.tunnus = k.alue"
                + " INNER JOIN Viesti v"
                + " ON k.tunnus = v.keskustelu"
                + " GROUP BY a.tunnus"
                + " ORDER BY alue");

        ResultSet rs = stmt.executeQuery();

        List<Alue> lista = new ArrayList<>();
        while (rs.next()) {
            int tunnus = rs.getInt("tunnus");
            String webTunnus = rs.getString("web_tunnus");
            String nimi = rs.getString("alue");
            int viestejaYhteensa = rs.getInt("viesteja");
            Timestamp viimeisin = Timestamp.valueOf(rs.getString("viimeisin"));

            lista.add(new Alue(tunnus, webTunnus, nimi, viestejaYhteensa, viimeisin));
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

 
    public void insert(String webTunnus, String nimi) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("INSERT INTO Alue(webTunnus, nimi) VALUES (?, ?)");

        stmt.setObject(1, webTunnus);
        stmt.setObject(2, nimi);

        int changes = stmt.executeUpdate();

        stmt.close();
        connection.close();
    }

}
