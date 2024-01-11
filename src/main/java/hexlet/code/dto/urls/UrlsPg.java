package hexlet.code.dto.urls;

import hexlet.code.controllers.UrlController;
import hexlet.code.dto.BasePage;
import hexlet.code.models.Url;
import hexlet.code.models.UrlCheck;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Getter

@AllArgsConstructor
@ToString
public class UrlsPg extends BasePage {
    List<Url> urlList;
    Map<Long, UrlCheck> checkMap;

    public static UrlCheck urlWithLastCheck = UrlController.getLastCheck();

    public static UrlCheck getUrlWithLastCheck() {
        return urlWithLastCheck;
    }
}
