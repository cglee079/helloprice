package com.podo.sadream.core.domain.item;

import com.podo.sadream.core.domain.UpdatableBaseEntity;
import com.podo.sadream.core.domain.useritem.UserItemNotify;
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
@Entity
public class Item extends UpdatableBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String itemCode;

    private String itemName;

    private String itemUrl;

    private String itemImage;

    private Integer itemPrice;

    private Integer itemBeforePrice;

    private LocalDateTime lastPoolAt;

    private LocalDateTime lastUpdatedAt;

    private Integer deadCount;

    @Enumerated(EnumType.STRING)
    private ItemStatus itemStatus;

    @Enumerated(EnumType.STRING)
    private ItemSaleStatus itemSaleStatus;

    @OneToMany(mappedBy = "item")
    List<UserItemNotify> userItemNotifies;

    @Builder
    public Item(String itemCode, String itemName,
                String itemUrl, String itemImage,
                Integer itemPrice, ItemSaleStatus itemSaleStatus,
                LocalDateTime lastPoolAt) {

        this.itemCode = itemCode;
        this.itemUrl = itemUrl;
        this.itemName = itemName;
        this.itemImage = itemImage;
        this.itemPrice = itemPrice;
        this.itemBeforePrice = itemPrice;
        this.lastPoolAt = lastPoolAt;
        this.lastUpdatedAt = lastPoolAt;
        this.deadCount = 0;
        this.itemStatus = ItemStatus.BE;
        this.itemSaleStatus = itemSaleStatus;

    }

    public void updateInfo(ItemInfoVo itemInfoVo, LocalDateTime lastPoolAt) {
        itemBeforePrice = this.itemPrice;

        this.itemName = itemInfoVo.getItemName();
        this.itemImage = itemInfoVo.getItemImage();
        this.itemPrice = itemInfoVo.getItemPrice();
        this.lastPoolAt = lastPoolAt;
        this.itemSaleStatus = itemInfoVo.getItemSaleStatus();
        this.deadCount = 0;

        if (Objects.equals(this.itemBeforePrice, this.itemPrice)) {
            itemStatus = ItemStatus.BE;
        } else {
            itemStatus = ItemStatus.UPDATED;
            lastUpdatedAt = lastPoolAt;
        }

    }

    public void increaseDeadCount() {
        this.deadCount++;
    }

    public void died() {
        this.itemStatus = ItemStatus.DEAD;
    }

    public void addUserItemNotify(UserItemNotify userItemNotify) {
        this.userItemNotifies.add(userItemNotify);
    }

    public void deleteUserItemNotify(UserItemNotify userItemNotify) {
        this.userItemNotifies.remove(userItemNotify);
    }

    public void notifiedUpdate() {
        this.itemStatus = ItemStatus.BE;
    }
}