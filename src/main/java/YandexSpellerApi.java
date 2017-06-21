import com.google.gson.Gson;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;

import java.util.HashMap;

/**
 * Created by yulia_atlasova@epam.com.
 */
public class YandexSpellerApi {
    private static final String YANDEX_SPELLER_API_URI = "http://speller.yandex.net/services/spellservice.json/checkText";
    private static final String PARAM_TEXT = "text";
    private static final String PARAM_OPTIONS = "options";
    private static final String PARAM_LANG = "lang";


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

   // public static GeoCoderResponse getGeoCoderJsonFromResp(Response response){
     //   return new Gson().fromJson(response.asString(), GeoCoderResponse.class);
   // }
}
