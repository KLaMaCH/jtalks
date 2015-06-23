import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by kolchanov on 23.06.15.
 */
public class Draft {

    private String[] mailLoginData = {"testtreesol@yandex.ru", "123456TS"};
    private String[] loginData = {"klamach89@gmail.com", "145278Goo"};
    private WebDriver driver;

    public static File makeSrceenshot(WebDriver driver, String pathName) {
        Screenshot shot = new AShot().takeScreenshot(driver);
        String path = pathName + ".png";
        File outputFile = new File(path);
        try {
            ImageIO.write(shot.getImage(), "png", outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new File(path);
    }


    @BeforeTest
    public void startWebDriver() {
        driver = new FirefoxDriver();
    }

    @Test
    public void Registration() throws Exception {
        driver.get("http://qa.jtalks.org/jcommune/");
        driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);

        driver.get("https://mail.yandex.ru/");
        WebElement ya_email = driver.findElement(By.name("login"));
        ya_email.clear();
        ya_email.sendKeys(mailLoginData[0]);

        WebElement ya_password = driver.findElement(By.name("passwd"));
        ya_password.clear();
        ya_password.sendKeys(mailLoginData[1]);
        ya_password.submit();
        Thread.sleep(500);

        driver.findElement(By.xpath("//a[contains(@href,'#message')]")).click();
        driver.findElement(By.xpath("//a[contains(@href,'jtalks')]")).click();
        for (String handle : driver.getWindowHandles()) {
            driver.switchTo().window(handle);
        }
        Assert.assertTrue(driver.getCurrentUrl().equals("http://qa.jtalks.org/jcommune/"));
        makeSrceenshot(driver, "RegSucsess");

    }


    @AfterTest
    public void stopWebDriver(){
        driver.quit();
    }
}