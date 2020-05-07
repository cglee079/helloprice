package com.podo.helloprice.crawl.agent.test;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.openqa.selenium.By.cssSelector;

public class ElementUtil {


    public static void scroll(JavascriptExecutor webDriver, WebElement element) {
        webDriver.executeScript("arguments[0].scrollIntoView(true);", element);
    }

    public static WebElement getElement(ChromeDriver chromeDriver, String selector) {
        wait(chromeDriver, selector);

        return chromeDriver.findElement(cssSelector(selector));
    }

    public static List<WebElement> getElements(ChromeDriver chromeDriver, String selector) {
        wait(chromeDriver, selector);

        return chromeDriver.findElements(cssSelector(selector));
    }

    private static void wait(ChromeDriver chromeDriver, String selector) {
        final WebDriverWait wait = new WebDriverWait(chromeDriver, 5);

        final ExpectedCondition<Boolean> documentComplete =
                driver -> ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");

        final List<ExpectedCondition<Boolean>> waitElements = Stream.of(selector)
                .map(w -> ExpectedConditions.and(ExpectedConditions.presenceOfElementLocated(By.cssSelector(w))))
                .collect(Collectors.toList());

        waitElements.add(documentComplete);

        try {
            wait.until(w -> ExpectedConditions.and(waitElements.toArray(new ExpectedCondition[0])).apply(w));
        } catch (Exception e) {
            return;
        }
    }



}
