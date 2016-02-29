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
        KeskusteluDao keskusteluDao = new KeskusteluDao(db);
        ViestiDao viestiDao = new ViestiDao(db);

        List<Alue> alueet = alueDao.findAll();
        List<Keskustelu> keskustelut = keskusteluDao.findAll();
        List<Viesti> viestit = new ArrayList<>();

        viestit.add(new Viesti(1, 1, "Matti", "", new Timestamp(1456740000), "Hyvin toimii"));
        viestit.add(new Viesti(1, 1, "Mikko", "", new Timestamp(1456747200), "Jep"));

        get("/", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("title", "Keskustelupalsta");
            map.put("alueet", alueet);

            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());

        get("/alue/:webTunnus", (req, res) -> {
            String webTunnus = req.params(":webTunnus");

            HashMap map = new HashMap<>();
            map.put("title", webTunnus);
            map.put("keskustelut", keskustelut);

            return new ModelAndView(map, "alue");
        }, new ThymeleafTemplateEngine());

        get("/keskustelu/:tunnus", (req, res) -> {
            String tunnus = req.params(":tunnus");

            HashMap map = new HashMap<>();
            map.put("title", tunnus);
            map.put("viestit", viestit);

            return new ModelAndView(map, "keskustelu");
        }, new ThymeleafTemplateEngine());

        get("/vastaa/:keskustelunTunnus", (req, res) -> {
            int tunnus = muunna(req.params(":keskustelunTunnus"));
            Keskustelu keskustelu = keskusteluDao.findOne(tunnus);

            HashMap map = new HashMap<>();
            map.put("title", "Vastaa keskusteluun");
            map.put("keskustelu", keskustelu);

            return new ModelAndView(map, "vastaa");
        }, new ThymeleafTemplateEngine());
        
        post("/vastaa/:keskustelunTunnus", (req, res) -> {
            int tunnus = muunna(req.params(":keskustelunTunnus"));
            Keskustelu keskustelu = keskusteluDao.findOne(tunnus);
            
            // keskustelu.insert(...)

            return "Tämä tulee kutsumaan metodia keskustelu.insert...";
        });
        
        get("avaa", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("title", "Avaa keskustelu");
            
            return new ModelAndView(map, "avaa");
        }, new ThymeleafTemplateEngine());
    }

    private static int muunna(String merkkijono) {
        try {
            return Integer.parseInt(merkkijono);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

}
