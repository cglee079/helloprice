package com.podo.helloprice.crawl.worker.util;


import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.Arrays;
import java.util.stream.Stream;

public class TestUtil {

    public static String getStringFromResource(String requirePath, String... otherPaths) {
        String[] requirePathInResources = {"src", "test", "resources", requirePath};

        final String[] filePath = Stream.concat(Arrays.stream(requirePathInResources), Arrays.stream(otherPaths))
                .toArray(String[]::new);

        final File file = FileUtils.getFile(filePath);

        return readFileToString(file);
    }

    private static String readFileToString(File file) {
        StringBuilder contentBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file),"utf-8"))) {

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

