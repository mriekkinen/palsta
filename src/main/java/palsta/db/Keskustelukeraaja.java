package palsta.db;

import java.sql.*;
import palsta.pojo.Keskustelu;

public class Keskustelukeraaja implements Collector<Keskustelu> {

    private DateHelper dateHelper;

    public Keskustelukeraaja(DateHelper dateHelper) {
        this.dateHelper = dateHelper;
    }

    @Override
    public Keskustelu collect(ResultSet rs) throws SQLException {
        Integer tunnus = rs.getInt("tunnus");
        Integer alue = rs.getInt("alue");
        String otsikko = rs.getString("otsikko");
        int viestejaYhteensa = rs.getInt("viesteja");
        Timestamp viimeisin = dateHelper.getTimestamp(rs, "viimeisin");

        return new Keskustelu(tunnus, alue, otsikko, viestejaYhteensa, viimeisin);
    }

}
