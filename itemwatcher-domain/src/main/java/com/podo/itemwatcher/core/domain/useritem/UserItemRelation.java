package com.podo.itemwatcher.core.domain.useritem;

import com.podo.itemwatcher.core.domain.item.Item;
import com.podo.itemwatcher.core.domain.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_item")
@Entity
public class UserItemRelation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;


    public UserItemRelation(User user, Item item) {
        this.user = user;
        this.item = item;
    }
}
