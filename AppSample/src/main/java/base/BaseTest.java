package base;

import Pages.BasePage;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;
import io.appium.java_client.remote.AutomationName;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import org.apache.commons.exec.OS;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestContext;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.URL;

public class BaseTest {

    DesiredCapabilities caps = null;
    private int appiumPort = 4273;
    private int webdriverAgentPort = 8100;
    private String udid = null;
    private String platform = null;
    private String deviceName = null;
    public boolean localExecution = false;
    public AppiumDriverLocalService service = null;
    public DriverParams driverParams = null;
    public BasePage basePage = null;
    ExtentHtmlReporter htmlReporter;
    public ExtentReports extent;

    @BeforeTest(alwaysRun = true)
    @Parameters({"appiumPort", "webdriverAgentPort", "udid", "platform", "localExecution", "deviceName"})
    public void setUpBaseClass(int appiumPort, int webdriverAgentPort, String udid, String platform, boolean localExecution, String deviceName, ITestContext iTestContext) {
        System.out.println("-------------- Started for " + deviceName + " SetupBaseClass --------------");
        iTestContext.setAttribute("appiumPort", appiumPort);
        iTestContext.setAttribute("webdriverAgentPort", webdriverAgentPort);
        iTestContext.setAttribute("udid", udid);
        iTestContext.setAttribute("platform", platform);
        iTestContext.setAttribute("localExecution", localExecution);
        iTestContext.setAttribute("deviceName", deviceName);
        this.startReport(deviceName);
        System.out.println("-------------- Completed for " + deviceName + " SetupBaseClass --------------");

    }

    public void startReport(String device) {
        htmlReporter = new ExtentHtmlReporter(System.getProperty("user.dir") + "/target/testReport.html");
        htmlReporter.config().setDocumentTitle("Sample Automation Report for " + device);
        htmlReporter.config().setReportName("Test Report");
        htmlReporter.config().setTheme(Theme.DARK);
        htmlReporter.config().setTimeStampFormat("EEEE, MMMM dd, yyyy, hh:mm a '('zzz')'");
        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);
    }

    @BeforeClass(alwaysRun = true)
    public void initClass(ITestContext iTestContext) {
        System.out.println("-------------- Started Appium Init Class -------------");
        this.deviceName = (String) iTestContext.getAttribute("deviceName");
        this.platform = (String) iTestContext.getAttribute("platform");
        this.appiumPort = (int) iTestContext.getAttribute("appiumPort");
        this.localExecution = (boolean) iTestContext.getAttribute("localExecution");
        this.udid = (String) iTestContext.getAttribute("udid");
        this.webdriverAgentPort = (int) iTestContext.getAttribute("webdriverAgentPort");
        caps = new DesiredCapabilities();
        driverParams = new DriverParams();
        System.out.println("-------------- Completed Appium Init Class -------------");
    }

    public DesiredCapabilities setAndroidCapabilities() {
        System.out.println("-------------- Started AltUnityBaseTest Set Android Capabilities -------------");
        if (localExecution) {
            this.startServer(this.appiumPort, this.udid, 0);
        }
        System.out.println("Executing with System port for device " + this.deviceName + "  ------> " + this.appiumPort);
//        caps.setCapability("skipDeviceInitialization", true);
//        caps.setCapability("skipServerInstallation", true);
//        caps.setCapability("fullReset", false);
        caps.setCapability("noReset", true);
        caps.setCapability("ignoreUnimportantViews", true);
        caps.setCapability("platformName", this.platform);
        caps.setCapability("appPackage", "io.appium.android.apis");
        caps.setCapability("appActivity", "io.appium.android.apis.ApiDemos");
        caps.setCapability("autoGrantPermissions", "true");
        caps.setCapability("enforceAppInstall", "true");
        if (localExecution) {
            caps.setCapability("udid", this.udid);
            caps.setCapability("deviceName", this.deviceName);
        }
        //caps.setCapability("autoDismissAlerts", "true");
        caps.setCapability("autoAcceptAlerts", true);
        caps.setCapability("newCommandTimeout", "300000");
        caps.setCapability("androidInstallTimeout", "300000");
        caps.setCapability("adbExecTimeout", "600000");
        caps.setCapability("ignoreHiddenApiPolicyError", "true");
        caps.setCapability(MobileCapabilityType.AUTOMATION_NAME, "UiAutomator2");
        System.out.println("-------------- Completed AltUnityBaseTest Set Android Capabilities -------------");
        return caps;

    }

    public DesiredCapabilities setIosCapabilities() throws Exception {
        System.out.println("-------------- Started AltUnityBaseTest Set iOS Capabilities -------------");
        if (localExecution) {
            this.startServer(this.appiumPort, this.udid, this.webdriverAgentPort);
        }
        /*caps.setCapability("skipDeviceInitialization", true);
        caps.setCapability("skipServerInstallation", true);
        caps.setCapability("noReset", true);
        caps.setCapability("ignoreUnimportantViews", true);
        caps.setCapability("fullReset", false);*/
        caps.setCapability(MobileCapabilityType.PLATFORM_NAME, "iOS");
        caps.setCapability(MobileCapabilityType.AUTOMATION_NAME, AutomationName.IOS_XCUI_TEST);
        caps.setCapability("startIWDP", "true");
        caps.setCapability("wdaLaunchTimeout", 90000);
        caps.setCapability("appPushTimeout", 240000);
        caps.setCapability("newCommandTimeout", "900000");
        caps.setCapability("showIOSLog", true);
        caps.setCapability("xcodeSigningId", "iPhone Developer");
        caps.setCapability("bundleId", "io.appium.android.apis");
        caps.setCapability("autoAcceptAlerts", false);
        caps.setCapability("showIOSLog", "true");
        System.out.println("-------------- Completed AltUnityBaseTest Set iOS Capabilities -------------");
        return caps;
    }

    public void startServer(int port, String deviceUdId, int webdriverAgentPort) {
        //Set Capabilities
        System.out.println("-------------- Started AltUnityBaseTest startAppium Server -------------");
//        if (OS.isFamilyMac()) {
        DesiredCapabilities cap = new DesiredCapabilities();
        cap.setCapability("noReset", "false");
        cap.setCapability("udid", deviceUdId);
        if (!this.platform.equals("Android")) {
            cap.setCapability("wdaLocalPort", webdriverAgentPort);
        }

        //Build the Appium service
        AppiumServiceBuilder builder = new AppiumServiceBuilder();
        builder.withIPAddress("127.0.0.1");
        builder.usingPort(port);
        builder.withCapabilities(cap);
        builder.withArgument(GeneralServerFlag.SESSION_OVERRIDE);
        builder.withArgument(GeneralServerFlag.LOG_LEVEL, "error");
        if (OS.isFamilyMac()) {
            builder.withAppiumJS(new File("/usr/local/lib/node_modules/appium/build/lib/main.js"));
            builder.usingDriverExecutable(new File("/usr/local/bin/node"));
        } else {
            builder.withAppiumJS(new File("C:/Program Files/Appium/resources/app/node_modules/appium/build/lib/main.js"));
            builder.usingDriverExecutable(new File("C:/Program Files/nodejs/node.exe"));
        }

        //Start the server with the builder
        if (!this.checkIfServerIsRunnning(port)) {
            service = AppiumDriverLocalService.buildService(builder);
            service.start();
        }
//        }
        System.out.println("-------------- Completed Start Appium Server -------------");
    }


    public boolean checkIfServerIsRunnning(int port) {

        boolean isServerRunning = false;
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(port);
            serverSocket.close();
        } catch (IOException e) {
            //If control comes here, then it means that the port is in use
            isServerRunning = true;
        } finally {
            serverSocket = null;
        }
        return isServerRunning;
    }

    public void stopServer() {
        try {
            System.out.println("------------ stopping appium server-----------");
            service.stop();
        } catch (Exception e) {
            System.out.println("Failed to stop the server");
        }
    }

    public void closeApp() {
        System.out.println("-------------------------Closing app and its drivers-------------");
        try {
            driverParams.getAppiumDriver().quit();
            if (this.localExecution) {
                stopServer();
            }
        } catch (Exception e) {
            if (this.localExecution) {
                stopServer();
            }
            System.out.println("unable to quit the drivers");
        }
    }


    public void initAppiumAndroidDriver() throws Exception {
        System.out.println("-------------- Started init Appium Android Driver -------------");
        this.driverParams.setAppiumDriver(new AndroidDriver<AndroidElement>(new URL("http://127.0.0.1:" +
                this.appiumPort + "/wd/hub"), this.caps));
        System.out.println("-------------- Completed init Appium Android Driver -------------");

    }

    public void initAppiumIOSDriver() throws Exception {
        System.out.println("-------------- Started init Appium iOS Driver -------------");
        this.driverParams.setAppiumDriver(new IOSDriver<IOSElement>(new URL("http://127.0.0.1:" +
                this.appiumPort + "/wd/hub"), caps));
        System.out.println("-------------- Completed init Appium iOS Driver -------------");
    }

    public void initDriver() throws Exception {
        System.out.println("--------------- Started Base Test Init Driver------------");
        if (this.platform.equals("Android")) {
            this.setAndroidCapabilities();
            this.initAppiumAndroidDriver();
        } else {
            this.setIosCapabilities();
            this.initAppiumIOSDriver();
        }
        basePage = new BasePage(this.driverParams);
        System.out.println("-------------- Completed Base Test Init Driver--------------");
    }

    @AfterTest
    public void aftTest() {
        extent.flush();
    }

    @AfterSuite
    public void afterSuite() {
        System.out.println("Execution for the device " + this.deviceName + " is completed");
    }

}
