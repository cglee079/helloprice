package com.podo.sadream.telegram.domain.useritem;

import com.podo.sadream.core.domain.item.Item;
import com.podo.sadream.core.domain.item.ItemRepository;
import com.podo.sadream.core.domain.user.User;
import com.podo.sadream.core.domain.user.UserRepository;
import com.podo.sadream.core.domain.useritem.UserItemNotify;
import com.podo.sadream.core.domain.useritem.UserItemNotifyRepository;
import com.podo.sadream.telegram.domain.item.ItemDto;
import com.podo.sadream.telegram.domain.user.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class UserItemNotifyService {

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

    public void deleteNotify(Long userId, Long itemId) {

        UserItemNotify userItemNotify = userItemNotifyRepository.findByUserIdAndItemId(userId, itemId);

        User user = userItemNotify.getUser();
        Item item = userItemNotify.getItem();

        log.info("{}님의 {}({}) 상품 알림을 삭제합니다.", user.getTelegramId(), item.getItemName(), item.getItemCode());

        user.deleteUserItemNotify(userItemNotify);
        item.deleteUserItemNotify(userItemNotify);

        userItemNotifyRepository.delete(userItemNotify);

        if (item.getUserItemNotifies().isEmpty()) {
            log.info("{}({}) 상품은, 어떤 사용자에게도 알림이 없습니다", item.getItemName(), item.getItemCode());
            log.info("{}({}) 상품을, 더 이상 갱신하지 않습니다.(삭제)", item.getItemName(), item.getItemCode());
            itemRepository.delete(item);
        }
    }

    public List<UserDto.detail> findNotifyUsersByItem(Long itemId) {
        List<UserItemNotify> userItemNotifies = userItemNotifyRepository.findByItemId(itemId);

        return userItemNotifies.stream()
                .map(notify -> new UserDto.detail(notify.getUser()))
                .collect(Collectors.toList());
    }

    public List<ItemDto.detail> findNotifyItemsByUserTelegramId(String telegramId) {
        User user = userRepository.findByTelegramId(telegramId);

        List<UserItemNotify> userItemNotifies = userItemNotifyRepository.findByUserId(user.getId());

        return userItemNotifies.stream()
                .map(notify -> new ItemDto.detail(notify.getItem()))
                .collect(Collectors.toList());
    }
}
