package com.podo.helloprice.pooler.loader;

import com.podo.helloprice.pooler.target.danawa.DanawaPoolConfig;
import com.podo.helloprice.pooler.target.danawa.DanawaPooler;
import com.podo.helloprice.pooler.exception.FailGetDocumentException;
import com.querydsl.core.types.dsl.TimeOperation;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;


@Component
public class DelayDocumentLoader {

    @Value("${pooler.useragent}")
    private String userAgent;

    @Value("${pooler.timeout}")
    private Integer timeout;

    private WebDriver driver;

    private void initDriver() {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("headless");
        chromeOptions.addArguments("disable-gpu");
        chromeOptions.addArguments("user-agent=" + userAgent);

        driver = new ChromeDriver(chromeOptions);

        driver.manage().timeouts().pageLoadTimeout(timeout, TimeUnit.MILLISECONDS);
        driver.manage().timeouts().implicitlyWait(timeout, TimeUnit.MILLISECONDS);
        driver.manage().timeouts().setScriptTimeout(timeout, TimeUnit.MILLISECONDS);
    }

    public final Document getDocument(String url, List<String> waitElementSelectors) throws FailGetDocumentException {

        if (Objects.isNull(driver)) {
            initDriver();
        }

        try {
            driver.get(url);
        } catch (org.openqa.selenium.TimeoutException e) {
            throw new FailGetDocumentException(e);
        }

        //Wait Element Loading Complete.
        waitElement(driver, waitElementSelectors);

        WebElement html = driver.findElement(By.cssSelector("html"));
        return Jsoup.parse(html.getAttribute("innerHTML"));
    }

    private void waitElement(final WebDriver webDriver, final List<String> waitElementSelectors) throws FailGetDocumentException {
        if (Objects.isNull(waitElementSelectors)) {
            return;
        }

        final WebDriverWait wait = new WebDriverWait(webDriver, timeout / 1000);

        final ExpectedCondition<Boolean> documentComplete =
                driver -> ((JavascriptExecutor) driver).executeScript(
                        "return document.readyState").equals("complete");

        //Wait.. Loading Element..
        final List<ExpectedCondition<Boolean>> waitElements = waitElementSelectors.stream()
                .map(w -> ExpectedConditions.and(ExpectedConditions.presenceOfElementLocated(By.cssSelector(w))))
                .collect(Collectors.toList());

        waitElements.add(documentComplete);

        try {
            wait.until(ExpectedConditions.and(waitElements.toArray(new ExpectedCondition[0])));
        } catch (Exception e) {
            throw new FailGetDocumentException(e);
        }

    }
}
