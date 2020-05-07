package com.podo.helloprice.crawl.agent.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Slf4j
class BoardDeleteAtMobile {

    private static final String FILE = "c://dev/hello.txt";

    private static final String LOGIN_PAGE = "https://m.dcinside.com/auth/login?r_url=https%3A%2F%2Fm.dcinside.com";
    private static final String ID = "cks2979";
    private static final String PWD = "dlsrn018";
    private static final String DELETE_BUTTON_SELECTOR = "body > div.container > div > div > section:nth-child(3) > div.gall-thum-btm > div > div.btn-justify-area > button:last-child";

    @Test
    void test() {
        final ChromeDriver webDriver = ChromeDriverUtil.get();

        login(webDriver);

        deleteInBoardList(webDriver);
    }

    private void login(ChromeDriver chromeDriver) {
        ChromeDriverUtil.movePage(chromeDriver, LOGIN_PAGE);

        chromeDriver.findElement(By.cssSelector("#user_id")).sendKeys(ID);
        chromeDriver.findElement(By.cssSelector("#user_pw")).sendKeys(PWD);

        try {
            chromeDriver.findElement(By.cssSelector("#login_ok")).click();
        }catch (Exception e){
        }

        ThreadUtil.sleep(30000);
    }

    private void deleteInBoardList(ChromeDriver webDriver) {

        final List<String> boardUrls = FileUtil.readByLine(FILE).stream()
                .sorted()
                .collect(Collectors.toList())
                .stream()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());

        log.info("게시글 삭제를 시작합니다. SIZE : {}", boardUrls.size());

        removeBoards(webDriver, boardUrls);
    }

    private void removeBoards(ChromeDriver webDriver, List<String> boardUrls) {
        for (int i = 0; i < boardUrls.size(); i++) {
            ThreadUtil.sleep(2000 + new Random().nextInt(2000));

            String url = boardUrls.get(i);

            log.info("게시글 : {}", url);

            try {
                webDriver.get(url);

                final WebElement element = ElementUtil.getElement(webDriver, DELETE_BUTTON_SELECTOR);

                ElementUtil.scroll(webDriver, element);
                ThreadUtil.sleep(1000);

                element.click();

                ThreadUtil.sleep(1000);

                try {
                    final Alert alert = webDriver.switchTo().alert();
                    if (alert.getText().contains("잠시후")) {
                        throw new RuntimeException("잠시후");
                    }

                    alert.accept();
                } catch (Exception e) {
                    log.info("Alert이 없습니다, {}", url);
                }

                log.info("@@ 게시글 삭제 :: 성공 :: {}", url);
            } catch (Exception e) {
                log.info("## 게시글 삭제 :: 실패 ::  {} : {}", url, e.getMessage());

                if (e.getMessage().contains("잠시후")) {
                    webDriver.close();
                    final ChromeDriver newWebDriver = ChromeDriverUtil.get();

                    login(newWebDriver);
                    removeBoards(newWebDriver, new ArrayList<>(boardUrls.subList(i, boardUrls.size())));

                    log.info("잠시후 다시 시도 해달래요...");
                    ThreadUtil.sleep(60000);
                }

            }
        }
    }


}
