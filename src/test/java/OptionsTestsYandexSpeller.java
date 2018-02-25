import beans.YandexSpellerAnswer;
import core.YandexSpellerApi;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static core.YandexSpellerConstants.*;
import java.util.List;

import static core.YandexSpellerConstants.CHECK_URL;
import static core.YandexSpellerConstants.Option.*;
import static core.YandexSpellerConstants.REPEAT_WORDS;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.core.IsEqual.equalTo;

public class OptionsTestsYandexSpeller {
    @DataProvider
    public Object[][] defaultOptionData() {
        return new Object[][] {{WORD_WITH_WRONG_CAPITAL, 1}, {RIGHT_WORD_EN, 0}, {WRONG_WORD_EN, 1},
                {WORD_WITH_LEADING_DIGITS, 1}, {REPEAT_WORDS, 0}};
    }

    @Test(dataProvider = "defaultOptionData")
    public void defualtOptionTest(String text, Integer size) {
        List<YandexSpellerAnswer> answer = YandexSpellerApi.getYandexSpellerAnswers(
                YandexSpellerApi.with()
                        .text(text)
                        .options(DEFAULT)
                        .callApi());
        assertThat(answer.size(), equalTo(size));
    }

    @Test
    public void ignoreURLSOptionTest() {
        List<YandexSpellerAnswer> answer = YandexSpellerApi.getYandexSpellerAnswers(
                YandexSpellerApi.with()
                        .text(CHECK_URL)
                        .options(IGNORE_URLS)
                        .callApi());
        assertThat(answer.size(), equalTo(0));

    }

    @Test
    public void ignoreDigitsOptionTest() {
        List<YandexSpellerAnswer> answer = YandexSpellerApi.getYandexSpellerAnswers(
                YandexSpellerApi.with()
                        .text(WORD_WITH_LEADING_DIGITS)
                        .options(IGNORE_DIGITS)
                        .callApi());
        assertThat(answer.size(), equalTo(0));
    }

    @Test
    public void findRepeatWordsOptionTest() {
        List<YandexSpellerAnswer> answer = YandexSpellerApi.getYandexSpellerAnswers(
                YandexSpellerApi.with()
                        .text(REPEAT_WORDS)
                        .options(FIND_REPEAT_WORDS)
                        .callApi());
        assertThat(answer.size(), equalTo(1));
    }

    @Test
    public void ignoreCapitalizationOptionTest() {
        List<YandexSpellerAnswer> answer = YandexSpellerApi.getYandexSpellerAnswers(
                YandexSpellerApi.with()
                        .text(WORD_WITH_WRONG_CAPITAL)
                        .options(IGNORE_CAPITALIZATION)
                        .callApi());
        assertThat(answer.size(), equalTo(0));
    }

    @Test
    public void wrongOptionTest() {
        List<YandexSpellerAnswer> answer = YandexSpellerApi.getYandexSpellerAnswers(
                YandexSpellerApi.with()
                        .text(WRONG_WORD_EN)
                        .options(WRONG_OPTION)
                        .callApi());
        assertThat(answer.size(), equalTo(1));
    }
}
