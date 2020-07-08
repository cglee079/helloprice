package com.podo.helloprice.product.update.analysis.processor.notify.helper;

import lombok.experimental.UtilityClass;
import org.springframework.util.StringUtils;

import java.util.Objects;

@UtilityClass
public class EmailContentCreator {

    public static String create(String imageUrl, String contents){
        final StringBuilder htmlContents = new StringBuilder();

        htmlContents.append("<div>");

        if (!StringUtils.isEmpty(imageUrl)) {
            htmlContents.append("<div>")
                    .append("<img src='")
                    .append(imageUrl)
                    .append("'/>")
                    .append("<br/>")
                    .append("<br/>")
                    .append("</div>");
        }

        htmlContents.append("<div>")
                .append(contents.replaceAll("\n", "<br/>"))
                .append("</div>");

        htmlContents.append("</div>");

        return htmlContents.toString();
    }
}
