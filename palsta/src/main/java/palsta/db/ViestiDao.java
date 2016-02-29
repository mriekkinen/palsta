package palsta.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import palsta.pojo.Alue;
import palsta.pojo.Keskustelu;
import palsta.pojo.Viesti;

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

        List<Viesti> lista = new ArrayList<>();
        while (rs.next()) {
            int tunnus = rs.getInt("tunnus");
            int keskustelu = rs.getInt("keskustelu");
            String lahettaja = rs.getString("lahettaja");
            Timestamp pvm = rs.getTimestamp("pvm");
            String sisalto = rs.getString("sisalto");
            String webTunnus = rs.getString("web_tunnus");
            String nimi = rs.getString("nimi");

            lista.add(new Viesti(tunnus, keskustelu, lahettaja, webTunnus, pvm, sisalto));
        }
        rs.close();
        stmt.close();
        connection.close();

        return lista;
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
        String sisalto = rs.getString("sisalto");
        String webTunnus = rs.getString("web_tunnus");
        String nimi = rs.getString("nimi");

        Viesti viesti = new Viesti(tunnus, keskustelu, lahettaja, webTunnus, pvm, sisalto);

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

        List<Viesti> lista = new ArrayList<>();
        while (rs.next()) {
            int tunnus = rs.getInt("tunnus");
            int keskustelu = rs.getInt("keskustelu");
            String lahettaja = rs.getString("lahettaja");
            Timestamp pvm = rs.getTimestamp("pvm");
            String sisalto = rs.getString("sisalto");
            String webTunnus = rs.getString("web_tunnus");
            String nimi = rs.getString("nimi");

            lista.add(new Viesti(tunnus, keskustelu, lahettaja, webTunnus, pvm, sisalto));
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

    @Override
    public void insert() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
