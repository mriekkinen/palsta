package palsta.pojo;

import java.sql.*;

public class Viesti {

    private int tunnus;
    private int keskustelu;
    private String lahettaja;
    private Timestamp pvm;
    private String sisalto;

    public Viesti(int tunnus, int keskustelu, String lahettaja, Timestamp pvm, String sisalto) {
        this.tunnus = tunnus;
        this.keskustelu = keskustelu;
        this.lahettaja = lahettaja;
        this.pvm = pvm;
        this.sisalto = sisalto;
    }

    public int getTunnus() {
        return this.tunnus;
    }

    public int getKeskustelu() {
        return this.keskustelu;
    }

    public String getLahettaja() {
        return this.lahettaja;
    }

    public Timestamp getPvm() {
        return this.pvm;
    }

    public String getSisalto() {
        return this.sisalto;
    }
}
