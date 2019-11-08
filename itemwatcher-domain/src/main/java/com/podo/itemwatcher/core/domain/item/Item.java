package com.podo.itemwatcher.core.domain.item;

import com.podo.itemwatcher.core.domain.UpdatableBaseEntity;
import com.podo.itemwatcher.core.domain.itemuser.ItemUserRelation;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

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
    List<ItemUserRelation> itemUserRelations;

    @Builder
    public Item(String itemCode, String itemName, Integer itemPrice, Integer itemBeforePrice, ItemStatus itemStatus) {
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemBeforePrice = itemBeforePrice;
        this.itemStatus = itemStatus;
    }

    public void updateInfo(ItemInfoVo itemInfoVo, LocalDateTime lastPoolAt) {
        itemBeforePrice = itemPrice;
        this.itemImage = itemInfoVo.getItemImage();
        this.itemName = itemInfoVo.getItemName();
        this.itemPrice = itemInfoVo.getItemPrice();
        this.lastPoolAt = lastPoolAt;
        this.deadCount = 0;

        if (!itemBeforePrice.equals(itemPrice)) {
            itemStatus = ItemStatus.UPDATED;
        }
    }

    public void increaseDeadCount() {
        this.deadCount++;
    }

    public void died() {
        this.itemStatus = ItemStatus.DEAD;
    }
}