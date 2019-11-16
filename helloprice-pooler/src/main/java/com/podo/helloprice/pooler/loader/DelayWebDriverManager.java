package com.podo.helloprice.pooler.loader;

import com.podo.helloprice.core.util.MyNumberUtils;
import com.podo.helloprice.pooler.exception.FailGetDocumentException;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Component
public class DelayWebDriverManager {

    @Value("${pooler.useragent}")
    private String userAgent;

    @Value("${pooler.timeout}")
    private Integer timeout;

    @Value("${pooler.webdriver.server1}")
    private String webDriver1RemoteUrlStr;

    @Value("${pooler.webdriver.server2}")
    private String webDriver2RemoteUrlStr;
    private WebDriver driver1;
    private WebDriver driver2;


    public WebDriver getRandDriver() {
        int rand = MyNumberUtils.rand(2);
        if (rand == 0) {
            log.info("WebDriver1을 사용합니다");
            if (Objects.isNull(driver1)) {
                initDriver1();
            }
            return driver1;
        } else {
            log.info("WebDriver2를 사용합니다");
            if (Objects.isNull(driver2)) {
                initDriver2();
            }
            return driver2;
        }
    }

    public void clearDrivers() {
        log.info("모든 Webdriver를 Clear 합니다");
        this.driver1 = null;
        this.driver2 = null;
    }


    private void initDriver1() {

        log.info("WebDriver1 을 초기화 합니다, RemoteUrl : {}", webDriver1RemoteUrlStr);

        final ChromeOptions chromeOptions = getChromeOptions();


        try {
            driver1 = new RemoteWebDriver(new URL(webDriver1RemoteUrlStr), chromeOptions);

            driver1.manage().timeouts().pageLoadTimeout(timeout, TimeUnit.MILLISECONDS);
            driver1.manage().timeouts().implicitlyWait(timeout, TimeUnit.MILLISECONDS);
            driver1.manage().timeouts().setScriptTimeout(timeout, TimeUnit.MILLISECONDS);

        } catch (MalformedURLException e) {
            log.error("WebDriver URL이 잘못되었습니다", e);
        }
    }

    private void initDriver2() {

        log.info("WebDriver2 를 초기화 합니다, RemoteUrl : {}", webDriver2RemoteUrlStr);

        final ChromeOptions chromeOptions = getChromeOptions();

        try {
            driver2 = new RemoteWebDriver(new URL(webDriver2RemoteUrlStr), chromeOptions);

            driver2.manage().timeouts().pageLoadTimeout(timeout, TimeUnit.MILLISECONDS);
            driver2.manage().timeouts().implicitlyWait(timeout, TimeUnit.MILLISECONDS);
            driver2.manage().timeouts().setScriptTimeout(timeout, TimeUnit.MILLISECONDS);

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
