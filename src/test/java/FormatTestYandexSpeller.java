import beans.YandexSpellerAnswer;
import core.YandexSpellerApi;
import core.YandexSpellerConstants.Format;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

import static core.YandexSpellerConstants.Format.HTML;
import static core.YandexSpellerConstants.Format.PLAIN;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class FormatTestYandexSpeller {

    private final String WRONG_WORD = "sucsesful";
    private final String CORRECT = "successful";

    @DataProvider
    public Object[][] formatData() {
        String html = String.format("<p>%s</p>", WRONG_WORD);
        return new Object[][] { {html, HTML, 1}, {html, PLAIN, 2}, {WRONG_WORD, HTML, 1}};
    }

    @Test(dataProvider = "formatData")
    public void formatHtmlOptionTest(String text, Format format, int size) {
        List<YandexSpellerAnswer> answer = YandexSpellerApi.getYandexSpellerAnswers(
                YandexSpellerApi.with()
                        .text(text)
                        .format(format)
                        .callApi());
        assertThat(answer.size(), equalTo(size));
        answer.stream().filter(ans -> ans.word.equalsIgnoreCase(WRONG_WORD))
                .forEach(ans-> assertThat(ans.s, hasItem(CORRECT)));
    }

}
