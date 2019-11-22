package com.podo.helloprice.telegram.client.menu.home;

import com.podo.helloprice.core.domain.user.Menu;
import com.podo.helloprice.crawler.target.danawa.DanawaCrawlConfig;
import com.podo.helloprice.telegram.client.menu.emailadd.EmailAddResponse;
import com.podo.helloprice.telegram.client.menu.emaildelete.EmailDeleteResponse;
import com.podo.helloprice.telegram.client.menu.itemsearch.ItemSearchResponse;
import com.podo.helloprice.telegram.domain.user.UserDto;
import com.podo.helloprice.telegram.domain.user.UserService;
import com.podo.helloprice.telegram.client.menu.global.CommonResponse;
import com.podo.helloprice.telegram.client.menu.KeyboardManager;
import com.podo.helloprice.telegram.client.TMessageCallbackFactory;
import com.podo.helloprice.telegram.client.TMessageVo;
import com.podo.helloprice.telegram.client.menu.AbstractMenuHandler;
import com.podo.helloprice.telegram.client.menu.itemadd.ItemAddResponse;
import com.podo.helloprice.telegram.client.menu.itemdelete.ItemDeleteResponse;
import com.podo.helloprice.telegram.domain.item.ItemDto;
import com.podo.helloprice.telegram.domain.item.ItemService;
import com.podo.helloprice.telegram.client.menu.global.ItemCommandTranslator;
import com.podo.helloprice.telegram.domain.useritem.UserItemNotifyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Component
public class HomeMenuHandler extends AbstractMenuHandler {

    @Value("${app.name} ${app.version}")
    private String appDesc;

    @Value("${app.telegram.help_url}")
    private String helpUrl;

    private final ItemService itemService;
    private final UserService userService;
    private final UserItemNotifyService userItemNotifyService;
    private final TMessageCallbackFactory callbackFactory;
    private final KeyboardManager km;

    @Override
    public Menu getHandleMenu() {
        return Menu.HOME;
    }

    public void handle(TMessageVo tMessageVo, String requestMessage) {
        final String telegramId = tMessageVo.getTelegramId();
        final List<String> itemCommands = ItemCommandTranslator.getItemCommands(userItemNotifyService.findNotifyItemsByUserTelegramId(telegramId));

        log.info("{} << 홈메뉴에서 응답, 받은메세지 '{}'", telegramId, requestMessage);


        final HomeCommand requestHomeCommand = HomeCommand.from(requestMessage);

        if (Objects.nonNull(requestHomeCommand)) {
            handleCommand(tMessageVo, requestMessage, telegramId, requestHomeCommand, itemCommands);
            return;
        }

        final String itemCode = ItemCommandTranslator.getItemCodeFromCommand(requestMessage);
        if (Objects.isNull(itemCode)) {
            log.info("{} << 응답 할 수 없는 메세지 입니다 받은메세지 '{}'", telegramId, requestMessage);
            sender().send(tMessageVo.newMessage(CommonResponse.wrongInput(), km.getHomeKeyboard(itemCommands), callbackFactory.createDefault(telegramId, Menu.HOME)));
            return;
        }

        if (!itemService.isExistedByItemCode(itemCode)) {
            log.info("{} << 잘못된 상품코드 메세지입니다. 받은메세지 '{}'", telegramId, requestMessage);
            sender().send(tMessageVo.newMessage(HomeResponse.wrongItemCode(itemCode), km.getHomeKeyboard(itemCommands), callbackFactory.createDefault(telegramId, Menu.HOME)));
            return;
        }

        log.info("{} << 상품정보 요청을 확인했습니다. 받은메세지 '{}'", telegramId, requestMessage);
        handleItemCommands(tMessageVo, telegramId, itemCode);
    }

    private void handleItemCommands(TMessageVo tMessageVo, String telegramId, String itemCode) {
        final ItemDto.detail itemDetail = itemService.findByItemCode(itemCode);
        sender().send(tMessageVo.newMessage(HomeResponse.itemDetail(itemDetail), itemDetail.getItemImage(), null, callbackFactory.createDefault(telegramId, Menu.HOME)));
    }

    private void handleCommand(TMessageVo tMessageVo, String requestMessage, String telegramId, HomeCommand requestCommand, List<String> itemCommands) {
        switch (requestCommand) {
            case ITEM_SEARCH_ADD:
                log.info("{} << 상품 검색 메뉴로 이동. 받은메세지 '{}'", telegramId, requestMessage);
                sender().send(tMessageVo.newMessage(ItemSearchResponse.explain(), km.getDefaultKeyboard(), callbackFactory.createDefault(telegramId, Menu.ITEM_SEARCH)));
                break;

            case ITEM_ADD:
                log.info("{} << 상품 추가 메뉴로 이동. 받은메세지 '{}'", telegramId, requestMessage);
                sender().send(tMessageVo.newMessage(ItemAddResponse.explain(DanawaCrawlConfig.Global.DANAWA_URL, helpUrl), km.getDefaultKeyboard(), callbackFactory.createDefault(telegramId, Menu.ITEM_ADD)));
                break;

            case ITEM_DELETE:
                log.info("{} << 상품 삭제 메뉴로 이동. 받은메세지 '{}'", telegramId, requestMessage);
                sender().send(tMessageVo.newMessage(ItemDeleteResponse.explain(), km.getItemDeleteKeyboard(itemCommands), callbackFactory.createDefault(telegramId, Menu.ITEM_DELETE)));
                break;

            case EMAIL_ADD:
                handleEmailAddCommand(tMessageVo, itemCommands);
                break;

            case EMAIL_DELETE:
                handleEmailDeleteCommand(tMessageVo, itemCommands);
                break;

            case HELP:
                log.info("{} << 도움말. 받은메세지 '{}'", telegramId, requestMessage);
                sender().send(tMessageVo.newMessage(CommonResponse.introduce(appDesc), null, callbackFactory.createDefault(telegramId, Menu.HOME)));
                sender().sendWithWebPagePreview(tMessageVo.newMessage(CommonResponse.help(helpUrl), null, callbackFactory.createDefault(telegramId, null)));
                break;
        }
    }

    private void handleEmailAddCommand(TMessageVo tMessageVo, List<String> itemCommands) {
        final String telegramId = tMessageVo.getTelegramId() + "";

        final UserDto.detail user = userService.findByTelegramId(telegramId);

        final String userEmail = user.getEmail();

        if (Objects.nonNull(userEmail)) {
            log.info("{} << 이미 이메일이 등록되어있습니다. 기존이메일 '{}'", telegramId, userEmail);
            sender().send(tMessageVo.newMessage(HomeResponse.rejectEmailAdd(userEmail), km.getHomeKeyboard(itemCommands), callbackFactory.createDefault(telegramId, Menu.HOME)));
            return;
        }

        log.info("{} << 이메일 추가 메뉴로 이동", telegramId);
        sender().send(tMessageVo.newMessage(EmailAddResponse.explain(), km.getDefaultKeyboard(), callbackFactory.createDefault(telegramId, Menu.EMAIL_ADD)));
    }

    private void handleEmailDeleteCommand(TMessageVo tMessageVo, List<String> itemCommands) {
        final String telegramId = tMessageVo.getTelegramId() + "";

        final UserDto.detail user = userService.findByTelegramId(telegramId);

        final String userEmail = user.getEmail();

        if (Objects.isNull(userEmail)) {
            log.info("{} << 등록되어있는 이메일이 없습니다", telegramId);
            sender().send(tMessageVo.newMessage(HomeResponse.rejectEmailDelete(), km.getHomeKeyboard(itemCommands), callbackFactory.createDefault(telegramId, Menu.HOME)));
            return;
        }

        log.info("{} << 이메일 삭제 메뉴로 이동", telegramId);
        sender().send(tMessageVo.newMessage(EmailDeleteResponse.explain(userEmail), km.getEmailDeleteKeyboard(), callbackFactory.createDefault(telegramId, Menu.EMAILL_DELETE)));
    }
}
