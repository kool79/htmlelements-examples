package qa.infopulse.ma.webdriver;

import java.io.File;
import java.io.IOException;

import org.openqa.selenium.Platform;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.events.WebDriverEventListener;

import static org.openqa.selenium.remote.BrowserType.IE;

public class LocalDriverStarter {

	private final String pluginsPath = "bin/browser-plugins/";

	private final WebDriverEventListener[] listeners;
	private final WebDriverProperties properties;

	// private final Logger logger = Logger.getLogger(getClass());  // -- log4j


	public LocalDriverStarter(WebDriverProperties webDriverProperties, WebDriverEventListener... listeners) {
		this.listeners = listeners;
		this.properties = webDriverProperties;
	}


	public WebDriver start() {

		// logger.info(String.format("Starting Local WebDriver. Browser==[%s]...", properties.getBrowser()));

		// @see https://code.google.com/p/selenium/wiki/DesiredCapabilities
		DesiredCapabilities capabilities = new DesiredCapabilities();

		capabilities.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.ACCEPT);
		capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);  // (chrome)

		WebDriver driver = null;
		switch (properties.getBrowser()) {

		case CHROME: {
			String prop = "webdriver.chrome.driver";
			System.setProperty(prop, "bin/web-drivers/chrome/win/chromedriver.exe");
			driver = new ChromeDriver(capabilities);
			break;
		}

		case IE:
			System.setProperty("webdriver.ie.driver", "bin/web-drivers/ie32/IEDriverServer.exe");
			capabilities.setPlatform(Platform.WINDOWS);
			capabilities.setBrowserName(IE);
			capabilities.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);
			//If IE fail to work, please remove this and remove
			// "enable protected mode" for all the 4 zones from Internet options
			capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
			driver = new InternetExplorerDriver(capabilities);
			break;

		case SAFARI:
			driver = new SafariDriver();
			break;

		case FF:
			FirefoxProfile profile = new FirefoxProfile();
			addFirebug(profile);
			addFirePath(profile);
			addFireFinder(profile);

			profile.setPreference("security.mixed_content.block_active_content", false); // disable mixed content warn
			profile.setPreference("plugin.state.flash", 2);        //0=disable, 1=ask user, 2 = enable
			//profile.setPreference("network.proxy.http", "localhost");
			//profile.setPreference("network.proxy.http_port", 3128);
			//profile.setPreference("network.proxy.ssl", "proxy.domain.example.com");
			//profile.setPreference("network.proxy.ssl_port", 3128);

			profile.setAcceptUntrustedCertificates(true);
			profile.setEnableNativeEvents(true);

			String ffBinary = properties.getFirefoxBinaryPath();

			File pathToFirefoxBinary = ffBinary.isEmpty() ? null : new File(ffBinary);

			FirefoxBinary binary = new FirefoxBinary(pathToFirefoxBinary);
			driver = new FirefoxDriver(binary, profile, capabilities);
			break;

		default:
			throw new RuntimeException(
					String.format("Browser type is not supported now: [%s]", properties.getBrowser()));
		}


		// Create EventFiringWebDriver to support logging purposes throw wd.register(WebDriverEventListener)
		EventFiringWebDriver wd = new EventFiringWebDriver(driver);

		for (WebDriverEventListener listener : listeners)
			wd.register(listener);

		wd.manage().window().setPosition(properties.getBrowserPosition());
		wd.manage().window().maximize();

		return wd;
	}


	private void addFireFinder(FirefoxProfile profile) {
		try {
			profile.addExtension(new File(pluginsPath + "firefox/firefinder_for_firebug-1.4-fx.xpi"));
		} catch (IOException e) {
			// logger.warn("FireFinder initialization failed.");
		}
	}


	private void addFirePath(FirefoxProfile profile) {
		try {
			profile.addExtension(new File(pluginsPath + "firefox/firepath-0.9.7-fx.xpi"));
		} catch (IOException e) {
			// logger.warn("FirePath initialization failed.");
		}
	}


	private void addFirebug(FirefoxProfile profile) {
		try {
			profile.addExtension(new File(pluginsPath + "firefox/firebug.xpi"));
			profile.setPreference("extensions.firebug.currentVersion", "1.12.7");
			profile.setPreference("extensions.firebug.allPagesActivation", "on");
			profile.setPreference("extensions.firebug.net.enableSites", true);           // enable NET tab
			profile.setPreference("extensions.firebug.cookies.enableSites", true);       // enable cookies tab
		} catch (IOException e) {
			// logger.warn("FireBug initialization failed.");
		}
	}

}
