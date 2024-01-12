package hexlet.code.dto.urls;

import hexlet.code.models.Url;
import hexlet.code.dto.BasePage;
import hexlet.code.models.UrlCheck;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.Optional;


@Getter
@AllArgsConstructor

public class UrlPage extends BasePage {
    private Url url;
    private List<UrlCheck> check;

}
