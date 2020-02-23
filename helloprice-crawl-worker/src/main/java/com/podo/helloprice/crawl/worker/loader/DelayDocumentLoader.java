package com.podo.helloprice.crawl.worker.loader;

import com.podo.helloprice.crawl.worker.exception.FailGetDocumentException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class DelayDocumentLoader {

    @Value("${crawler.useragent}")
    private String userAgent;

    @Value("${crawler.timeout}")
    private Integer timeout;

    private final DelayWebDriverManager delayWebDriverManager;

    public final Document getDocument(String url, List<String> waitElementSelectors) throws FailGetDocumentException {

        WebDriver webDriver = delayWebDriverManager.getRandomWebDriver();

        try {
            webDriver.get(url);
        } catch (TimeoutException e1) {
            throw new FailGetDocumentException(e1);
        } catch (NoSuchSessionException e2) {
            log.error("WebDriver 세션을 찾을 수 없습니다", e2);
            delayWebDriverManager.clearAllWebDrivers();
            return getDocument(url, waitElementSelectors);
        } catch (Exception e3) {
            log.error("WebDriver 알수 없는 에러", e3);
            delayWebDriverManager.clearAllWebDrivers();
            throw new FailGetDocumentException(e3);
        }

        waitByElementCssSelectors(webDriver, waitElementSelectors);

        return Jsoup.parse(webDriver.getPageSource());
    }

    private void waitByElementCssSelectors(final WebDriver webDriver, final List<String> waitElementCssSelectors) throws FailGetDocumentException {
        if (Objects.isNull(waitElementCssSelectors)) {
            return;
        }

        final WebDriverWait wait = new WebDriverWait(webDriver, timeout / 1000);

        final ExpectedCondition<Boolean> documentComplete =
                driver -> ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");

        final List<ExpectedCondition<Boolean>> waitElements = waitElementCssSelectors.stream()
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
