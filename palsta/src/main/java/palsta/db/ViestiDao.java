package palsta.db;

import java.sql.*;
import java.util.*;
import palsta.pojo.*;

public class ViestiDao implements Dao<Viesti, Integer> {

    private Database database;

    public ViestiDao(Database d) {
        this.database = d;
    }

    public List<Viesti> findConvosMessages(Integer key) throws SQLException { // Jonkin keskustelun viestit
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Viesti WHERE keskustelu = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();

        List<Viesti> viestit = new ArrayList<>();
        while (rs.next()) {
            int tunnus = rs.getInt("tunnus");
            int keskustelu = rs.getInt("keskustelu");
            String lahettaja = rs.getString("lahettaja");
            Timestamp pvm = rs.getTimestamp("pvm");
            String webTunnus = rs.getString("web_tunnus");
            String sisalto = rs.getString("sisalto");

            viestit.add(new Viesti(tunnus, keskustelu, lahettaja, pvm, webTunnus, sisalto));
        }
        rs.close();
        stmt.close();
        connection.close();

        return viestit;
    }

    @Override
    public Viesti findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Keskustelu WHERE tunnus = ?");
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
        String webTunnus = rs.getString("web_tunnus");
        String sisalto = rs.getString("sisalto");

        Viesti viesti = new Viesti(tunnus, keskustelu, lahettaja, pvm, webTunnus, sisalto);

        rs.close();
        stmt.close();
        connection.close();

        return viesti;
    }

    @Override
    public List<Viesti> findAll() throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Viesti");

        ResultSet rs = stmt.executeQuery();

        List<Viesti> viestit = new ArrayList<>();
        while (rs.next()) {
            int tunnus = rs.getInt("tunnus");
            int keskustelu = rs.getInt("keskustelu");
            String lahettaja = rs.getString("lahettaja");
            Timestamp pvm = Timestamp.valueOf(rs.getString("pvm"));
            String webTunnus = rs.getString("web_tunnus");
            String sisalto = rs.getString("sisalto");

            viestit.add(new Viesti(tunnus, keskustelu, lahettaja, pvm, webTunnus, sisalto));
        }
        rs.close();
        stmt.close();
        connection.close();

        return viestit;
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
    public void insert() throws SQLException {
        
    }

}
