package palsta;

import java.sql.*;
import java.util.*;
import palsta.pojo.*;
import palsta.db.*;
import spark.ModelAndView;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

public class Main {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        // 1. Käynnistä projekti Netbeansissa
        // 2. Mene selaimella osoitteeseen http://localhost:4567/
        Database db = new Database("jdbc:sqlite:../tietokannat/keskustelut.db");
        AlueDao alueDao = new AlueDao(db);
        KeskusteluDao keskusteluDao = new KeskusteluDao(db);
        ViestiDao viestiDao = new ViestiDao(db);

        List<Alue> alueet = alueDao.findAll();

        get("/", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("title", "Keskustelupalsta");
            map.put("alueet", alueet);

            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());

        get("/alue/:webTunnus", (req, res) -> {
            String webTunnus = req.params(":webTunnus");
            List<Keskustelu> keskustelut = keskusteluDao.findTenNewest(webTunnus);

            HashMap map = new HashMap<>();
            map.put("title", alueDao.findOne(webTunnus).getNimi());
            map.put("keskustelut", keskustelut);
            map.put("alueenWebTunnus", webTunnus);

            return new ModelAndView(map, "alue");
        }, new ThymeleafTemplateEngine());

        get("/keskustelu/:tunnus", (req, res) -> {
            int tunnus = muunna(req.params(":tunnus"));
            List<Viesti> viestit = viestiDao.findConvosMessages(tunnus);

            HashMap map = new HashMap<>();
            map.put("title", keskusteluDao.findOne(tunnus).getOtsikko());
            map.put("viestit", viestit);
            map.put("keskustelunTunnus", tunnus);

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

        post("/vastaa", (req, res) -> {
            int tunnus = muunna(req.queryParams("keskustelunTunnus"));
            String lahettaja = req.queryParams("nimimerkki");
            String viesti = req.queryParams("viesti");

            Keskustelu keskustelu = keskusteluDao.findOne(tunnus);

            // viestiDao.insert(keskustelunTunnus, lahettaja, sisalto)
            return lahettaja + ": " + viesti + " (keskustelu " + tunnus + ")";
        });

        get("avaa/:alueenWebTunnus", (req, res) -> {
            String webTunnus = req.params(":alueenWebTunnus");

            Alue alue = alueDao.findOne(webTunnus);

            HashMap map = new HashMap<>();
            map.put("title", "Avaa keskustelu");
            map.put("alue", alue);

            return new ModelAndView(map, "avaa");
        }, new ThymeleafTemplateEngine());

        post("avaa", (req, res) -> {
            String webTunnus = req.queryParams("alueenWebTunnus");
            String otsikko = req.queryParams("otsikko");
            String nimimerkki = req.queryParams("nimimerkki");
            String viesti = req.queryParams("viesti");

            // keskusteluDao.insert(alueenWebTunnus, otsikko, lahettaja, sisalto)
            return nimimerkki + ": " + otsikko + ", " + viesti + " (" + webTunnus + ")";
        });
    }

    private static int muunna(String merkkijono) {
        try {
            return Integer.parseInt(merkkijono);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

}
