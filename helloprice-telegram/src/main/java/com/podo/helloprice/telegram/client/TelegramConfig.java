package com.podo.helloprice.telegram.client;

import com.podo.helloprice.core.domain.model.Menu;
import com.podo.helloprice.telegram.client.menu.MenuHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

@RequiredArgsConstructor
@Slf4j
@Configuration
public class TelegramConfig {


    private final List<MenuHandler> menuHandlers;

    @Bean
    public Map<Menu, MenuHandler> menuHandlers() {
        return menuHandlers.stream()
                .collect(toMap(MenuHandler::getHandleMenu, x -> x));
    }

}
