package palsta.pojo;

import java.sql.*;

public class Alue {

    private int tunnus;
    private String webTunnus;
    private String nimi;
    private int viestejaYhteensa;
    private Timestamp uusinViesti;

    public Alue(int tunnus, String webTunnus, String nimi) {
        this(tunnus, webTunnus, nimi, 0, null);
    }

    public Alue(int tunnus, String webTunnus, String nimi, int viesteja, Timestamp uusin) {
        this.tunnus = tunnus;
        this.webTunnus = webTunnus;
        this.nimi = nimi;
        this.viestejaYhteensa = viesteja;
        this.uusinViesti = uusin;
    }

    public int getTunnus() {
        return this.tunnus;
    }

    public String getWebTunnus() {
        return this.webTunnus;
    }

    public String getNimi() {
        return this.nimi;
    }

    public int getViestejaYhteensa() {
        return viestejaYhteensa;
    }

    public Timestamp getUusinViesti() {
        return uusinViesti;
    }

}
