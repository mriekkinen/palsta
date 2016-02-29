package palsta.pojo;

import java.util.*;
import java.sql.*;

public class Keskustelu {

    private Integer tunnus;
    private Integer alue; // Alueen PK
    private String webTunnus;
    private String otsikko;

    public Keskustelu(Integer tunnus, Integer alue, String webTunnus, String otsikko) {
        this.tunnus = tunnus;
        this.alue = alue;
        this.webTunnus = webTunnus;
        this.otsikko = otsikko;
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

}
