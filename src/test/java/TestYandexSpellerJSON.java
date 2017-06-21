import org.junit.Test;

/**
 * Created by yulia-atlasova.
 */
public class TestYandexSpellerJSON {

    @Test
    public void worksWithOneWord(){
        YandexSpellerApi.with().text("notherr").callApi();
    }

}
