package core;

/**
 * Created by yulia_atlasova@epam.com on 22/06/2017.
 * Constants of YandexSpeller
 */
public class YandexSpellerConstants {

    //useful constants for API under test
    public static final String YANDEX_SPELLER_API_URI = "https://speller.yandex.net/services/spellservice.json/checkText";
    public static final String PARAM_TEXT = "text";
    public static final String PARAM_OPTIONS = "options";
    public static final String PARAM_LANG = "lang";
    public static final String WRONG_WORD_EN = "requisitee";
    public static final String RIGHT_WORD_EN = "requisite";
    public static final String WRONG_WORD_UK = "питаня";
    public static final String WORD_WITH_WRONG_CAPITAL = "moscow";
    public static final String WORD_WITH_LEADING_DIGITS = "12" + RIGHT_WORD_EN;
    public static final String PARAM_FORMAT = "format";
    public static final String REPEAT_WORDS = "я полетел на на Кипр";
    public static final String CHECK_URL = "https://tech.yandex.ru";


    public enum Languages {
        RU("ru"),
        UK("uk"),
        EN("en");
        String languageCode;

        private Languages(String lang) {
            this.languageCode = lang;
        }
    }

    public enum Format {
        PLAIN("plain"),
        HTML("html");

        String parameter;

        Format(String parameter) {
            this.parameter = parameter;
        }
    }

    public enum Option {
        DEFAULT("0"),
        IGNORE_DIGITS("2"),
        IGNORE_URLS("4"),
        FIND_REPEAT_WORDS("8"),
        IGNORE_CAPITALIZATION("512"),
        WRONG_OPTION("5");

        String optionNumber;

        Option(String optionNumber) {
            this.optionNumber = optionNumber;
        }
    }
}
