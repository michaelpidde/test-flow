import com.michaelpidde.testflow.client.pageObject.wolfnet.Search
import com.michaelpidde.testflow.engine.util.Test
import com.michaelpidde.testflow.engine.util.Logger

import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.PageFactory

class TestSingleFamily extends Test {
	def TestSingleFamily(WebDriver driver, String baseUrl, Logger logger) {
		super(driver, baseUrl, logger)
	}

	def run() {
		Search search = PageFactory.initElements(driver, Search.class)

		driver.get(baseUrl)
		logStep("Get base URL.")

		// Open Type menu
		WebElement menu = search.openSearchOptionMenu("Type")
		menu.click()
		logStep("Open Type menu.")

		// Select Single Family
		search.selectPropertyType(driver, "Single Family")
		logStep("Select Single Family.")

		// Account for any latency...
		wait(3);

		int results = search.getResultCount()
		logStep("Results: " + results)

		return (results > 0) ? true : false;
	}
}

def invoke(WebDriver driver, String baseUrl, Logger logger) {
	test = new TestSingleFamily(driver, baseUrl, logger)
	return test.run();
}