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
        PreparedStatement stmt = connection.prepareStatement("delete FROM Alue WHERE tunnus = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        rs.next();
        
        rs.close();
        stmt.close();
        connection.close();
    }

}
