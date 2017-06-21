import beans.YandexSpellerResponse;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.lessThan;

/**
 * Created by yulia-atlasova.
 */
public class TestYandexSpellerJSON {

    private String wrongWord1 = "requisitee";
    private String rightWord1 = "requisite";

    @Test
    public void simpleApiCall() {
        RestAssured
                .given()
                .queryParam(YandexSpellerApi.PARAM_TEXT, wrongWord1)
                .parameters(YandexSpellerApi.PARAM_LANG, YandexSpellerApi.Languages.EN)
                .formParameter(YandexSpellerApi.PARAM_OPTIONS, "1")
                .and()
                .accept(ContentType.JSON)
                .auth().basic("abcName", "abcPassword")
                .body("some body payroll")
                .header("custom header1", "header1.value")
                .log().everything()
                .when().get(YandexSpellerApi.YANDEX_SPELLER_API_URI)
                .prettyPeek()
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body(Matchers.allOf(
                        Matchers.stringContainsInOrder(Arrays.asList(wrongWord1, rightWord1)),
                        Matchers.containsString("\"code\":1")))
                .and().assertThat()
                .contentType(ContentType.JSON)
                .time(lessThan(2000L)); // Milliseconds
    }

    @Test
    public void validateResponse() {
        YandexSpellerApi.with().text(wrongWord1).callApi()
        ;


    }


    @Test
    public void worksWithOneWord() {
        List<YandexSpellerResponse> responses =
                YandexSpellerApi.getYandexSpellerResp(
                        YandexSpellerApi.with().text("motherr+fatherr").callApi()
                                .then().statusCode(HttpStatus.SC_OK)
                                .body(Matchers.contains("mother"))

                                .extract().response());
        Assert.assertTrue(responses.get(0).word, equals("mom"));

    }

}
