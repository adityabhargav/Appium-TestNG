package Tests;

import Pages.HomePage;
import TestHelper.BaseTestHelper;
import org.testng.annotations.Test;

public class SampleTest extends BaseTestHelper {

    @Test(priority = 0)
    public void zerothTst() {
        HomePage homePage = new HomePage(super.driverParams);
        homePage.verifyHmePg();
    }

    @Test(priority = 1)
    public void firstTst() {
        HomePage homePage = new HomePage(super.driverParams);
        homePage.clkOnAccBtnOnHmePg();
    }

}
