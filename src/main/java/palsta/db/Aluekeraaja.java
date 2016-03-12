package palsta.db;

import java.sql.*;
import palsta.pojo.Alue;

public class Aluekeraaja implements Collector<Alue> {

    private DateHelper dateHelper;

    public Aluekeraaja(DateHelper dateHelper) {
        this.dateHelper = dateHelper;
    }

    @Override
    public Alue collect(ResultSet rs) throws SQLException {
        Integer tunnus = rs.getInt("tunnus");
        String webTunnus = rs.getString("web_tunnus");
        String nimi = rs.getString("nimi");
        int viestejaYhteensa = rs.getInt("viesteja");
        Timestamp viimeisin = dateHelper.getTimestamp(rs, "viimeisin");

        return new Alue(tunnus, webTunnus, nimi, viestejaYhteensa, viimeisin);
    }

}
