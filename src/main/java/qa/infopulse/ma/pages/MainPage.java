package qa.infopulse.ma.pages;

import qa.infopulse.ma.elements.SearchArrow;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import ru.yandex.qatools.allure.annotations.Step;
import ru.yandex.qatools.htmlelements.loader.decorator.HtmlElementDecorator;

/**
 * @author Artem Eroshenko eroshenkoam
 *         5/6/13, 5:14 PM
 */
public class MainPage {

    private WebDriver driver;

    @FindBy(css = "form.search2")
    private SearchArrow searchArrow;

    public MainPage(final WebDriver driver) {
        PageFactory.initElements(new HtmlElementDecorator(driver), this);
        this.driver = driver;
    }

    @Step
    public SearchPage searchFor(String request) {
        searchArrow.searchFor(request);
        return new SearchPage(driver);
    }

}
