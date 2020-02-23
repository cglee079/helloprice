package com.podo.helloprice.telegram.client.menu.emaildelete;

import com.podo.helloprice.core.model.Menu;
import com.podo.helloprice.telegram.client.TMessageCallbackFactory;
import com.podo.helloprice.telegram.client.vo.TMessageVo;
import com.podo.helloprice.telegram.client.menu.AbstractMenuHandler;
import com.podo.helloprice.telegram.client.menu.Keyboard;
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
        return Menu.EMAIL_DELETE;
    }

    private final UserService userService;
    private final UserItemNotifyService userItemNotifyService;
    private final TMessageCallbackFactory callbackFactory;

    public void handle(TMessageVo tMessageVo, String requestMessage) {

        final String telegramId = tMessageVo.getTelegramId();

        log.info("{} << 이메일 삭제 메뉴에서 응답, 받은메세지 '{}'", telegramId, requestMessage);

        final List<String> itemCommands = ItemCommandTranslator.getItemCommands(userItemNotifyService.findNotifyItemsByUserTelegramId(telegramId));

        final EmailDeleteCommand requestCommand = EmailDeleteCommand.from(requestMessage);
        if (Objects.isNull(requestCommand)) {
            sender().send(tMessageVo.newMessage(CommonResponse.wrongInput(), Keyboard.getHomeKeyboard(itemCommands), callbackFactory.create(telegramId, Menu.HOME)));
            return;
        }

        handleEmailDeleteCommand(requestCommand, tMessageVo, itemCommands);
    }

    private void handleEmailDeleteCommand(EmailDeleteCommand requestCommand, TMessageVo tMessageVo, List<String> itemCommands) {
        switch (requestCommand) {
            case YES:
                handleYesCommand(tMessageVo, itemCommands);
                break;
            case NO:
                sender().send(tMessageVo.newMessage(CommonResponse.toHome(), Keyboard.getHomeKeyboard(itemCommands), callbackFactory.create(tMessageVo.getTelegramId(), Menu.HOME)));
        }
    }

    private void handleYesCommand(TMessageVo tMessageVo, List<String> itemCommands) {
        final String telegramId = tMessageVo.getTelegramId();

        userService.updateEmailByTelegramId(telegramId, null);

        sender().send(tMessageVo.newMessage(EmailDeleteResponse.success(), Keyboard.getHomeKeyboard(itemCommands), callbackFactory.create(tMessageVo.getTelegramId(), Menu.HOME)));
    }

}
