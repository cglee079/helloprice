package com.podo.itemwatcher.core.domain.itemuser;

import com.podo.itemwatcher.core.domain.item.Item;
import com.podo.itemwatcher.core.domain.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "item_user")
@Entity
public class ItemUserRelation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    @ManyToOne
    @JoinColumn(name="item_id")
    private Item item;


    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

}
