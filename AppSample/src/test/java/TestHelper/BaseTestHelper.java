package TestHelper;

import base.BaseTest;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

public class BaseTestHelper extends BaseTest {

    public ExtentTest test;

    @BeforeClass
    public void initClass() throws Exception {
        System.out.println("------------- Started Base Test Init Class --------------");
        super.initDriver();
        System.out.println("--------------Completed Base Test Init Class-------------");
    }

    @BeforeMethod
    public void beforeMtd(ITestResult result) {
        test = super.extent.createTest("TestName", result.getName());
    }

    @AfterMethod
    public void aftEachCase(ITestResult result) {
        if (result.getStatus() == 2) {
            //Failure
            test.log(Status.FAIL, result.getThrowable());
        } else if (result.getStatus() == 3) {
            //Skip
            test.log(Status.SKIP, "Skipped");
        } else if (result.getStatus() == 1) {
            //Success
            test.log(Status.PASS, "pass");
        }
    }

    @AfterClass(alwaysRun = true)
    public void closeDriver(ITestContext iTestContext) {
        super.closeApp();
    }
}
