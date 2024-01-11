@import hexlet.code.dto.urls.UrlsPg
@param UrlsPg page

@template.layout.page(
page = page,
content = @`
    <div class="container-lg mt-5">
        <h1>Сайты</h1>
        <table class="table table-bordered table-hover mt-3">
            <thead>
            <tr>
                <th class="col-1">ID</th>
                <th>Имя</th>
                <th class="col-2">Последняя проверка</th>
                <th class="col-1">Код ответа</th>
            </tr>
            </thead>
            @if(!page.getUrlList().isEmpty())
                <tbody>
                @for(var url : page.getUrlList())
                    <tr>
                        <td>
                            ${url.getId()}
                        </td>
                        <td>
                            <a href="urls/${url.getId()}">${url.getName()}</a>
                        </td>
                        <td>
                            @if(UrlsPg.getUrlWithLastCheck() != null)
                                ${String.valueOf(UrlsPg.getUrlWithLastCheck().getCreatedAt())}
                            @endif
                        </td>
                        <td>
                            @if(UrlsPg.getUrlWithLastCheck() != null)
                                ${UrlsPg.getUrlWithLastCheck().getStatusCode()}
                            @endif
                        </td>
                    </tr>
                @endfor
                </tbody>
            @endif
        </table>
    </div>

`
)