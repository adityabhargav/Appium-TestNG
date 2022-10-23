package Pages;

import base.DriverParams;
import org.openqa.selenium.By;

public class HomePage extends BasePage {

    public HomePage(DriverParams driverParams) {
        super(driverParams);
    }

    public boolean verifyHmePg() {
        super.waitForElement(By.id("android:id/action_bar_container"));
        return super.driverParams.getAppiumDriver().findElement(By.id("android:id/action_bar_container")).isDisplayed();
    }

    public void clkOnAccBtnOnHmePg() {
        super.waitForElement(By.xpath("//android.widget.TextView[@content-desc=\"Accessibility\"]"));
        super.driverParams.getAppiumDriver().findElement(By.xpath("//android.widget.TextView[@content-desc=\"Accessibility\"]")).click();
    }
}
