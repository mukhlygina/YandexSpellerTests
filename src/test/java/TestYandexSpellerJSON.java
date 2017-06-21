import beans.YandexSpellerAnswer;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.lessThan;

/**
 * Created by yulia-atlasova@epam.com.
 */
public class TestYandexSpellerJSON {

    private String wrongWord1 = "requisitee";
    private String rightWord1 = "requisite";

    // simple usage of RestAssured library: direct request call and response validations in test.
    @Test
    public void simpleSpellerApiCall() {
        RestAssured
                .given()
                .queryParam(YandexSpellerApi.PARAM_TEXT, wrongWord1)
                .parameters(YandexSpellerApi.PARAM_LANG, Languages.EN)
                .formParameter(YandexSpellerApi.PARAM_OPTIONS, "1")
                .and()
                .accept(ContentType.JSON)
                .auth().basic("abcName", "abcPassword")
                .header("custom header1", "header1.value")
                .and()
                .body("some body payroll")
                .log().everything()
                .when().get(YandexSpellerApi.YANDEX_SPELLER_API_URI)
                .prettyPeek()
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body(Matchers.allOf(
                        Matchers.stringContainsInOrder(Arrays.asList(wrongWord1, rightWord1)),
                        Matchers.containsString("\"code\":1")))
                .contentType(ContentType.JSON)
                .time(lessThan(2000L)); // Milliseconds
    }

    // different http methods calls
    @Test
    public void spellerApiCallsWithDifferentMethods() {
        //GET
        RestAssured.given().param(YandexSpellerApi.PARAM_TEXT, wrongWord1)
                .log().everything()
                .get(YandexSpellerApi.YANDEX_SPELLER_API_URI).prettyPeek();
        System.out.println("\n=====================================================================");

        //POST
        RestAssured.given().param(YandexSpellerApi.PARAM_TEXT, wrongWord1)
                .log().everything()
                .post(YandexSpellerApi.YANDEX_SPELLER_API_URI).prettyPeek();
        System.out.println("\n=====================================================================");

        //HEAD
        RestAssured.given().param(YandexSpellerApi.PARAM_TEXT, wrongWord1)
                .log().everything()
                .head(YandexSpellerApi.YANDEX_SPELLER_API_URI).prettyPeek();
        System.out.println("\n=====================================================================");

        //OPTIONS
        RestAssured.given().param(YandexSpellerApi.PARAM_TEXT, wrongWord1)
                .log().everything()
                .options(YandexSpellerApi.YANDEX_SPELLER_API_URI).prettyPeek();
        System.out.println("\n=====================================================================");

        //PUT
        RestAssured.given().param(YandexSpellerApi.PARAM_TEXT, wrongWord1)
                .log().everything()
                .put(YandexSpellerApi.YANDEX_SPELLER_API_URI).prettyPeek();
        System.out.println("\n=====================================================================");

        //PATCH
        RestAssured.given().param(YandexSpellerApi.PARAM_TEXT, wrongWord1)
                .log().everything()
                .patch(YandexSpellerApi.YANDEX_SPELLER_API_URI).prettyPeek();
        System.out.println("\n=====================================================================");

        //DELETE
        RestAssured.given().param(YandexSpellerApi.PARAM_TEXT, wrongWord1)
                .log().everything()
                .delete(YandexSpellerApi.YANDEX_SPELLER_API_URI).prettyPeek()
                .then()
//                .body(isEmptyOrNullString())
                .statusCode(HttpStatus.SC_METHOD_NOT_ALLOWED)
                .statusLine("HTTP/1.1 405 Method not allowed");
    }


    // use base request and response specifications to form request and validate response.
    @Test
    public void useBaseRequestAndResponseSpecifications() {
        RestAssured
                .given(YandexSpellerApi.baseRequestConfiguration())
                .param(YandexSpellerApi.PARAM_TEXT, wrongWord1)
                .get().prettyPeek()
                .then().specification(YandexSpellerApi.successResponse());
    }


    //validate an object we've got in API response
    @Test
    public void validateSpellerAnswerAsAnObject() {
        List<YandexSpellerAnswer> answers =
                YandexSpellerApi.getYandexSpellerAnswers(
                        YandexSpellerApi.with().text("motherr+fatherr," + wrongWord1).callApi());
        assertThat("expected number of answers is wrong.", answers.size(), equalTo(3));
        assertThat(answers.get(0).word, equalTo("motherr"));
        assertThat(answers.get(1).word, equalTo("fatherr"));
        assertThat(answers.get(0).s.get(0), equalTo("mother"));
        assertThat(answers.get(1).s.get(0), equalTo("father"));
        assertThat(answers.get(2).s.get(0), equalTo(rightWord1));
    }

}
