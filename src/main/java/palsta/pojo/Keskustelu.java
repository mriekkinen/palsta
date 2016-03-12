package palsta.pojo;

import java.sql.*;

public class Keskustelu {

    private Integer tunnus;
    private Integer alue;
    private String otsikko;
    private int viestejaYhteensa;
    private Timestamp uusinViesti;

    public Keskustelu(Integer tunnus, Integer alue, String otsikko) {
        this(tunnus, alue, otsikko, 0, null);
    }

    public Keskustelu(Integer tunnus, Integer alue, String otsikko, int viesteja, Timestamp uusin) {
        this.tunnus = tunnus;
        this.alue = alue;
        this.otsikko = otsikko;
        this.viestejaYhteensa = viesteja;
        this.uusinViesti = uusin;
    }

    public Integer getTunnus() {
        return this.tunnus;
    }

    public Integer getAlue() {
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
