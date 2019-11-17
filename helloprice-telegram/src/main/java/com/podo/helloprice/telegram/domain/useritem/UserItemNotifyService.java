package com.podo.helloprice.telegram.domain.useritem;

import com.podo.helloprice.core.domain.item.Item;
import com.podo.helloprice.core.domain.item.ItemRepository;
import com.podo.helloprice.core.domain.user.User;
import com.podo.helloprice.core.domain.user.UserRepository;
import com.podo.helloprice.core.domain.user.UserStatus;
import com.podo.helloprice.core.domain.useritem.UserItemNotify;
import com.podo.helloprice.core.domain.useritem.UserItemNotifyRepository;
import com.podo.helloprice.telegram.domain.item.ItemDto;
import com.podo.helloprice.telegram.domain.user.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class UserItemNotifyService {

    @Value("${user.max_item}")
    private Integer maxItemsPerUser;

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final UserItemNotifyRepository userItemNotifyRepository;

    public boolean hasNotify(Long userId, Long itemId) {
        return userItemNotifyRepository.findByUserIdAndItemId(userId, itemId) != null;

    }

    public void addNotify(Long userId, Long itemId) {
        User user = userRepository.findById(userId).get();
        Item item = itemRepository.findById(itemId).get();

        UserItemNotify userItemNotify = new UserItemNotify(user, item);
        userItemNotifyRepository.save(userItemNotify);

        user.addUserItemNotify(userItemNotify);
        item.addUserItemNotify(userItemNotify);
    }

    public boolean hasMaxNotifyByUserTelegramId(String telegramId) {

        List<UserItemNotify> userItemNotifies = userItemNotifyRepository.findByUserTelegramId(telegramId);
        if (userItemNotifies.size() > maxItemsPerUser) {
            return true;
        }
        return false;
    }

    private void deleteNotify(UserItemNotify userItemNotify) {
        User user = userItemNotify.getUser();
        Item item = userItemNotify.getItem();

        log.info("{}님의 {}({}) 상품 알림을 삭제합니다.", user.getTelegramId(), item.getItemName(), item.getItemCode());

        user.removeUserItemNotify(userItemNotify);
        item.removeUserItemNotify(userItemNotify);

        userItemNotifyRepository.delete(userItemNotify);
    }

    public void deleteNotifies(Long itemId) {
        final List<UserItemNotify> userItemNotifies = userItemNotifyRepository.findByItemId(itemId);

        for (UserItemNotify userItemNotify : userItemNotifies) {
            this.deleteNotify(userItemNotify);
        }

    }

    public void deleteNotifyByUserIdAndItemId(Long userId, Long itemId) {
        UserItemNotify userItemNotify = userItemNotifyRepository.findByUserIdAndItemId(userId, itemId);
        deleteNotify(userItemNotify);
    }

    public List<UserDto.detail> findNotifyUsersByItemId(Long itemId, UserStatus userStatus) {
        List<UserItemNotify> userItemNotifies = userItemNotifyRepository.findByItemIdAndUserStatus(itemId, userStatus);

        return userItemNotifies.stream()
                .map(notify -> new UserDto.detail(notify.getUser()))
                .collect(Collectors.toList());
    }

    public List<ItemDto.detail> findNotifyItemsByUserTelegramId(String telegramId) {
        List<UserItemNotify> userItemNotifies = userItemNotifyRepository.findByUserTelegramId(telegramId);

        return userItemNotifies.stream()
                .map(notify -> new ItemDto.detail(notify.getItem()))
                .collect(Collectors.toList());
    }


    public void updateNotifyAt(Long itemId, LocalDateTime notifyAt) {
        final List<UserItemNotify> userItemNotifies = userItemNotifyRepository.findByItemId(itemId);
        for (UserItemNotify userItemNotify : userItemNotifies) {
            userItemNotify.updateNotifiedAt(notifyAt);
        }
    }
}
