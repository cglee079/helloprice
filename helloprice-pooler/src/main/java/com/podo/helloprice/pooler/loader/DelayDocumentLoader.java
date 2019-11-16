package com.podo.helloprice.pooler.loader;

import com.podo.helloprice.pooler.exception.FailGetDocumentException;
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

    @Value("${pooler.useragent}")
    private String userAgent;

    @Value("${pooler.timeout}")
    private Integer timeout;

    private final DelayWebDriverManager delayWebDriverManager;

    public final Document getDocument(String url, List<String> waitElementSelectors) throws FailGetDocumentException {

        WebDriver randDriver = delayWebDriverManager.getRandDriver();

        try {
            randDriver.get(url);
        } catch (org.openqa.selenium.TimeoutException e1) {
            throw new FailGetDocumentException(e1);
        } catch (NoSuchSessionException e2) {
            //세션 에러면, WebDriver 재연결후, 문서를 다시 요청함.
            log.error("WebDriver 세션을 찾을 수 없습니다", e2);
            delayWebDriverManager.clearDrivers();
            return getDocument(url, waitElementSelectors);
        } catch (Exception e3) {
            //알 수 없는 에러면, 초기화하고, 서버에러 메세지 전송
            log.error("WebDriver 알수 없는 에러", e3);
            delayWebDriverManager.clearDrivers();
            throw new FailGetDocumentException(e3);
        }

        //Wait Element Loading Complete.
        waitElement(randDriver, waitElementSelectors);

        WebElement html = randDriver.findElement(By.cssSelector("html"));
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
