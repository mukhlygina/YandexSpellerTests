import beans.YandexSpellerAnswer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.builder.RequestSpecBuilder;
import com.jayway.restassured.builder.ResponseSpecBuilder;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.jayway.restassured.specification.ResponseSpecification;
import org.apache.http.HttpStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

import static org.hamcrest.Matchers.lessThan;

/**
 * Created by yulia_atlasova@epam.com.
 */
class YandexSpellerApi {

    //useful constants for API under test
    static final String YANDEX_SPELLER_API_URI = "https://speller.yandex.net/services/spellservice.json/checkText";
    static final String PARAM_TEXT = "text";
    static final String PARAM_OPTIONS = "options";
    static final String PARAM_LANG = "lang";

    private YandexSpellerApi() {
    }

    //builder pattern
    private HashMap<String, String> params = new HashMap<String, String>();

    public static class ApiBuilder {
        YandexSpellerApi spellerApi;

        private ApiBuilder(YandexSpellerApi gcApi) {
            spellerApi = gcApi;
        }

        ApiBuilder text(String text) {
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

        Response callApi() {
            return RestAssured.with()
                    .queryParams(spellerApi.params)
                    .log().all()
                    .get(YANDEX_SPELLER_API_URI).prettyPeek();
        }
    }


    static ApiBuilder with() {
        YandexSpellerApi gcApi = new YandexSpellerApi();
        return new ApiBuilder(gcApi);
    }

    //get ready Speller answers list form api response
    static List<YandexSpellerAnswer> getYandexSpellerAnswers(Response response){
        return new Gson().fromJson(response.asString(), new TypeToken<List<YandexSpellerAnswer>>() {}.getType());
    }

    //set base request and response specifications tu use in tests
    static ResponseSpecification successResponse(){
        return new ResponseSpecBuilder()
                .expectContentType(ContentType.JSON)
                .expectHeader("Connection", "keep-alive")
                .expectResponseTime(lessThan(2000L))
                .expectStatusCode(HttpStatus.SC_OK)
                .build();
    }

    static RequestSpecification baseRequestConfiguration(){
        return new RequestSpecBuilder()
                .setAccept(ContentType.XML)
                .addHeader("custom header2", "header2.value")
                .addQueryParam("requestID", new Random().nextLong())
                .setBaseUri(YANDEX_SPELLER_API_URI)
                .build();
    }

}
