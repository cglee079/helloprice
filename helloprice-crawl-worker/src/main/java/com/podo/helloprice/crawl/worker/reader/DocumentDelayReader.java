package com.podo.helloprice.crawl.worker.reader;

import com.podo.helloprice.crawl.worker.exception.FailReadDocumentException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class DocumentDelayReader {

    private final String userAgent;
    private final Long readTimeout;
    private final WebDriverManager webDriverManager;

    public final Document getDocument(String url, List<String> waitElementSelectors) throws FailReadDocumentException {

        WebDriver webDriver = webDriverManager.getRandomWebDriver(userAgent, readTimeout);

        try {
            webDriver.get(url);
        } catch (TimeoutException e1) {
            throw new FailReadDocumentException(e1);

        } catch (NoSuchSessionException e2) {
            log.warn("WEB DRIVER :: ERROR :: WebDriver 세션을 찾을 수 없습니다", e2);
            webDriverManager.clearAllWebDrivers();
            return getDocument(url, waitElementSelectors);

        } catch (Exception e3) {
            log.error("WEB DRIVER :: ERROR :: WebDriver 알수 없는 에러", e3);
            webDriverManager.clearAllWebDrivers();
            throw new FailReadDocumentException(e3);

        }

        waitByElementCssSelectors(webDriver, waitElementSelectors);

        return Jsoup.parse(webDriver.getPageSource());
    }

    private void waitByElementCssSelectors(final WebDriver webDriver, final List<String> waitElementCssSelectors) throws FailReadDocumentException {
        if (Objects.isNull(waitElementCssSelectors)) {
            return;
        }

        final WebDriverWait wait = new WebDriverWait(webDriver, readTimeout / 1000);

        final ExpectedCondition<Boolean> documentComplete =
                driver -> ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");

        final List<ExpectedCondition<Boolean>> waitElements = waitElementCssSelectors.stream()
                .map(w -> ExpectedConditions.and(ExpectedConditions.presenceOfElementLocated(By.cssSelector(w))))
                .collect(Collectors.toList());

        waitElements.add(documentComplete);

        try {
            wait.until(w -> ExpectedConditions.and(waitElements.toArray(new ExpectedCondition[0])).apply(w));
        } catch (Exception e) {
            throw new FailReadDocumentException(e);
        }
    }

}
