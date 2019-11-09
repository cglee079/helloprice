package com.podo.itemwatcher.telegram.domain.user;

import com.podo.itemwatcher.core.domain.item.Item;
import com.podo.itemwatcher.core.domain.item.ItemRepository;
import com.podo.itemwatcher.core.domain.useritem.UserItemRelation;
import com.podo.itemwatcher.core.domain.user.MenuStatus;
import com.podo.itemwatcher.core.domain.user.User;
import com.podo.itemwatcher.core.domain.user.UserRepository;
import com.podo.itemwatcher.core.domain.useritem.UserItemRelationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final UserItemRelationRepository userItemRelationRepository;

    public UserDto.detail findByTelegramId(Integer telegramId) {
        final User user = userRepository.findByTelegramId(telegramId + "");

        if (Objects.isNull(user)) {
            return null;
        }

        return new UserDto.detail(user);
    }

    public void insert(UserDto.insert userInsert) {
        userRepository.save(userInsert.toEntity());
    }

    public void updateMenuStatus(Integer telegramId, MenuStatus menuStatus) {
        final User user = userRepository.findByTelegramId(telegramId + "");
        user.updateMenuStatus(menuStatus);
    }

    public boolean hasNotifyItem(Integer telegramId, Long itemId) {
        User user = userRepository.findByTelegramId(telegramId + "");

        for (UserItemRelation userItemRelation : user.getUserItemRelations()) {
            if (userItemRelation.getItem().getId().equals(itemId)) {
                return true;
            }
        }

        return false;
    }

    public void addNotifyItem(Integer telegramId, Long itemId) {
        User user = userRepository.findByTelegramId(telegramId + "");
        Item item = itemRepository.findById(itemId).get();

        UserItemRelation userItemRelation = new UserItemRelation(user, item);
        userItemRelationRepository.save(userItemRelation);

        user.addItemUserRelation(userItemRelation);
        item.addItemUserRelation(userItemRelation);
    }
}
