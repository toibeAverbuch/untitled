import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;


public class TableTest {
    private static WebDriver driver;

    @BeforeClass
    public static void BeforeClass() {
        System.setProperty("webdriver.chrome.driver", Constants.DRIVERLOCATION);
        driver = new ChromeDriver();
        driver.get(Constants.LINK);
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.MINUTES);
    }

    @Test
    public void myTest() throws ParserConfigurationException, IOException, SAXException {
        for (int i = 0; i < 5; i++) {
            String country = getData("country", i);
            String company = getData("company", i);
            WebElement table = driver.findElement(By.id("customers"));
            Assert.assertTrue(verifyTableCellText(table, 3, country, 1, company));
        }
    }


    public boolean verifyTableCellText(WebElement table, int searchColumn,
                                       String searchText, int returnColumnText, String expectedText) {
        try {
            String realText = getTableCellTextByXpath(table, searchColumn, searchText, returnColumnText);
            Assert.assertEquals("wrong text", realText, expectedText);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getTableCellTextByXpath(WebElement table, int searchColumn,
                                          String searchText, int returnColumnText) throws Exception {
        return table.findElement(By.xpath(".//tr[.//td[" + searchColumn + "][text()='" + searchText + "']]/td[" + returnColumnText + "]")).getText();
    }

    private static String getData(String keyName, int i) throws ParserConfigurationException, IOException, SAXException {
        File myData = new File(Constants.FILENAME);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        assert dBuilder != null;
        Document doc = dBuilder.parse(myData);
        if (doc != null) {
            doc.getDocumentElement().normalize();
        }
        assert doc != null;
        return doc.getElementsByTagName(keyName).item(i).getTextContent();
    }

    @AfterClass
    public static void afterClass() {
        driver.quit();
    }
}
