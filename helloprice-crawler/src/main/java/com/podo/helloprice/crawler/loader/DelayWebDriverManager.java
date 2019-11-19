package com.podo.helloprice.crawler.loader;

import com.podo.helloprice.core.util.MyNumberUtils;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class DelayWebDriverManager {

    @Value("${crawler.useragent}")
    private String userAgent;

    @Value("${crawler.timeout}")
    private Integer timeout;

    @Value("${crawler.webdriver.servers}")
    private List<String> webDriverRemoteUrls;

    private Map<Integer, WebDriver> drivers = new HashMap<>();

    public WebDriver getRandDriver() {
        int driverNumber = MyNumberUtils.rand(webDriverRemoteUrls.size());

        if (Objects.isNull(drivers.get(driverNumber))) {
            initDriver(driverNumber);
        }

        log.info("WebDriver{} 에서 응답합니다", driverNumber);
        return drivers.get(driverNumber);
    }

    public void clearDrivers() {
        log.info("모든 Webdriver를 Clear 합니다");

        this.drivers = new HashMap<>();
    }


    private void initDriver(int driverNumber) {
        final String webDriverRemoteUrlStr = webDriverRemoteUrls.get(driverNumber);

        log.info("WebDriver{} 을 초기화 합니다, RemoteUrl : {}", driverNumber, webDriverRemoteUrlStr);

        final ChromeOptions chromeOptions = getChromeOptions();

        try {
            WebDriver driver = new RemoteWebDriver(new URL(webDriverRemoteUrlStr), chromeOptions);

            driver.manage().timeouts().pageLoadTimeout(timeout, TimeUnit.MILLISECONDS);
            driver.manage().timeouts().implicitlyWait(timeout, TimeUnit.MILLISECONDS);
            driver.manage().timeouts().setScriptTimeout(timeout, TimeUnit.MILLISECONDS);

            this.drivers.put(driverNumber, driver);
        } catch (MalformedURLException e) {
            log.error("WebDriver URL이 잘못되었습니다", e);
        }
    }

    private ChromeOptions getChromeOptions() {
        final ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("headless");
        chromeOptions.addArguments("disable-gpu");
        chromeOptions.addArguments("user-agent=" + userAgent);
        return chromeOptions;
    }


}
