package qa.infopulse.ma.webdriver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.AbstractWebDriverEventListener;

public class DriverLogger extends AbstractWebDriverEventListener {

	// Клас може бути використаний для логування дій веб-драйвера
	// для цього треба заоверрайдити відповідні методи із AbstractWebDriverEventListener

	// private final Logger logger = Logger.getLogger(getClass());  // -- log4j

	@Override
	public void onException(Throwable throwable, WebDriver driver) {
		// TODO: put extended info into logger here.
		// logger.error("Some driver error occurred....", throwable)
	}


	@Override
	public void beforeNavigateTo(String url, WebDriver driver) {
		//logger.info("Navigate to: [%s] ...", url);
	}
}
