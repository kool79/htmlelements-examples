package qa.infopulse.ma.elements;

import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.allure.annotations.Step;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.HtmlElement;
import ru.yandex.qatools.htmlelements.element.TextInput;

/**
 * @author Artem Eroshenko eroshenkoam
 *         5/6/13, 5:13 PM
 */
public class SearchArrow extends HtmlElement {

    @FindBy(name = "text")
    public TextInput requestInput;


    @FindBy(className = "button")
    // на https://www.yandex.ua/ є 3 кнопки з цим класом,
    // але сюди попаде лише кнопка, яка знаходиться всередині SearchArrow
    // (локатор для нього - в класі пейджі MainPage
    public Button searchButton;

	@Step
    public void searchFor(String request) {
        requestInput.getWrappedElement().click();
        requestInput.clear();
        requestInput.sendKeys(request);
        searchButton.click();
    }
}
