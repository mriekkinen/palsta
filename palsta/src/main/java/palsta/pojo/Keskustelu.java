package palsta.pojo;

import java.util.*;
import java.sql.*;

public class Keskustelu {

    private Integer tunnus;
    private Integer alue; // Alueen PK
    private String webTunnus;
    private String otsikko;
    private int viestejaYhteensa;
    private Timestamp uusinViesti;

    public Keskustelu(Integer tunnus, Integer alue, String webTunnus, String otsikko) {
        this(tunnus, alue, webTunnus, otsikko, 0, null);
    }
    
    public Keskustelu(Integer tunnus, Integer alue, String webTunnus, String otsikko, int viesteja, Timestamp uusin) {
        this.tunnus = tunnus;
        this.alue = alue;
        this.webTunnus = webTunnus;
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

    public String getWebTunnus() {
        return this.webTunnus;
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
