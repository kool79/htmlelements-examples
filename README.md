HtmlElements jUnit Samples
==========================

Подключение зависимостей
------------------------

Чтобы использовать библиотеку HtmlElements необходимо создать простой [maven-проект](http://maven.apache.org/guides/getting-started/index.html).
После этого добавить в зависимости свежую версию библиотеки:

```xml
<dependency>
    <groupId>ru.yandex.qatools.htmlelements</groupId>
    <artifactId>htmlelements-java</artifactId>
    <version>1.11</version>
</dependency>
```

Выполните команду `mvn clean compile`, чтобы проерить, что ваш проект компилируется.

Пример использования HtmlElements
---------------------------------

В качестве примера возьмем главную страницу Яндекса (http://www.yandex.ru).
Давайте опишем для начала простенький элемент, например поисковую строку:

```java
public class SearchArrow extends HtmlElement {

    @FindBy(xpath = ".//input[@class='b-form-input__input']")
    public TextInput requestInput;

    @FindBy(xpath = ".//input[@class='b-form-button__input']")
    public Button searchButton;

    public void searchFor(String request) {
        requestInput.clear();
        requestInput.sendKeys(request);
        searchButton.click();
    }
}
```

Этот класс описывает структуру поисковой строки и логику взаимодействия с ней.
Дальше необходимо создать класс MainPage, который содержит поисковую строку:

```java
public class MainPage {

    private WebDriver driver;

    @FindBy(className = "b-morda-search-form")
    private SearchArrow searchArrow;

    public MainPage(final WebDriver driver) {
        PageFactory.initElements(new HtmlElementDecorator(driver), this);
        this.driver = driver;
    }

    public SearchPage searchFor(String request) {
        this.searchArrow.searchFor(request);
        return new SearchPage(driver);
    }

}
```

Как видно, в конструкторе вызывается инициализация внутренних эелементов стнаицы MainPage используется
кастомный декоратор полей класса: HtmlElementDecorator.
Так как поле 'searchArrow' является наследником HtmlElements, то его инициализация происходит рекурсивно.
Таком образом, инициализируются и внутренние элементы поисковой строки:
 -  protected WebElement requestInput;
 -  protected WebElement searchButton;

Как видно из описания, метод `searchFor` возвращает экземпляр поисковой страницы. Опишем ее:

```java
public class SearchPage {

    private WebDriver driver;

    @FindBy(className = "b-serp-list")
    private SearchResults searchResults;

    @FindBy(className = "b-morda-search-form")
    private SearchArrow searchArrow;

    public SearchPage(WebDriver driver) {
        PageFactory.initElements(new HtmlElementDecorator(driver), this);
        this.driver = driver;
    }

    public SearchPage searchFor(String request) {
        this.searchArrow.searchFor(request);
        return this;
    }

    public SearchResults getSearchResults() {
        return this.searchResults;
    }
}
```

Для того, чтобы это проверить создадим простенький тест для проверки поисковых результатов,
в котором создим экземляр нашей страницы:

```java
public class SearchingByRequestTest {

    private final int DEFAULT_RESULTS_COUNT;

    public WebDriver driver = new FirefoxDriver();

    public SearchingByRequestTest() {
        DEFAULT_RESULTS_COUNT = 10;
    }

    @Before
    public void loadStartPage() {
        driver.get("http://www.yandex.ru");
    }

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
```

В этом примере видно, что после инициализации самой страницы `MainPage` произошла инициализация внутренних элементов.

Олександр Куличок:
В прикладі для інфопульса я додав інтеграцію з бібліотекою allure для (яндексовською) 
та залишив лише проект із junit. Для запуску тестів з генерацією репорту треба використовувати мейвен версії > 3.2
(вірніше, це потрібно лише для генерації репортів)

Також додав проект IntelliJ IDEA в репозиторій

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
     

