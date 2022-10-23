package base;

import io.appium.java_client.AppiumDriver;

public class DriverParams {
    private int localPort;
    private int systemPort;
    private int webDriverAgentPort;
    private String udID;
    private boolean isAndroid;
    private boolean isLocalExecution;
    private AppiumDriver appiumDriver;

    public int getLocalPort() {
        return localPort;
    }

    public void setLocalPort(int localPort) {
        this.localPort = localPort;
    }

    public int getSystemPort() {
        return systemPort;
    }

    public void setSystemPort(int systemPort) {
        this.systemPort = systemPort;
    }

    public int getWebDriverAgentPort() {
        return webDriverAgentPort;
    }

    public void setWebDriverAgentPort(int webDriverAgentPort) {
        this.webDriverAgentPort = webDriverAgentPort;
    }

    public String getUdID() {
        return udID;
    }

    public void setUdID(String udID) {
        this.udID = udID;
    }

    public boolean isAndroid() {
        return isAndroid;
    }

    public void setAndroid(boolean android) {
        isAndroid = android;
    }

    public boolean isLocalExecution() {
        return isLocalExecution;
    }

    public void setLocalExecution(boolean localExecution) {
        isLocalExecution = localExecution;
    }

    public AppiumDriver getAppiumDriver() {
        return appiumDriver;
    }

    public void setAppiumDriver(AppiumDriver appiumDriver) {
        this.appiumDriver = appiumDriver;
    }
}
