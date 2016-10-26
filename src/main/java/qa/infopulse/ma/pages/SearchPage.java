package qa.infopulse.ma.pages;

import qa.infopulse.ma.elements.SearchArrow;
import qa.infopulse.ma.elements.SearchResults;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import ru.yandex.qatools.allure.annotations.Step;
import ru.yandex.qatools.htmlelements.loader.decorator.HtmlElementDecorator;

/**
 * @author Artem Eroshenko eroshenkoam
 *         5/6/13, 5:15 PM
 */
public class SearchPage {

    private WebDriver driver;

    @FindBy(css = "ul.serp-list")
    private SearchResults searchResults;

    @FindBy(css = "form.search2")
    private SearchArrow searchArrow;

    public SearchPage(WebDriver driver) {
        PageFactory.initElements(new HtmlElementDecorator(driver), this);
        this.driver = driver;
    }

    @Step
    public SearchPage searchFor(String request) {
        this.searchArrow.searchFor(request);
        return this;
    }

    @Step
    public SearchResults getSearchResults() {
        return this.searchResults;
    }
}
