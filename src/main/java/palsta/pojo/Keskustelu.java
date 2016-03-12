package palsta.pojo;

import java.sql.*;

public class Keskustelu {

    private int tunnus;
    private int alue;
    private String otsikko;
    private int viestejaYhteensa;
    private Timestamp uusinViesti;

    public Keskustelu(int tunnus, int alue, String otsikko) {
        this(tunnus, alue, otsikko, 0, null);
    }

    public Keskustelu(int tunnus, int alue, String otsikko, int viesteja, Timestamp uusin) {
        this.tunnus = tunnus;
        this.alue = alue;
        this.otsikko = otsikko;
        this.viestejaYhteensa = viesteja;
        this.uusinViesti = uusin;
    }

    public int getTunnus() {
        return this.tunnus;
    }

    public int getAlue() {
        return this.alue;

    }

    public String getOtsikko() {
        return this.otsikko;
    }

    public int getViestejaYhteensa() {
        return viestejaYhteensa;
    }

    public Timestamp getUusinViesti() {
        return uusinViesti;
    }
}
