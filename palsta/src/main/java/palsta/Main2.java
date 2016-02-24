package palsta;

import java.sql.*;
import java.util.*;
import palsta.pojo.*;
import palsta.db.*;
import spark.ModelAndView;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

public class Main2 {

    public static void main(String[] args) throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:sqlite:../tietokannat/keskustelut.db");
        Statement stmt = connection.createStatement();
        
        ArrayList<Alue> alueet = new ArrayList<>();
        alueet.add(new Alue(1, "ajoneuvos", "Moottoriajoneuvot"));
        alueet.add(new Alue(1, "lentokoneet", "Lentokoneet"));
        alueet.add(new Alue(1, "pyorat", "Polkupyörät"));

        get("/", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("title", "Keskustelupalsta");

            map.put("alueet", alueet);

            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());
    }
}
