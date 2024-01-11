package hexlet.code;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import hexlet.code.controllers.RootController;
import hexlet.code.controllers.UrlController;
import hexlet.code.repositories.BaseRepository;
import hexlet.code.routes.NamedRoutes;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinJte;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.sql.SQLException;

import static hexlet.code.AppUtils.getDataBaseUrl;
import static hexlet.code.AppUtils.createTemplateEngine;
import static hexlet.code.AppUtils.getResourceFileAsString;


@Slf4j
public class App {


    public static void main(String[] args) throws SQLException, IOException {
        var app = getApp();
        app.start(AppUtils.getPort());
    }

    public static Javalin getApp() throws IOException, SQLException {
        var hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(getDataBaseUrl());

        var dataSource = new HikariDataSource(hikariConfig);
        var sql = getResourceFileAsString("schema.sql");
//      conn
        log.info(sql);
        try (var connection = dataSource.getConnection();
             var statement = connection.createStatement()) {
            statement.execute(sql);
        }
        BaseRepository.dataSource = dataSource;

        var app = Javalin.create(config -> {
            config.plugins.enableDevLogging();
        });

        app.before(ctx -> {
            ctx.contentType("text/html; charset=utf-8");
        });

        //GET
        app.get(NamedRoutes.homePagePath(), RootController::page);
        app.get(NamedRoutes.sitesPath(), UrlController::urlList);
        app.get(NamedRoutes.sitePagePath("{id}"), UrlController::showUrl);
        //POST
        app.post(NamedRoutes.sitesPath(), UrlController::addUrl);
        app.post("/urls/{id}/checks", UrlController::checks);
        JavalinJte.init(createTemplateEngine());
        return app;

    }

}
