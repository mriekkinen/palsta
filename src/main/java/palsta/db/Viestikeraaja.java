package palsta.db;

import java.sql.*;
import palsta.pojo.Viesti;

public class Viestikeraaja implements Collector<Viesti> {

    private DateHelper dateHelper;

    public Viestikeraaja(DateHelper dateHelper) {
        this.dateHelper = dateHelper;
    }

    @Override
    public Viesti collect(ResultSet rs) throws SQLException {
        Integer tunnus = rs.getInt("tunnus");
        Integer keskustelu = rs.getInt("keskustelu");
        String lahettaja = rs.getString("lahettaja");
        Timestamp pvm = dateHelper.getTimestamp(rs, "pvm");
        String sisalto = rs.getString("sisalto");

        return new Viesti(tunnus, keskustelu, lahettaja, pvm, sisalto);
    }

}
