package com.podo.helloprice.crawl.agent.test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ChromeDriverUtil {

    public static void movePage(ChromeDriver chromeDriver, String startUrl) {
        chromeDriver.get(startUrl);
    }

    public static ChromeDriver get() {
        try {
            final ChromeDriverService defaultService = new ChromeDriverService(new File("c://chromedriver.exe"), 9515, ImmutableList.<String>builder().build(), ImmutableMap.<String, String>builder().build());
            return new ChromeDriver(defaultService, getMobileChromeOptions());
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private static ChromeOptions getMobileChromeOptions() {
        final ChromeOptions chromeOptions = new ChromeOptions();
//        chromeOptions.addArguments("headless");
//        chromeOptions.addArguments("disable-gpu");
        Map<String, Object> deviceMetrics = new HashMap<>();
        deviceMetrics.put("width", 360);
        deviceMetrics.put("height", 1000);
        deviceMetrics.put("pixelRatio", 3.0);
        Map<String, Object> mobileEmulation = new HashMap<>();
        mobileEmulation.put("deviceMetrics", deviceMetrics);
        mobileEmulation.put("userAgent", "Mozilla/5.0 (Linux; Android 8.0.0;" +
                "Pixel 2 XL Build/OPD1.170816.004) AppleWebKit/537.36 (KHTML, like Gecko) " +
                "Chrome/67.0.3396.99 Mobile Safari/537.36");
        chromeOptions.setExperimentalOption("mobileEmulation", mobileEmulation);
        return chromeOptions;
    }


}
