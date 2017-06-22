import beans.YandexSpellerAnswer;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import core.YandexSpellerApi;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static core.YandexSpellerConstants.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;


/**
 * Created by yulia-atlasova@epam.com.
 */
public class TestYandexSpellerJSON {

    // simple usage of RestAssured library: direct request call and response validations in test.
    @Test
    public void simpleSpellerApiCall() {
        RestAssured
                .given()
                .queryParam(PARAM_TEXT, wrongWordEn)
                .parameters(PARAM_LANG, Languages.EN)
                .formParameter(PARAM_OPTIONS, "1")
                .and()
                .accept(ContentType.JSON)
                .auth().basic("abcName", "abcPassword")
                .header("custom header1", "header1.value")
                .and()
                .body("some body payroll")
                .log().everything()
                .when().get(YANDEX_SPELLER_API_URI)
                .prettyPeek()
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body(Matchers.allOf(
                        Matchers.stringContainsInOrder(Arrays.asList(wrongWordEn, rightWordEn)),
                        Matchers.containsString("\"code\":1")))
                .contentType(ContentType.JSON)
                .time(lessThan(2000L)); // Milliseconds
    }

    // different http methods calls
    @Test
    public void spellerApiCallsWithDifferentMethods() {
        //GET
        RestAssured.given().param(PARAM_TEXT, wrongWordEn)
                .log().everything()
                .get(YANDEX_SPELLER_API_URI).prettyPeek();
        System.out.println("\n=====================================================================");

        //POST
        RestAssured.given().param(PARAM_TEXT, wrongWordEn)
                .log().everything()
                .post(YANDEX_SPELLER_API_URI).prettyPeek();
        System.out.println("\n=====================================================================");

        //HEAD
        RestAssured.given().param(PARAM_TEXT, wrongWordEn)
                .log().everything()
                .head(YANDEX_SPELLER_API_URI).prettyPeek();
        System.out.println("\n=====================================================================");

        //OPTIONS
        RestAssured.given().param(PARAM_TEXT, wrongWordEn)
                .log().everything()
                .options(YANDEX_SPELLER_API_URI).prettyPeek();
        System.out.println("\n=====================================================================");

        //PUT
        RestAssured.given().param(PARAM_TEXT, wrongWordEn)
                .log().everything()
                .put(YANDEX_SPELLER_API_URI).prettyPeek();
        System.out.println("\n=====================================================================");

        //PATCH
        RestAssured.given().param(PARAM_TEXT, wrongWordEn)
                .log().everything()
                .patch(YANDEX_SPELLER_API_URI).prettyPeek();
        System.out.println("\n=====================================================================");

        //DELETE
        RestAssured.given().param(PARAM_TEXT, wrongWordEn)
                .log().everything()
                .delete(YANDEX_SPELLER_API_URI).prettyPeek()
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
                .param(PARAM_TEXT, wrongWordEn)
                .get().prettyPeek()
                .then().specification(YandexSpellerApi.successResponse());
    }

    @Test
    public void reachBuilderUsage(){
                YandexSpellerApi.with()
                        .language(Languages.UK)
                        .options("5")
                        .text(wrongWordUK)
                        .callApi()
                .then().specification(YandexSpellerApi.successResponse());
    }


    //validate an object we've got in API response
    @Test
    public void validateSpellerAnswerAsAnObject() {
        List<YandexSpellerAnswer> answers =
                YandexSpellerApi.getYandexSpellerAnswers(
                        YandexSpellerApi.with().text("motherr+fatherr," + wrongWordEn).callApi());
        assertThat("expected number of answers is wrong.", answers.size(), equalTo(3));
        assertThat(answers.get(0).word, equalTo("motherr"));
        assertThat(answers.get(1).word, equalTo("fatherr"));
        assertThat(answers.get(0).s.get(0), equalTo("mother"));
        assertThat(answers.get(1).s.get(0), equalTo("father"));
        assertThat(answers.get(2).s.get(0), equalTo(rightWordEn));
    }

}
