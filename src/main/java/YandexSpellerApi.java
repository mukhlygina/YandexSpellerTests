import beans.YandexSpellerResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

/**
 * Created by yulia_atlasova@epam.com.
 */
public class YandexSpellerApi {
    public static final String YANDEX_SPELLER_API_URI = "https://speller.yandex.net/services/spellservice.json/checkText";
    public static final String PARAM_TEXT = "text";
    public static final String PARAM_OPTIONS = "options";
    public static final String PARAM_LANG = "lang";

    enum Languages{
        RU("ru"),
        UK("uk"),
        EN("en");
        String languageCode;
        private Languages(String lang){
            this.languageCode=lang;
        }
    }

    private YandexSpellerApi() {
    }

    private HashMap<String, String> params = new HashMap<String, String>();

    public static class ApiBuilder {
        YandexSpellerApi spellerApi;

        private ApiBuilder(YandexSpellerApi gcApi) {
            spellerApi = gcApi;
        }

        public ApiBuilder text(String text) {
            spellerApi.params.put(PARAM_TEXT, text);
            return this;
        }

        public ApiBuilder options(String options) {
            spellerApi.params.put(PARAM_OPTIONS, options);
            return this;
        }

        public ApiBuilder language(String language) {
            spellerApi.params.put(PARAM_LANG, language);
            return this;
        }

        public Response callApi() {
            Response response = RestAssured.with()
                    .queryParams(spellerApi.params)
                    .log().all()
                    .get(YANDEX_SPELLER_API_URI).prettyPeek();
            return response;
        }
    }


    public static ApiBuilder with() {
        YandexSpellerApi gcApi = new YandexSpellerApi();
        return new ApiBuilder(gcApi);
    }

    public static List<YandexSpellerResponse> getYandexSpellerResp(Response response){
        Type respListType = new TypeToken<List<YandexSpellerResponse>>() {}.getType();
        return new Gson().fromJson(response.asString(), respListType);
    }
}
