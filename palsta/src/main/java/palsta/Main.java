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
        // 1. Käynnistä projekti Netbeansissa
        // 2. Mene selaimella osoitteeseen http://localhost:4567/
        Database db = new Database("jdbc:sqlite:../tietokannat/keskustelut.db");
        AlueDao alueDao = new AlueDao(db);
        KeskusteluDao keskusteluDao = new KeskusteluDao(db);
        ViestiDao viestiDao = new ViestiDao(db);
        Calendar calendar = Calendar.getInstance();

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
            int tunnus = muunna(req.queryParams("keskustelu"), -1);

            String lahettaja = req.queryParams("nimimerkki");
            String viesti = req.queryParams("viesti");
            //java.util.Date date= new java.util.Date();

            java.util.Date now = calendar.getTime();
            java.sql.Timestamp timestamp = new java.sql.Timestamp(now.getTime());

            viestiDao.insert(tunnus, lahettaja, timestamp, viesti);
            res.redirect("/keskustelu/" + tunnus);
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

        post("avaa", (req, res) -> {   //// KESKUSTELU
            String webTunnus = req.queryParams("alueenWebTunnus");
            String otsikko = req.queryParams("otsikko");
            String nimimerkki = req.queryParams("nimimerkki");
            String viesti = req.queryParams("viesti");
            int alueTunnus = alueDao.findOne(webTunnus).getTunnus();

            java.util.Date now = calendar.getTime();
            java.sql.Timestamp timestamp = new java.sql.Timestamp(now.getTime());

            keskusteluDao.insert(alueTunnus, otsikko);

            viestiDao.insert(keskusteluDao.findPrimaryKey(otsikko), nimimerkki, timestamp, viesti);

            //res.redirect("/" + webTunnus);
            return nimimerkki + ": " + otsikko + ", " + viesti + " (" + webTunnus + ")";
        });

    }

    private static int muunna(String merkkijono, int oletusarvo) {
        try {
            return Integer.parseInt(merkkijono);
        } catch (NumberFormatException e) {
            return oletusarvo;
        }
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
