package base;

import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BaseCls {
    public DriverParams driverParams;

    public BaseCls(DriverParams driverParams) {
        this.driverParams = driverParams;
    }

    public void clkOnNativeXY(int x, int y) {
        try {
            TouchAction touchAction = new TouchAction(this.driverParams.getAppiumDriver());
            touchAction.tap(PointOption.point(x, y))
                    .waitAction(WaitOptions.waitOptions(Duration.ofMillis(500)))
                    .release().perform();
        } catch (Exception e) {
            System.out.println(e.getCause());
        }
    }

    public void waitForElement(By ele){
        WebDriverWait wait = new WebDriverWait(driverParams.getAppiumDriver(),20);
        wait.until(ExpectedConditions.visibilityOfElementLocated(ele));
    }
}
