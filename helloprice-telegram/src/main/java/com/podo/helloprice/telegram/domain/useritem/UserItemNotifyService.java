package com.podo.helloprice.telegram.domain.useritem;

import com.podo.helloprice.core.domain.item.Item;
import com.podo.helloprice.core.domain.item.ItemRepository;
import com.podo.helloprice.core.domain.user.User;
import com.podo.helloprice.core.domain.user.UserRepository;
import com.podo.helloprice.core.domain.user.UserStatus;
import com.podo.helloprice.core.domain.useritem.UserItemNotify;
import com.podo.helloprice.core.domain.useritem.UserItemNotifyRepository;
import com.podo.helloprice.telegram.domain.item.ItemDto;
import com.podo.helloprice.telegram.domain.item.exception.InvalidItemIdException;
import com.podo.helloprice.telegram.domain.user.UserDto;
import com.podo.helloprice.telegram.domain.user.exception.InvalidUserIdException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class UserItemNotifyService {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final UserItemNotifyRepository userItemNotifyRepository;

    public boolean isExistedNotify(Long userId, Long itemId) {
        final UserItemNotify existedNotify = userItemNotifyRepository.findByUserIdAndItemId(userId, itemId);
        return Objects.nonNull(existedNotify);
    }

    public void addNewNotify(Long userId, Long itemId) {
        final User existedUser = getExistedUser(userId);
        final Item existedItem = getExistedItem(itemId);

        UserItemNotify userItemNotify = new UserItemNotify(existedUser, existedItem);
        userItemNotifyRepository.save(userItemNotify);

        existedUser.addUserItemNotify(userItemNotify);
        existedItem.addUserItemNotify(userItemNotify);
    }

    private User getExistedUser(Long userId) {
        final Optional<User> existedUserOptional = userRepository.findById(userId);

        if (!existedUserOptional.isPresent()) {
            throw new InvalidUserIdException(userId);
        }
        return existedUserOptional.get();
    }

    private Item getExistedItem(Long itemId) {
        final Optional<Item> itemOptional = itemRepository.findById(itemId);
        if (!itemOptional.isPresent()) {
            throw new InvalidItemIdException(itemId);
        }
        return itemOptional.get();
    }


    public void deleteNotifiesByItemId(Long itemId) {
        final List<UserItemNotify> userItemNotifies = userItemNotifyRepository.findByItemId(itemId);

        for (UserItemNotify userItemNotify : userItemNotifies) {
            this.deleteExistedNotify(userItemNotify);
        }
    }

    public void deleteNotifyByUserIdAndItemId(Long userId, Long itemId) {
        final UserItemNotify userItemNotify = userItemNotifyRepository.findByUserIdAndItemId(userId, itemId);
        deleteExistedNotify(userItemNotify);
    }

    private void deleteExistedNotify(UserItemNotify userItemNotify) {
        final User user = userItemNotify.getUser();
        final Item item = userItemNotify.getItem();

        log.info("{}님의 {}({}) 상품 알림을 삭제합니다.", user.getTelegramId(), item.getItemName(), item.getItemCode());

        user.removeUserItemNotify(userItemNotify);
        item.removeUserItemNotify(userItemNotify);

        userItemNotifyRepository.delete(userItemNotify);
    }


    public List<UserDto.detail> findNotifyUsersByItemIdAndUserStatus(Long itemId, UserStatus userStatus) {
        final List<UserItemNotify> existedUserItemNotifies = userItemNotifyRepository.findByItemIdAndUserStatus(itemId, userStatus);

        return existedUserItemNotifies.stream()
                .map(notify -> new UserDto.detail(notify.getUser()))
                .collect(Collectors.toList());
    }

    public List<ItemDto.detail> findNotifyItemsByUserTelegramId(String telegramId) {
        final List<UserItemNotify> existedUserItemNotifies = userItemNotifyRepository.findByUserTelegramId(telegramId);

        return existedUserItemNotifies.stream()
                .map(notify -> new ItemDto.detail(notify.getItem()))
                .collect(Collectors.toList());
    }

    public void updateNotifiedAtByItemId(Long itemId, LocalDateTime notifiedAt) {
        final List<UserItemNotify> existedUserItemNotifies = userItemNotifyRepository.findByItemId(itemId);

        for (UserItemNotify userItemNotify : existedUserItemNotifies) {
            userItemNotify.updateNotifiedAt(notifiedAt);
        }
    }
}
