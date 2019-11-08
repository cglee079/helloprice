package com.podo.itemwatcher.core.domain.item;

import com.podo.itemwatcher.core.domain.UpdatableBaseEntity;
import com.podo.itemwatcher.core.domain.useritem.UserItemRelation;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "item")
@Entity(name = "Item")
public class Item extends UpdatableBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String itemCode;

    private String itemName;

    private String itemImage;

    private Integer itemPrice;

    private Integer itemBeforePrice;

    private LocalDateTime lastPoolAt;

    private Integer deadCount;

    @Enumerated(EnumType.STRING)
    private ItemStatus itemStatus;

    @OneToMany(mappedBy = "item")
    List<UserItemRelation> userItemRelations;

    @Builder
    public Item(String itemCode, String itemName,
                String itemImage,
                Integer itemPrice, Integer itemBeforePrice,
                ItemStatus itemStatus, LocalDateTime lastPoolAt) {

        this.itemCode = itemCode;
        this.itemName = itemName;
        this.itemImage = itemImage;
        this.itemPrice = itemPrice;
        this.itemBeforePrice = itemBeforePrice;
        this.itemStatus = itemStatus;
        this.lastPoolAt = lastPoolAt;
    }

    public void updateInfo(String itemName, String itemImage, Integer itemPrice, LocalDateTime lastPoolAt) {
        itemBeforePrice = itemPrice;
        this.itemName = itemName;
        this.itemImage = itemImage;
        this.itemPrice = itemPrice;
        this.lastPoolAt = lastPoolAt;
        this.deadCount = 0;

        if (!Objects.equals(itemBeforePrice, itemPrice)) {
            itemStatus = ItemStatus.UPDATED;
        }
    }

    public void increaseDeadCount() {
        this.deadCount++;
    }

    public void died() {
        this.itemStatus = ItemStatus.DEAD;
    }

    public void addItemUserRelation(UserItemRelation userItemRelation) {
        this.userItemRelations.add(userItemRelation);
    }
}