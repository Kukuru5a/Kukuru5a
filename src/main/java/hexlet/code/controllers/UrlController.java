package hexlet.code.controllers;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import hexlet.code.dto.urls.UrlPage;
import hexlet.code.dto.urls.UrlsPg;
import hexlet.code.models.Url;
import hexlet.code.models.UrlCheck;
import hexlet.code.repositories.UrlCheckRepository;
import hexlet.code.repositories.UrlRepository;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class UrlController {

    public static void addUrl(Context ctx) throws SQLException {
        String urlName;
        urlName = ctx.formParam("url");
        URL url;

        try {
            url = new URL(urlName);
        } catch (MalformedURLException e) {
            ctx.sessionAttribute("flash", "Некорректный URL");
            ctx.sessionAttribute("flash-type", "danger");
            ctx.redirect("/");
            return;
        }

        String normalizedUrl = url.getProtocol() + "://" + url.getAuthority();
        Url urlFromDb = UrlRepository.findByName(normalizedUrl);

        if (urlFromDb != null) {
            ctx.sessionAttribute("flash", "Страница уже существует");
            ctx.sessionAttribute("flash-type", "info");
            ctx.redirect("/urls");
            return;
        }

        Url urlForSave = new Url(normalizedUrl);
        UrlRepository.save(urlForSave);

        ctx.sessionAttribute("flash", "Страница успешно добавлена");
        ctx.sessionAttribute("flash-type", "success");
        ctx.redirect("/urls");
    }

    //adding list OK
    public static void urlList(Context ctx) throws SQLException {
        List<Url> urls = UrlRepository.getEntities();
        var urlChecks = UrlCheckRepository.findLatestChecks();
        var page = new UrlsPg(urls, urlChecks);
        page.setFlash(ctx.consumeSessionAttribute("flash"));
        page.setColor(ctx.consumeSessionAttribute("flash-type"));
        ctx.render("urls/urls.jte", Collections.singletonMap("page", page));
    }


    public static void showUrl(Context ctx) throws SQLException {
        var id = ctx.pathParamAsClass("id", Long.class).get();
        var url = UrlRepository.find(id)
                .orElseThrow(() -> new NotFoundResponse("Entity with id " + id + " is not defined"));
        //new
        var checkedUrl = UrlCheckRepository.getEntitiesByUrl(url.getId());
        //was
        var page = new UrlPage(url, checkedUrl);
        //new
        page.setFlash(ctx.consumeSessionAttribute("flash"));
        page.setColor(ctx.consumeSessionAttribute("flash-type"));
        ctx.render("urls/show.jte", Collections.singletonMap("page", page));
    }

    public static void checks(Context ctx) throws SQLException {
        Long id = ctx.pathParamAsClass("id", Long.class).getOrDefault(null);
        var url = UrlRepository.find(id)
                .orElseThrow(() -> new NotFoundResponse("Entity with id " + id + " not found"));

        UrlCheckRepository.save(getCheck(url, ctx));

        ctx.attribute("url", url);
        ctx.redirect("/urls/" + id);
    }

    public static UrlCheck getCheck(Url url, Context ctx) {
        String checkedUrlName = url.getName();
        HttpResponse<String> urlResponse = null;
        String urlH1Value = "";
        String urlTitle = "";
        String urlDescription = "";
        int urlStatusCode = 0;

        try {
            urlResponse = Unirest
                    .get(checkedUrlName)
                    .asString();
            urlStatusCode = urlResponse.getCode();
            Document urlDoc = Jsoup.parse(urlResponse.getBody());

            if (urlDoc.select("h1").first() != null) {
                urlH1Value = urlDoc.select("h1").first().text();
            }

            if (urlDoc.select("title").first() != null) {
                urlTitle = urlDoc.select("title").first().text();
            }

            if (!urlDoc.select("meta[name=description]").isEmpty()) {
                urlDescription = urlDoc.select("meta[name=description]")
                        .get(0)
                        .attr("content");
            }

            ctx.sessionAttribute("flash", "Проверка добавлена");
            ctx.sessionAttribute("flash-type", "success");
            ctx.redirect("/");

            return new UrlCheck(urlStatusCode, urlTitle, urlH1Value, urlDescription, url.getId());
        } catch (Exception e) {

            ctx.sessionAttribute("flash", "Ошибка при проверке URL");
            ctx.sessionAttribute("flash-type", "danger");
            ctx.redirect("/");

            return new UrlCheck(urlStatusCode, urlTitle, urlH1Value, urlDescription, url.getId());
        }
    }

    public static UrlCheck getLastCheck() {
        Optional<UrlCheck> checks;
        try {
            checks = UrlCheckRepository.find(Url.getId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return checks.orElse(null);
    }
}
