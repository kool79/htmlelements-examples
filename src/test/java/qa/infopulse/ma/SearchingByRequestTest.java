package qa.infopulse.ma;

import qa.infopulse.ma.pages.MainPage;
import qa.infopulse.ma.pages.SearchPage;
import qa.infopulse.ma.webdriver.WebDriverProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertThat;
import static ru.yandex.qatools.htmlelements.matchers.WebElementMatchers.exists;

/**
 * @author Artem Eroshenko eroshenkoam
 *         5/6/13, 2:51 PM
 */
public class SearchingByRequestTest {

    private final int DEFAULT_RESULTS_COUNT = 11;

    public WebDriver driver = WebDriverProvider.createWebDriver();


    @Before
    public void loadStartPage() {
        driver.get("http://www.yandex.ua");
    }


    /*
     * Запуск тестів:  mvn clean test
     * Запуск генерації репорту:  mvn site
     * Запуск всього одразу:  mvn clean test site
     * Репорт має створитись в папці
     * ./target/site/allure-maven-plugin/index.html
     * !! мейвен має бути версії > 3.2. Інакше фейлиться генерація репорту.
     * Якщо використовувати IntelliJ IDEA - з нею (v2016.2) по дефолту йде мейвен 3,0.
     * Треба скачати новий мейвен, і в настройках ідеї для мейвена (build tools - maven) вибрати шлях до нового.
     * (в мене версія 3.3.9
     *
     * Генерація репортів для allure працює лише коли тести запущено із мейвена.
     * Якщо запускати з контекстного меню в IntelliJ IDEA - по дефолту не працює, але можна настроїти
     */
    @Test
    public void afterSearchingUserShouldSeSearchResults() {
        MainPage mainPage = new MainPage(driver);
        SearchPage page = mainPage.searchFor("Yandex");
        assertThat(page.getSearchResults(), exists());
        assertThat(page.getSearchResults().getSearchItems(), hasSize(DEFAULT_RESULTS_COUNT));
    }

    @After
    public void killWebDriver() {
        driver.quit();
    }
}
