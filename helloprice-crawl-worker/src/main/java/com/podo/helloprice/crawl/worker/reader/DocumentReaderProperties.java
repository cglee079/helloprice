package com.podo.helloprice.crawl.worker.reader;

import com.podo.helloprice.core.util.StringUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Objects;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "crawler")
public class DocumentReaderProperties {
    
    private String useragent;
    private Long readTimeout;
    private List<String> webdriverRemotes;

    @PostConstruct
    public void validate() {
        if(StringUtil.isEmpty(useragent)){
            throw new IllegalArgumentException("`crawler.useragent` properties 를 설정해주세요");
        }

        if(Objects.isNull(readTimeout)){
            throw new IllegalArgumentException("`crawler.read-timeout` properties 를 설정해주세요");
        }
    }

    public void setWebdriverRemotes(List<String> webdriverRemotes) {
        this.webdriverRemotes = webdriverRemotes;
    }
}
