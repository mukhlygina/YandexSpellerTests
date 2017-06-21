/**
 * Created by yulia_atlasova@epam.com on 21/06/2017.
 *
 */


enum Languages {
    RU("ru"),
    UK("uk"),
    EN("en");
    String languageCode;

    private Languages(String lang) {
        this.languageCode = lang;
    }
}
