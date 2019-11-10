package com.podo.sadream.core.domain.useritem;

import com.podo.sadream.core.domain.BaseEntity;
import com.podo.sadream.core.domain.item.Item;
import com.podo.sadream.core.domain.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_item_notify")
@Entity
public class UserItemNotify extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;


    public UserItemNotify(User user, Item item) {
        this.user = user;
        this.item = item;
    }
}
