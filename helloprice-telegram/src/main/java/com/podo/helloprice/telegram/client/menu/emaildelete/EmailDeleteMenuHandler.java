package com.podo.helloprice.telegram.client.menu.emaildelete;

import com.podo.helloprice.core.domain.user.Menu;
import com.podo.helloprice.telegram.client.TMessageCallbackFactory;
import com.podo.helloprice.telegram.client.TMessageVo;
import com.podo.helloprice.telegram.client.menu.AbstractMenuHandler;
import com.podo.helloprice.telegram.client.menu.KeyboardManager;
import com.podo.helloprice.telegram.client.menu.global.ItemCommandTranslator;
import com.podo.helloprice.telegram.domain.user.UserService;
import com.podo.helloprice.telegram.domain.useritem.UserItemNotifyService;
import com.podo.helloprice.telegram.client.menu.global.CommonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Component
public class EmailDeleteMenuHandler extends AbstractMenuHandler {

    @Override
    public Menu getHandleMenu() {
        return Menu.EMAILL_DELETE;
    }

    private final UserService userService;
    private final UserItemNotifyService userItemNotifyService;
    private final TMessageCallbackFactory callbackFactory;
    private final KeyboardManager km;

    public void handle(TMessageVo tMessageVo, String requestMessage) {

        final String telegramId = tMessageVo.getTelegramId() + "";

        log.info("{} << 이메일 삭제 메뉴에서 응답, 받은메세지 '{}'", telegramId, requestMessage);

        final List<String> itemCommands = ItemCommandTranslator.getItemCommands(userItemNotifyService.findNotifyItemsByUserTelegramId(telegramId));


        //명령어가 아닌 경우
        final EmailDeleteCommand requestCommand = EmailDeleteCommand.from(requestMessage);
        if (Objects.isNull(requestCommand)) {
            getSender().send(tMessageVo.newMessage(CommonResponse.wrongInput(), km.getHomeKeyboard(itemCommands), callbackFactory.createDefault(telegramId, Menu.HOME)));
            return;
        }

        handleCommand(requestCommand, tMessageVo, itemCommands);
    }

    private void handleCommand(EmailDeleteCommand requestCommand, TMessageVo tMessageVo, List<String> itemCommands) {
        switch (requestCommand) {
            case YES:
                handleYesCommand(tMessageVo, itemCommands);
                break;
            case NO:
                getSender().send(tMessageVo.newMessage(CommonResponse.toHome(), km.getHomeKeyboard(itemCommands), callbackFactory.createDefault(tMessageVo.getTelegramId() + "", Menu.HOME)));
        }
    }

    private void handleYesCommand(TMessageVo tMessageVo, List<String> itemCommands) {
        final String telegramId = tMessageVo.getTelegramId() + "";

        userService.updateEmail(telegramId, null);

        getSender().send(tMessageVo.newMessage(EmailDeleteResponse.success(), km.getHomeKeyboard(itemCommands), callbackFactory.createDefault(tMessageVo.getTelegramId() + "", Menu.HOME)));
    }

}
