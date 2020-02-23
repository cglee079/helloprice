package com.podo.helloprice.crawl.worker.util;


import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Stream;

public class TestUtil {

    public static String getStringFromResource(String requirePath, String... otherPaths) {
        String[] requirePathInResouces = {"src", "test", "resources", requirePath};

        final String[] filePath = Stream.concat(Arrays.stream(requirePathInResouces), Arrays.stream(otherPaths))
                .toArray(String[]::new);

        final File file = FileUtils.getFile(filePath);

        return readFileToString(file);
    }

    private static String readFileToString(File file) {
        StringBuilder contentBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                contentBuilder.append(sCurrentLine).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contentBuilder.toString();
    }
}
