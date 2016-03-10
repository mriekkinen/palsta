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
        Timestamp pvm = Timestamp.valueOf(rs.getString("pvm"));
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

   
   
    public void insert(int tunnus, String lahettaja, Timestamp pvm, int webtunnus , String sisalto) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("INSERT INTO "
                + "Viesti(keskustelu, lahettaja, pvm, web_tunnus, sisalto) VALUES (?, ?, ?, ?, ?)");

        stmt.setObject(1, tunnus);
        stmt.setObject(2, lahettaja);
        stmt.setObject(3, pvm);
        stmt.setObject(4, webtunnus);
        stmt.setObject(5, sisalto);
        
        
//        for (int i = 0; i < params.length; i++) {
//            stmt.setObject(i + 1, params[i]);
//        }

        stmt.executeUpdate();

        stmt.close();
        connection.close();
    }

}
