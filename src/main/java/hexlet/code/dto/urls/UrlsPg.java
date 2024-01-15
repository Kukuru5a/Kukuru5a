package hexlet.code.dto.urls;

import hexlet.code.dto.BasePage;
import hexlet.code.models.Url;
import hexlet.code.models.UrlCheck;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;


@AllArgsConstructor
@Getter
@ToString
public class UrlsPg extends BasePage {
    Map<Url, UrlCheck> checkMap;

    //    public static UrlCheck urlWithLastCheck = UrlChecksController.getLastCheck();

//    public static UrlCheck getUrlWithLastCheck() {
//        return urlWithLastCheck;
//    }
}
