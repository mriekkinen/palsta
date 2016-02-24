package palsta;

import java.sql.*;
import java.util.*;
import palsta.pojo.*;
import palsta.db.*;
import spark.ModelAndView;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

public class Main2 {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        // 1. Käynnistä projekti Netbeansissa
        // 2. Mene selaimella osoitteeseen http://localhost:4567/
        Database db = new Database("jdbc:sqlite:../tietokannat/keskustelut.db");
        AlueDao alueDao = new AlueDao(db);
        List<Alue> alueet = alueDao.findAll();

        get("/", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("title", "Keskustelupalsta");

            map.put("alueet", alueet);

            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());
    }
}
