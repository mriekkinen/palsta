package palsta.pojo;

import java.util.*;
import java.sql.*;

public class Alue {
    
    
    private int tunnus;
    private String webTunnus;
    private String nimi;
    
    public Alue(int tunnus, String webTunnus, String nimi) {
        this.tunnus = tunnus;
        this.webTunnus = webTunnus;
        this.nimi = nimi;
    }
    
    public Integer getTunnus() {
        return this.tunnus;
    }
    
    public String getWebTunnus() {
        return this.webTunnus;
    }
    
    public String getNimi() {
        return this.nimi;
    }
    
}
