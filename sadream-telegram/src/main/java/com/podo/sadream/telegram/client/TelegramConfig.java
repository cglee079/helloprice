package com.podo.sadream.telegram.client;

import com.podo.itemwatcher.core.domain.user.Menu;
import com.podo.sadream.telegram.client.menu.MenuHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

@Configuration
@RequiredArgsConstructor
public class TelegramConfig {

    private final List<MenuHandler> menuHandlers;

    @Bean
    public Map<Menu, MenuHandler> menuHandlers() {
        return menuHandlers.stream()
                .collect(toMap(MenuHandler::getHandleMenu, x -> x));
    }

}
