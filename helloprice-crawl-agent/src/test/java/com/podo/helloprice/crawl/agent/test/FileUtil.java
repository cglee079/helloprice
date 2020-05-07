package com.podo.helloprice.crawl.agent.test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

    public static List<String> readByLine(String filePath) {

        final ArrayList<String> lines = new ArrayList<>();

        try {

            final FileReader fileReader = new FileReader(new File(filePath));
            String line;

            BufferedReader bufferReader = new BufferedReader(fileReader);

            while ((line = bufferReader.readLine()) != null) {
                lines.add(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return lines;
    }
}
