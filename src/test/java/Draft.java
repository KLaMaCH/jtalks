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
    private String[] loginData = {"KLaMaCH", "145278Goo"};
    private String[] topicdata = {"NewTopic Subject", "This topic was created by auto-test"};
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

    @Test(priority = 1, alwaysRun = true)
    public void NewTopic() throws Exception {
        driver.get("http://qa.jtalks.org/jcommune/login");
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        WebElement username = driver.findElement(By.id("dialog-userName"));
        username.clear();
        username.sendKeys(loginData[0]);

        WebElement password = driver.findElement(By.id("dialog-password"));
        password.clear();
        password.sendKeys(loginData[1]);
        password.submit();

        driver.get("http://qa.jtalks.org/jcommune/branches/3");
        makeSrceenshot(driver, "branch-page");

        driver.findElement(By.xpath("//a[contains(@href,'/jcommune/topics/new')]")).click();
        makeSrceenshot(driver, "newtopicpage");

        WebElement subject = driver.findElement(By.id("subject"));
        subject.clear();
        subject.sendKeys(topicdata[0]);

        if(!driver.findElement(By.id("sticked")).isSelected())
            driver.findElement(By.id("sticked")).click();
        if(!driver.findElement(By.id("announcement")).isSelected())
            driver.findElement(By.id("announcement")).click();

        WebElement messege = driver.findElement(By.id("postBody"));
        messege.clear();
        messege.sendKeys(topicdata[1]);
        messege.submit();

        Thread.sleep(1000);
        makeSrceenshot(driver, "newtopic");
    }


    @AfterTest
    public void stopWebDriver(){
        driver.quit();
    }
}