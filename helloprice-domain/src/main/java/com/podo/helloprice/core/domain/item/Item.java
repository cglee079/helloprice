package com.podo.helloprice.core.domain.item;

import com.podo.helloprice.core.domain.UpdatableBaseEntity;
import com.podo.helloprice.core.domain.useritem.UserItemNotify;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Slf4j
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

    private String itemDesc;

    private String itemUrl;

    private String itemImage;

    private Integer itemPrice;

    private Integer itemBeforePrice;

    private LocalDateTime lastCrawledAt;

    private LocalDateTime lastUpdatedAt;

    private Integer deadCount;

    @Enumerated(EnumType.STRING)
    private ItemStatus itemStatus;

    @Enumerated(EnumType.STRING)
    private ItemSaleStatus itemSaleStatus;

    @Enumerated(EnumType.STRING)
    private ItemUpdateStatus itemUpdateStatus;

    @OneToMany(mappedBy = "item")
    List<UserItemNotify> userItemNotifies;

    @Builder
    public Item(String itemCode,
                String itemName, String itemDesc,
                String itemUrl, String itemImage,
                Integer itemPrice,
                ItemSaleStatus itemSaleStatus,
                LocalDateTime lastCrawledAt) {

        this.itemCode = itemCode;
        this.itemUrl = itemUrl;
        this.itemName = itemName;
        this.itemDesc = itemDesc;
        this.itemImage = itemImage;
        this.itemPrice = itemPrice;
        this.itemBeforePrice = itemPrice;
        this.lastCrawledAt = lastCrawledAt;
        this.lastUpdatedAt = lastCrawledAt;
        this.itemSaleStatus = itemSaleStatus;
        this.deadCount = 0;
        this.itemStatus = ItemStatus.ALIVE;
        this.itemUpdateStatus = ItemUpdateStatus.BE;

    }

    public void updateByCrawledItem(CrawledItemVo crawledItemVo, LocalDateTime lastPoolAt) {
        final Integer existPrice = this.itemPrice;

        this.itemName = crawledItemVo.getItemName();
        this.itemImage = crawledItemVo.getItemImage();
        this.itemPrice = crawledItemVo.getItemPrice();
        this.lastCrawledAt = lastPoolAt;
        this.itemSaleStatus = crawledItemVo.getItemSaleStatus();

        this.deadCount = 0;

        switch (itemSaleStatus) {
            case UNKNOWN:
            case DISCONTINUE:
            case NOT_SUPPORT:
                this.itemStatus = ItemStatus.PAUSE;
                this.itemUpdateStatus = ItemUpdateStatus.UPDATED;
                break;

            case SALE:
            case EMPTY_AMOUNT:
                if (Objects.equals(existPrice, this.itemPrice)) {
                    itemUpdateStatus = ItemUpdateStatus.BE;
                } else {
                    itemBeforePrice = existPrice;
                    itemUpdateStatus = ItemUpdateStatus.UPDATED;
                    lastUpdatedAt = lastPoolAt;
                }
            default:
                break;
        }
    }

    public void increaseDeadCount() {
        this.deadCount++;
    }

    public void died(LocalDateTime lastPoolAt) {
        this.itemBeforePrice = this.itemPrice;
        this.itemPrice = 0;
        this.itemUpdateStatus = ItemUpdateStatus.UPDATED;
        this.lastUpdatedAt = lastPoolAt;
        this.itemStatus = ItemStatus.DEAD;
    }

    public void addUserItemNotify(UserItemNotify userItemNotify) {
        this.userItemNotifies.add(userItemNotify);
        this.itemStatus = ItemStatus.ALIVE;
    }

    public void removeUserItemNotify(UserItemNotify userItemNotify) {
        this.userItemNotifies.remove(userItemNotify);

        if (this.userItemNotifies.isEmpty() && this.itemStatus.equals(ItemStatus.ALIVE)) {
            log.info("{}({}) 상품은, 어떤 사용자에게도 알림이 없습니다", this.itemName, this.itemCode);
            log.info("{}({}) 상품을, 더 이상 갱신하지 않습니다.(중지)", this.itemName, this.itemCode);

            this.itemStatus = ItemStatus.PAUSE;
        }
    }

    public void notified() {
        this.itemUpdateStatus = ItemUpdateStatus.BE;
    }
}
