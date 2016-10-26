package qa.infopulse.ma.webdriver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.WebDriverEventListener;

public class WebDriverProvider {

	private static WebDriverProperties properties = new WebDriverProperties();
/*
	public static ThreadLocal<WebDriver> driverContainer = ThreadLocal.withInitial(() -> createWebDriver());


	public static WebDriver getWebDriver() {
		return driverContainer.get();
	}*/

	public static WebDriver createWebDriver() {

		WebDriver webDriver;
		final WebDriverEventListener webDriverLogger = getWebDriverLogger();

		webDriver = properties.isUseRemoteDriver()
		            ? new RemoteDriverStarter(properties, webDriverLogger).start()
		            : new LocalDriverStarter(properties, webDriverLogger).start();

		return webDriver;
	}


	private static WebDriverEventListener getWebDriverLogger() {
		return new DriverLogger();
	}

}
