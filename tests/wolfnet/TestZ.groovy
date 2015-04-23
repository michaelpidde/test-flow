import com.michaelpidde.testflow.client.pageObject.wolfnet.Search
import com.michaelpidde.testflow.engine.util.Test
import org.openqa.selenium.support.PageFactory

// Get page object
page = PageFactory.initElements(driver, Search.class)
page.setDriver driver

driver.get baseUrl
logStep "Get base URL."

page.openSearchOptionMenu "Type"
logStep "Open Type menu."

page.selectPropertyType "Single Family"
logStep "Select Single Family."

// Account for any latency...
wait 3

results = page.getResultCount()
logStep "Results: " + results

return (results > 0) ? true : false;