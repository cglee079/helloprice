package com.podo.helloprice.crawl.worker.reader;

import com.podo.helloprice.core.util.NumberUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Slf4j
@RequiredArgsConstructor
public class WebDriverManager {
    private final List<String> webDriverRemoteUrls;
    private Map<Integer, WebDriver> webDrivers = new HashMap<>();

    public WebDriver getRandomWebDriver(String userAgent, Long readTimeout) {
        int randomIndexOfWebDrivers = NumberUtil.getRandomInt(webDriverRemoteUrls.size());

        final WebDriver randomWebDriver = webDrivers.get(randomIndexOfWebDrivers);
        if (Objects.isNull(randomWebDriver)) {
            initNewWebDriverOnIndex(randomIndexOfWebDrivers, userAgent, readTimeout);
            return webDrivers.get(randomIndexOfWebDrivers);
        }

        log.debug("WEB DRIVER :: WebDriver {} 에서 응답합니다", randomIndexOfWebDrivers);

        return randomWebDriver;
    }

    public void clearAllWebDrivers() {
        log.debug("WEB DRIVER :: 모든 WebDriver 를 Clear 합니다");
        this.webDrivers = new HashMap<>();
    }


    private void initNewWebDriverOnIndex(int webDriverIndex, String userAgent, Long readTimeout) {
        final String webDriverRemoteUrl = webDriverRemoteUrls.get(webDriverIndex);

        log.debug("WEB DRIVER :: WebDriver{} 을 초기화 합니다, RemoteUrl : {}", webDriverIndex, webDriverRemoteUrl);

        final ChromeOptions chromeOptions = getDefaultChromeOptions(userAgent);

        try {
            final WebDriver driver = new RemoteWebDriver(new URL(webDriverRemoteUrl), chromeOptions);

            driver.manage().timeouts().pageLoadTimeout(readTimeout, MILLISECONDS);
            driver.manage().timeouts().implicitlyWait(readTimeout, MILLISECONDS);
            driver.manage().timeouts().setScriptTimeout(readTimeout, MILLISECONDS);

            this.webDrivers.put(webDriverIndex, driver);
        } catch (MalformedURLException e) {
            log.error("WEB DRIVER :: WebDriver URL 이 잘못되었습니다", e);
        }
    }

    private ChromeOptions getDefaultChromeOptions(String userAgent) {
        final ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("headless");
        chromeOptions.addArguments("disable-gpu");
        chromeOptions.addArguments("user-agent=" + userAgent);
        return chromeOptions;
    }


}
