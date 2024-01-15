package hexlet.code.controllers;

import io.javalin.http.Context;

import java.sql.SQLException;
import java.util.Map;

public class RootController {
    public static void page(Context ctx) throws SQLException {
        var flash = ctx.consumeSessionAttribute("flash");
        var fType = ctx.consumeSessionAttribute("flash-type");

        flash = flash == null ? "" : flash;
        fType = fType == null ? "" : fType;
        ctx.render("index.jte", Map.of("flash", flash, "fType", fType));
    }
}

