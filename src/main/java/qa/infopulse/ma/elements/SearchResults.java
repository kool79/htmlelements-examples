package qa.infopulse.ma.elements;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.HtmlElement;

import java.util.List;

/**
 * @author Artem Eroshenko eroshenkoam
 *         5/6/13, 5:13 PM
 */
public class SearchResults extends HtmlElement {

    @SuppressWarnings("unused")
    @FindBy(css = "li.serp-item")
    private List<WebElement> searchItems;

    public List<WebElement> getSearchItems() {
        return searchItems;
    }
}
