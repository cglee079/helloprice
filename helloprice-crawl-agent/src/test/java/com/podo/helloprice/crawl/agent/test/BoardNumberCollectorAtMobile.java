package com.podo.helloprice.crawl.agent.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
class BoardNumberCollectorAtMobile {

    private static final String FILE = "c://dev/hello.txt";
    private static final String NEXT_PAGE_SELECTOR = "#pagination_div > a.next";
    private static final String START_BOARD_LIST = "https://m.dcinside.com/board/etc_program?s_type=name&serval=%EB%8F%90%EC%84%9D&recommend=&headid=";
    private static final String BOARD_URL_SELECTOR = "body > div > div > div > section:nth-child(4) > ul > li";
    private static final String PAGE_BUTTON_SELECTOR = "#pagination_div > span > a";
    public static final int MAX_BOARD_LIST = Integer.MAX_VALUE;
    private Set<String> boardListUrls = new LinkedHashSet<>();
    private Map<String, String> borderUrls = new HashMap<>();

    @Test
    void dd() {
        final HashSet<String> strings = new HashSet<>();
        final List<String> aa = FileUtil.readByLine("c://dev/hello-1.txt");
        final List<String> bb = FileUtil.readByLine("c://dev/hello-2.txt");

        strings.addAll(aa);
        strings.addAll(bb);

        System.out.println(strings.size());

        final List<String> collect = strings.stream()
                .sorted()
                .collect(Collectors.toList());

        this.writeFile(collect, new File("c://dev/boards.txt"));
    }

    @Test
    void test() {

        final ChromeDriver chromeDriver = ChromeDriverUtil.get();

        boardListUrls.add(START_BOARD_LIST);
        ChromeDriverUtil.movePage(chromeDriver, START_BOARD_LIST);

        collectBoardList(chromeDriver, MAX_BOARD_LIST);
        final List<String> collect = new ArrayList<>(boardListUrls).stream()
                .sorted()
                .collect(Collectors.toList());

        writeFile(collect, new File("c://dev/board-list.txt"));

        boardListUrls.addAll(FileUtil.readByLine("c://dev/board-list.txt"));

        log.info("총게시글 리스트 수 : " + boardListUrls.size());

        for (String url : boardListUrls) {
            log.info("게시글 번호 가져오기 : " + url);
            getBoardUrl(chromeDriver, url);

            final List<String> collect2 = new ArrayList<>(borderUrls.values()).stream()
                    .sorted()
                    .collect(Collectors.toList())
                    .stream()
                    .sorted(Comparator.reverseOrder())
                    .collect(Collectors.toList());


            writeFile(collect2, new File(FILE));
        }

        log.info("총게시글 수 : " + borderUrls.size());

    }

    private void writeFile(List<String> borderUrls, File file) {

        try {
            FileWriter fw = new FileWriter(file);

            for (String s : borderUrls) {
                fw.write(s + "\n");
            }
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void collectBoardList(ChromeDriver chromeDriver, int maxBoardList) {
        final List<String> pageUrls = collectPageUrl(chromeDriver, PAGE_BUTTON_SELECTOR);

        this.boardListUrls.addAll(pageUrls);

        if (boardListUrls.size() > maxBoardList) {
            return;
        }

        //NEXT
        try {
            final WebElement element = ElementUtil.getElement(chromeDriver, NEXT_PAGE_SELECTOR);
            if (Objects.nonNull(element)) {
                if (element.getAttribute("href").equals("javascript:;")) {
                    log.info("더이상 다음 페이지가 없습니다.");
                    return;
                }
                log.info("다음 페이지로 이동");
                element.click();
                ThreadUtil.sleep(3000);
                collectBoardList(chromeDriver, maxBoardList);
            }
        } catch (Exception e) {
            log.info(chromeDriver.getCurrentUrl());
            e.printStackTrace();
        }
    }

    private List<String> collectPageUrl(ChromeDriver chromeDriver, String pageButtonSelector) {
        final List<String> strings = new ArrayList<>();
        final List<WebElement> elements = ElementUtil.getElements(chromeDriver, pageButtonSelector);

        for (WebElement element : elements) {
            final String src = element.getAttribute("href");
            log.info("페이지 URL 저장 : {}", src);
            strings.add(src);
        }

        return strings;
    }

    private void getBoardUrl(ChromeDriver chromeDriver, String url) {

        ChromeDriverUtil.movePage(chromeDriver, url);

        ThreadUtil.sleep(2000);

        final List<WebElement> elements = ElementUtil.getElements(chromeDriver, BOARD_URL_SELECTOR);

        for (WebElement element : elements) {
            WebElement name;
            String writer;
            String title;

            System.out.println(element);

            try {
                final WebElement element1 = element.findElement(By.cssSelector("span.blockInfo"));
                writer = element1.getAttribute("data-info");
            } catch (Exception e) {
                writer = "";
            }

            try {
                name = element.findElement(By.cssSelector("div > a.lt > ul > li:nth-child(1) > span:nth-child(1)"));
            } catch (Exception e) {
                name = null;
            }


            try {
                final WebElement titleElement = element.findElement(By.cssSelector("div > a.lt > span > span:last-child"));
                title = titleElement.getText();
            } catch (Exception e) {
                title = "@@";
            }


            WebElement gonick;
            try {
                gonick = element.findElement(By.cssSelector("div > a.lt > ul > li:nth-child(1) > span.sp-nick.gonick"));
            } catch (Exception e) {
                gonick = null;
            }


            if (Objects.nonNull(name) && name.getText().equals("돐석") && Objects.nonNull(gonick)) {
                final String urlstr = element.findElement(By.cssSelector("div > a.lt")).getAttribute(("href")).trim();
                final String urlstr2 = urlstr.substring(0, urlstr.indexOf("?"));
                final String e = writer
                        + ".\t\t"
                        + urlstr2
                        + ".\t\t"
                        + title
                        .trim();

                if (!borderUrls.containsKey(urlstr2)) {
                    borderUrls.put(urlstr2, e);
                }

            } else {
                log.info("아님 : " + element.getAttribute("href"));
            }

        }
    }


}
