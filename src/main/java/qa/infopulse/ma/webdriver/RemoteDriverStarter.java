package qa.infopulse.ma.webdriver;

import java.util.logging.Level;

import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.events.WebDriverEventListener;

public class RemoteDriverStarter {

	private final WebDriverEventListener[] listeners;
	private final WebDriverProperties properties;

	// private final Logger logger = Logger.getLogger(getClass());  // -- log4j


	public RemoteDriverStarter(WebDriverProperties webDriverProperties, WebDriverEventListener... listeners) {
		this.listeners = listeners;
		this.properties = webDriverProperties;
	}


	public WebDriver start() {

		// logger.info(String.format("Starting Remote WebDriver. Browser==[%s]...", properties.getBrowser()));

		// @see https://code.google.com/p/selenium/wiki/DesiredCapabilities
		DesiredCapabilities capabilities;

		switch (properties.getBrowser()) {
		case IE:
			capabilities = DesiredCapabilities.internetExplorer();
			capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
			capabilities.setCapability(CapabilityType.HAS_NATIVE_EVENTS, true);
			capabilities.setCapability("ie.ensureCleanSession", true);
			capabilities.setCapability("browserAttachTimeout", 20000); //ms
			capabilities.setCapability("logLevel", "WARN");
			break;

		case CHROME:
			capabilities = DesiredCapabilities.chrome();
			capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
			capabilities.setCapability(CapabilityType.HAS_NATIVE_EVENTS, true);
			break;

		case SAFARI:
			capabilities = DesiredCapabilities.safari();
			capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
			capabilities.setCapability(CapabilityType.HAS_NATIVE_EVENTS, true);
			break;

		case FF:
			FirefoxProfile profile = new FirefoxProfile();
			profile.setEnableNativeEvents(true);
			profile.setAcceptUntrustedCertificates(true);
			profile.setPreference("security.mixed_content.block_active_content", false); // disable mixed content warn
			profile.setPreference("plugin.state.flash", 2);        //0=disable, 1=ask user, 2 = enable

			capabilities = DesiredCapabilities.firefox();
			capabilities.setCapability("firefox_profile", profile);
			break;

		default:
			throw new RuntimeException(
					String.format("Browser type is not supported now: [%s]", properties.getBrowser()));
		}

		capabilities.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.ACCEPT);

		try {
			RemoteWebDriver remoteWD = new RemoteWebDriver(properties.getRemoteDriverUrl(), capabilities);
			remoteWD.setFileDetector(new LocalFileDetector());
			remoteWD.manage().window().maximize();
			remoteWD.setLogLevel(Level.INFO);

			EventFiringWebDriver wd = new EventFiringWebDriver(new Augmenter().augment(remoteWD));
			for (WebDriverEventListener listener : listeners) {
				wd.register(listener);
			}

			return wd;

		} catch (Exception e) {
			throw new RuntimeException("Web-driver initialization error: " + e.getMessage(), e);
		}
	}
}
