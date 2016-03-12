package palsta;

import java.sql.*;
import java.util.*;
import palsta.pojo.*;
import palsta.db.*;
import spark.ModelAndView;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

public class Main {

    public static final int keskusteluitaSivulla = 10;
    public static final int viestejaSivulla = 10;

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        // Osoite: http://keskustelupalsta.herokuapp.com/
        // Paikallinen versio: http://localhost:4567/)

        // Asetetaan portti, jos heroku antaa PORT-ympäristömuuttujan
        if (System.getenv("PORT") != null) {
            port(Integer.valueOf(System.getenv("PORT")));
        }

        // Käytetään oletuksena paikallista sqlite-tietokantaa
        // Jos heroku antaa käyttöömme tietokantaosoitteen, otetaan se käyttöön
        String jdbcOsoite = "jdbc:sqlite:tietokannat/keskustelut.db";
        if (System.getenv("DATABASE_URL") != null) {
            jdbcOsoite = System.getenv("DATABASE_URL");
        }

        Database db = new Database(jdbcOsoite);
        AlueDao alueDao = new AlueDao(db);
        KeskusteluDao keskusteluDao = new KeskusteluDao(db);
        ViestiDao viestiDao = new ViestiDao(db);

        get("/", (req, res) -> {
            List<Alue> alueet = alueDao.findAll();
            HashMap map = new HashMap<>();
            map.put("title", "Keskustelupalsta");
            map.put("alueet", alueet);

            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());

        get("/alue/:webTunnus", (req, res) -> {
            String webTunnus = req.params(":webTunnus");
            Alue alue = alueDao.findOne(webTunnus);
            int nykyinenSivu = muunna(req.queryParams("sivu"), 1);
            int offset = keskusteluitaSivulla * (nykyinenSivu - 1);
            int yhteensa = alueDao.countDiscussions(alue.getTunnus());
            List<Keskustelu> keskustelut = keskusteluDao.findOffset(webTunnus, keskusteluitaSivulla, offset);
            List<Integer> sivut = listaaSivut(yhteensa, keskusteluitaSivulla);

            HashMap map = new HashMap<>();
            map.put("title", alue.getNimi());
            map.put("keskustelut", keskustelut);
            map.put("alueenWebTunnus", webTunnus);

            lisaaSivujenVaihdonMuuttujat(map, sivut, nykyinenSivu);

            return new ModelAndView(map, "alue");
        }, new ThymeleafTemplateEngine());

        get("/keskustelu/:tunnus", (req, res) -> {
            int tunnus = muunna(req.params(":tunnus"), -1);
            Keskustelu keskustelu = keskusteluDao.findOne(tunnus);
            int nykyinenSivu = muunna(req.queryParams("sivu"), 1);
            int offset = viestejaSivulla * (nykyinenSivu - 1);
            int yhteensa = keskustelu.getViestejaYhteensa();
            List<Viesti> viestit = viestiDao.findDiscussion(tunnus, viestejaSivulla, offset);
            List<Integer> sivut = listaaSivut(yhteensa, viestejaSivulla);

            HashMap map = new HashMap<>();
            map.put("title", keskustelu.getOtsikko());
            map.put("viestit", viestit);
            map.put("keskustelu", tunnus);

            lisaaSivujenVaihdonMuuttujat(map, sivut, nykyinenSivu);

            return new ModelAndView(map, "keskustelu");
        }, new ThymeleafTemplateEngine());

        get("/vastaa/:keskustelu", (req, res) -> {
            int tunnus = muunna(req.params(":keskustelu"), -1);
            Keskustelu keskustelu = keskusteluDao.findOne(tunnus);

            HashMap map = new HashMap<>();
            map.put("title", "Vastaa keskusteluun");
            map.put("keskustelu", keskustelu);

            return new ModelAndView(map, "vastaa");
        }, new ThymeleafTemplateEngine());

        post("/vastaa", (req, res) -> {
            int keskustelu = muunna(req.queryParams("keskustelu"), -1);

            String lahettaja = req.queryParams("nimimerkki");
            String viesti = req.queryParams("viesti");

            viestiDao.insert(keskustelu, lahettaja, now(), viesti);

            res.redirect("keskustelu/" + keskustelu);
            return lahettaja + ": " + viesti + " (keskustelu " + keskustelu + ")";
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
            String lahettaja = req.queryParams("nimimerkki");
            String viesti = req.queryParams("viesti");
            int alue = alueDao.findOne(webTunnus).getTunnus();

            int keskustelu = keskusteluDao.insert(alue, otsikko);

            if (keskustelu != -1) {
                viestiDao.insert(keskustelu, lahettaja, now(), viesti);
            }

            res.redirect("keskustelu/" + keskustelu);
            return lahettaja + ": " + otsikko + ", " + viesti + " (" + webTunnus + ")";
        });

    }

    private static int muunna(String merkkijono, int oletusarvo) {
        try {
            return Integer.parseInt(merkkijono);
        } catch (NumberFormatException e) {
            return oletusarvo;
        }
    }

    private static java.sql.Timestamp now() {
        TimeZone zone = TimeZone.getTimeZone("Europe/Helsinki");
        Calendar calendar = new GregorianCalendar(zone);
        return new java.sql.Timestamp(calendar.getTime().getTime());
    }

    private static List<Integer> listaaSivut(int alkioita, int alkioitaPerSivu) {
        List<Integer> sivut = new ArrayList<>();
        int sivu = 1;
        while (alkioita > 0) {
            sivut.add(sivu);
            sivu++;
            alkioita -= alkioitaPerSivu;
        }

        return sivut;
    }

    private static void lisaaSivujenVaihdonMuuttujat(HashMap map, List<Integer> sivut, int nykyinenSivu) {
        map.put("naytaSivujenNavigointi", true);
        map.put("sivut", sivut);
        map.put("nykyinenSivu", nykyinenSivu);
        map.put("edellinenSivu", Math.max(1, Math.min(sivut.size(), nykyinenSivu - 1)));
        map.put("seuraavaSivu", Math.max(1, Math.min(sivut.size(), nykyinenSivu + 1)));
        map.put("onkoEnsimmainenSivu", nykyinenSivu <= 1);
        map.put("onkoViimeinenSivu", nykyinenSivu >= sivut.size());
    }

}
