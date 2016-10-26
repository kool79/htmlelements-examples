package qa.infopulse.ma.webdriver;

import java.net.URL;
import java.security.InvalidParameterException;

import org.openqa.selenium.Point;
import ru.yandex.qatools.properties.PropertyLoader;
import ru.yandex.qatools.properties.annotations.Property;
import ru.yandex.qatools.properties.annotations.Resource;


@Resource.Classpath("webdriver.properties")
public class WebDriverProperties {

	@Property("webdriver.remote.url")
	private URL remoteDriverUrl;

	@Property("webdriver.browser")
	private String browserName = "ff";

	@Property("webdriver.remote")
	private boolean useRemoteDriver = false;

	@Property("webdriver.firefox.bin")
	private String firefoxBinaryPath = "";

	@Property("browser.window.position.x")
	private Integer browserPositionX = 0;

	@Property("browser.window.position.y")
	private Integer browserPositionY = 0;


	public String getFirefoxBinaryPath() {
		return firefoxBinaryPath;
	}


	public Point getBrowserPosition() {
		return new Point(browserPositionX, browserPositionY);
	}


	public enum Browser {
		NULL,
		IE,
		CHROME,
		FF,
		SAFARI;


		public static Browser fromString(String browserName) {

			switch (browserName.toLowerCase().replaceAll("[\"'\\s]+", "")) {
			case "internetexplorer":
			case "ie":
			case "iexplore":
				return Browser.IE;
			case "chrome":
				return Browser.CHROME;
			case "safari":
				return Browser.SAFARI;
			case "firefox":
			case "ff":
				return Browser.FF;
			}
			throw new InvalidParameterException(String.format("Browser type is not supported now: [%s]", browserName));

		}
	}


	public WebDriverProperties() {
		PropertyLoader.populate(this);
	}


	public URL getRemoteDriverUrl() {
		return remoteDriverUrl;
	}


	public Browser getBrowser() {
		return Browser.fromString(browserName);
	}


	public boolean isUseRemoteDriver() {
		return useRemoteDriver;
	}
}
