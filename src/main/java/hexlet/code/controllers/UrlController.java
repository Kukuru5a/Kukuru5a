//package hexlet.code.controllers;
//
//import com.mashape.unirest.http.HttpResponse;
//import com.mashape.unirest.http.Unirest;
//import hexlet.code.dto.urls.UrlPage;
//import hexlet.code.dto.urls.UrlsPg;
//import hexlet.code.models.Url;
//import hexlet.code.models.UrlCheck;
//import hexlet.code.repositories.UrlCheckRepository;
//import hexlet.code.repositories.UrlRepository;
//import io.javalin.http.Context;
//import io.javalin.http.NotFoundResponse;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.sql.SQLException;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.List;
//
//public class UrlController {
//
//    public static void addUrl(Context ctx) throws SQLException {
//        String urlName;
//        urlName = ctx.formParam("url");
//        URL url;
//
//        try {
//            url = new URL(urlName);
//        } catch (MalformedURLException e) {
//            ctx.sessionAttribute("flash", "Некорректный URL");
//            ctx.sessionAttribute("flash-type", "danger");
//            ctx.redirect("/");
//            return;
//        }
//
//        String normalizedUrl = url.getProtocol() + "://" + url.getAuthority();
//        Url urlFromDb = UrlRepository.findById(normalizedUrl);
//
//        if (urlFromDb != null) {
//            ctx.sessionAttribute("flash", "Страница уже существует");
//            ctx.sessionAttribute("flash-type", "info");
//            ctx.redirect("/urls");
//            return;
//        }
//
//        Url urlForSave = new Url(normalizedUrl);
//        UrlRepository.save(urlForSave);
//
//        ctx.sessionAttribute("flash", "Страница успешно добавлена");
//        ctx.sessionAttribute("flash-type", "success");
//        ctx.redirect("/urls");
//    }
//
//    //adding list OK
//    public static void urlList(Context ctx) throws SQLException {
////        List<Url> urls = UrlRepository.getEntities();
////        var urlChecks = UrlCheckRepository.findLatestChecks();
////        var page = new UrlsPg(urls, urlChecks);
////        page.setFlash(ctx.consumeSessionAttribute("flash"));
////        page.setColor(ctx.consumeSessionAttribute("flash-type"));
////        ctx.render("urls/urls.jte", Collections.singletonMap("page", page));
//        var urlsWithLastChecks = new HashMap<Url, UrlCheck>();
//        var urls = UrlRepository.getEntities();
//        for (var url : urls) {
//            UrlCheckRepository.find(url.getId()).ifPresentOrElse(
//                    (check) -> urlsWithLastChecks.put(url, check),
//                    () -> urlsWithLastChecks.put(url, null));
//        }
//        var page = new UrlsPg(urlsWithLastChecks);
//        page.setFlash(ctx.consumeSessionAttribute("flash"));
//        page.setColor(ctx.consumeSessionAttribute("color"));
//        ctx.render("urls/index.jte", Collections.singletonMap("page", page));
//    }
//
//
//    public static void showUrl(Context ctx) throws SQLException {
//        var id = ctx.pathParamAsClass("id", Long.class).get();
//        var url = UrlRepository.find(id)
//                .orElseThrow(() -> new NotFoundResponse("Entity with id " + id + " is not defined"));
//        //new
//        var checkedUrl = UrlCheckRepository.getEntitiesByUrl(url.getId());
//        //was
//        var page = new UrlPage(url, checkedUrl);
//        //new
//        page.setFlash(ctx.consumeSessionAttribute("flash"));
//        page.setColor(ctx.consumeSessionAttribute("flash-type"));
//        ctx.render("urls/show.jte", Collections.singletonMap("page", page));
//    }
//
//    public static void checks(Context ctx) throws SQLException {
//        Long id = ctx.pathParamAsClass("id", Long.class).getOrDefault(null);
//        var url = UrlRepository.find(id)
//                .orElseThrow(() -> new NotFoundResponse("Entity with id " + id + " not found"));
//
//        UrlCheckRepository.save(getCheck(url, ctx));
//
//        ctx.attribute("url", url);
//        ctx.redirect("/urls/" + id);
//    }
//
//    public static UrlCheck getCheck(Url url, Context ctx) {
//        String checkedUrlName = url.getName();
//        HttpResponse<String> urlResponse = null;
//        String urlH1Value = "";
//        String urlTitle = "";
//        String urlDescription = "";
//        int urlStatusCode = 0;
//
//        try {
//            urlResponse = Unirest
//                    .get(checkedUrlName)
//                    .asString();
//            urlStatusCode = urlResponse.getCode();
//            Document urlDoc = Jsoup.parse(urlResponse.getBody());
//
//            if (urlDoc.select("h1").first() != null) {
//                urlH1Value = urlDoc.select("h1").first().text();
//            }
//
//            if (urlDoc.select("title").first() != null) {
//                urlTitle = urlDoc.select("title").first().text();
//            }
//
//            if (!urlDoc.select("meta[name=description]").isEmpty()) {
//                urlDescription = urlDoc.select("meta[name=description]")
//                        .get(0)
//                        .attr("content");
//            }
//
//            ctx.sessionAttribute("flash", "Проверка добавлена");
//            ctx.sessionAttribute("flash-type", "success");
//            ctx.redirect("/");
//
//            return new UrlCheck(urlStatusCode, urlTitle, urlH1Value, urlDescription, url.getId());
//        } catch (Exception e) {
//
//            ctx.sessionAttribute("flash", "Ошибка при проверке URL");
//            ctx.sessionAttribute("flash-type", "danger");
//            ctx.redirect("/");
//
//            return new UrlCheck(urlStatusCode, urlTitle, urlH1Value, urlDescription, url.getId());
//        }
//    }
//
////    public static UrlCheck getLastCheck() {
////        Optional<UrlCheck> checks;
////        try {
////            checks = UrlCheckRepository.find(UrlCheckRepository.getId());
////        } catch (SQLException e) {
////            throw new RuntimeException(e);
////        }
////        return checks.orElse(null);
////    }
//}
package hexlet.code.controllers;

import hexlet.code.routes.NamedRoutes;
import hexlet.code.dto.urls.UrlPage;
import hexlet.code.dto.urls.UrlsPg;
import hexlet.code.models.Url;
import hexlet.code.models.UrlCheck;
import hexlet.code.repositories.UrlCheckRepository;
import hexlet.code.repositories.UrlRepository;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;

public class UrlController {
    public static void showUrlList(Context ctx) throws SQLException {
        var urlsWithLastChecks = new HashMap<Url, UrlCheck>();
        var urls = UrlRepository.getUrls();
        for (var url : urls) {
            UrlCheckRepository.getLastCheck(url.getId()).ifPresentOrElse(
                    (check) -> urlsWithLastChecks.put(url, check),
                    () -> urlsWithLastChecks.put(url, null));
        }
        var page = new UrlsPg(urlsWithLastChecks);
        page.setFlash(ctx.consumeSessionAttribute("flash"));
        page.setColor(ctx.consumeSessionAttribute("color"));
        ctx.render("urls/urls.jte", Collections.singletonMap("page", page));
    }

    public static void showUrl(Context ctx) throws SQLException {
        var id = ctx.pathParamAsClass("id", Long.class).getOrDefault(null);
        var url = UrlRepository.findById(id)
                .orElseThrow(() -> new NotFoundResponse("Url with id = " + id + " not found"));
        var urlChecks = UrlCheckRepository.getUrlChecks(id);
        var page = new UrlPage(url, urlChecks);
        page.setFlash(ctx.consumeSessionAttribute("flash"));
        page.setColor(ctx.consumeSessionAttribute("color"));
        ctx.render("urls/show.jte", Collections.singletonMap("page", page));
    }

    public static void createUrl(Context ctx) throws SQLException {
        var inputName = ctx.formParamAsClass("url", String.class).getOrDefault(null);
        URL inputUrl = null;
        try {
            inputUrl = new URL(inputName);
        } catch (MalformedURLException e) {
            ctx.sessionAttribute("flash", "Некорректный URL");
            ctx.sessionAttribute("color", "danger");
            ctx.redirect(NamedRoutes.mainPath());
        }
        if (inputUrl != null) {
            String protocol = inputUrl.getProtocol();
            String authority = inputUrl.getAuthority();
            var name = String.format("%s://%s", protocol, authority);
            var url = new Url(name);
            var uniqueness = UrlRepository.getUrls().stream()
                    .noneMatch(entity -> entity.getName().equals(name));
            if (uniqueness) {
                UrlRepository.save(url);
                ctx.sessionAttribute("flash", "Страница успешно добавлена");
                ctx.sessionAttribute("color", "success");
                ctx.redirect(NamedRoutes.urlsPath());
            } else {
                ctx.sessionAttribute("flash", "Страница уже существует");
                ctx.sessionAttribute("color", "info");
                ctx.redirect(NamedRoutes.urlsPath());
            }
        }
    }
}
