/*
Luokka on AlueDaon findOne metodeja varten,
jolloin luodaan Alue ilman viestejä ja uusinta viestiä.
*/
package palsta.db;

import java.sql.*;
import palsta.pojo.Alue;

public class UudenAlueenkeraaja implements Collector<Alue> {

    @Override
    public Alue collect(ResultSet rs) throws SQLException {
        Integer tunnus = rs.getInt("tunnus");
        String webTunnus = rs.getString("web_tunnus");
        String nimi = rs.getString("nimi");

        return new Alue(tunnus, webTunnus, nimi);
    }

}
