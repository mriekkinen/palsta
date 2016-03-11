package palsta.db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import palsta.pojo.*;

public class ViestiDao implements Dao<Viesti, Integer> {

    private Database database;
    private DateHelper dateHelper;

    public ViestiDao(Database d) {
        this.database = d;
        this.dateHelper = new DateHelper(d.hasTimestampType());
    }

    public List<Viesti> findDiscussion(Integer keskustelu, int limit, int offset) throws SQLException { // Jonkin keskustelun viestit
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Viesti WHERE keskustelu = ? LIMIT ? OFFSET ?");
        stmt.setObject(1, keskustelu);
        stmt.setObject(2, limit);
        stmt.setObject(3, offset);

        ResultSet rs = stmt.executeQuery();

        List<Viesti> viestit = new ArrayList<>();
        while (rs.next()) {
            int tunnus = rs.getInt("tunnus");
            String lahettaja = rs.getString("lahettaja");
            Timestamp pvm = dateHelper.getTimestamp(rs, "pvm");
            String sisalto = rs.getString("sisalto");

            viestit.add(new Viesti(tunnus, keskustelu, lahettaja, pvm, sisalto));
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
        Timestamp pvm = dateHelper.getTimestamp(rs, "pvm");
        String sisalto = rs.getString("sisalto");

        Viesti viesti = new Viesti(tunnus, keskustelu, lahettaja, pvm, sisalto);

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
            Timestamp pvm = dateHelper.getTimestamp(rs, "pvm");
            String sisalto = rs.getString("sisalto");

            viestit.add(new Viesti(tunnus, keskustelu, lahettaja, pvm, sisalto));
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

    public void insert(int keskustelu, String lahettaja, Timestamp pvm, String sisalto) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("INSERT INTO Viesti(keskustelu, lahettaja, pvm, sisalto) VALUES (?, ?, ?, ?)");

        stmt.setObject(1, keskustelu);
        stmt.setObject(2, lahettaja);
        stmt.setObject(3, dateHelper.saveAs(pvm));
        stmt.setObject(4, sisalto);

        stmt.executeUpdate();

        stmt.close();
        connection.close();
    }

}
