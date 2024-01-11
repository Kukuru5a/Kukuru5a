package hexlet.code.routes;

public class NamedRoutes {
    public static String sitesPath() {
        return "/urls";
    }
    public static String sitePagePath(String id) {
        return "/urls/" + id;
    }

    public static String sitePagePath(Long id) {
        return "/urls" + sitePagePath(String.valueOf(id));
    }

    public static String homePagePath() {
        return "/";
    }


}
