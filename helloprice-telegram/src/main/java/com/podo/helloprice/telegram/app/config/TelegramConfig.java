package com.podo.helloprice.telegram.app.config;


import com.podo.helloprice.telegram.app.menu.MenuHandler;
import com.podo.helloprice.telegram.app.menu.Menu;
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
