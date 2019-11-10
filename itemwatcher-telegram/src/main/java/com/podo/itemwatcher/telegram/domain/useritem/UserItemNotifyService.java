package com.podo.itemwatcher.telegram.domain.useritem;

import com.podo.itemwatcher.core.domain.item.Item;
import com.podo.itemwatcher.core.domain.item.ItemRepository;
import com.podo.itemwatcher.core.domain.user.User;
import com.podo.itemwatcher.core.domain.user.UserRepository;
import com.podo.itemwatcher.core.domain.useritem.UserItemNotify;
import com.podo.itemwatcher.core.domain.useritem.UserItemNotifyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        userItemNotify.getUser().deleteUserItemNotify(userItemNotify);
        userItemNotify.getItem().deleteUserItemNotify(userItemNotify);

        userItemNotifyRepository.delete(userItemNotify);
    }
}
