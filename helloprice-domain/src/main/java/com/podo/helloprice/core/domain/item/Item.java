package com.podo.helloprice.core.domain.item;

import com.podo.helloprice.code.model.ProductAliveStatus;
import com.podo.helloprice.core.domain.BaseEntity;
import com.podo.helloprice.core.domain.item.model.ItemSaleStatus;
import com.podo.helloprice.core.domain.item.model.ItemStatus;
import com.podo.helloprice.core.domain.item.model.ItemUpdateStatus;
import com.podo.helloprice.core.domain.item.vo.CrawledItem;
import com.podo.helloprice.core.domain.useritem.UserItemNotify;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "item")
@Entity
public class Item extends BaseEntity {

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

    private LocalDateTime lastPublishAt;

    private LocalDateTime lastUpdatedAt;

    private Integer deadCount;

    @Enumerated(EnumType.STRING)
    private ProductAliveStatus productAliveStatus;

    @Enumerated(EnumType.STRING)
    private ItemSaleStatus itemSaleStatus;

    @Enumerated(EnumType.STRING)
    private ItemUpdateStatus itemUpdateStatus;

    @OneToMany(mappedBy = "item")
    private List<UserItemNotify> userItemNotifies = new ArrayList<>();

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
        this.lastPublishAt = lastCrawledAt;
        this.itemSaleStatus = itemSaleStatus;
        this.deadCount = 0;
        this.productAliveStatus = ProductAliveStatus.ALIVE;
        this.itemUpdateStatus = ItemUpdateStatus.BE;

    }
    public void updateByCrawledItem(CrawledItem crawledItem) {
        final Integer existPrice = this.itemPrice;
        final Integer crawledPrice = crawledItem.getItemPrice();

        this.itemPrice = crawledPrice;
        this.deadCount = 0;

        this.itemName = crawledItem.getItemName();
        this.itemImage = crawledItem.getItemImage();
        this.lastCrawledAt = crawledItem.getCrawledAt();
        this.itemSaleStatus = crawledItem.getItemSaleStatus();

        switch (crawledItem.getItemSaleStatus()) {
            case UNKNOWN:
            case DISCONTINUE:
            case NOT_SUPPORT:
                this.productAliveStatus = ProductAliveStatus.PAUSE;
                updateItemUpdateInfo(lastCrawledAt);
                break;

            case SALE:
            case EMPTY_AMOUNT:
                if (existPrice.equals(crawledPrice)) {
                    itemUpdateStatus = ItemUpdateStatus.BE;
                } else {
                    itemBeforePrice = existPrice;
                    updateItemUpdateInfo(lastCrawledAt);
                }
            default:
                break;
        }
    }

    public void increaseDeadCount(Integer maxDeadCount, LocalDateTime lastCrawledAt) {
        this.deadCount++;

        if(this.deadCount > maxDeadCount){
            log.info("{}({}) 상품 DEAD_COUNT 초과, DEAD 상태로 변경", this.itemName, this.itemCode);
            this.itemBeforePrice = this.itemPrice;
            this.itemPrice = 0;
            this.productAliveStatus = ProductAliveStatus.DEAD;
            updateItemUpdateInfo(lastCrawledAt);
        }
    }

    private void updateItemUpdateInfo(LocalDateTime lastUpdatedAt) {
        this.itemUpdateStatus = ItemUpdateStatus.UPDATED;
        this.lastUpdatedAt = lastUpdatedAt;
    }

    public void addUserItemNotify(UserItemNotify userItemNotify) {
        this.userItemNotifies.add(userItemNotify);
        this.productAliveStatus = ProductAliveStatus.ALIVE;
    }

    public void removeUserItemNotify(UserItemNotify userItemNotify) {
        this.userItemNotifies.remove(userItemNotify);

        if (!hasAnyNotify() && isAlive()) {
            log.info("{}({}) 상품은, 어떤 사용자에게도 알림이 없습니다", this.itemName, this.itemCode);
            log.info("{}({}) 상품을, 더 이상 갱신하지 않습니다.(중지)", this.itemName, this.itemCode);

            this.productAliveStatus = ProductAliveStatus.PAUSE;
        }
    }

    private boolean hasAnyNotify() {
        return !this.userItemNotifies.isEmpty();
    }

    private boolean isAlive() {
        return this.productAliveStatus.equals(ProductAliveStatus.ALIVE);
    }

    public void notified() {
        this.itemUpdateStatus = ItemUpdateStatus.BE;
    }

    public void updateLastPublishAt(LocalDateTime lastPublishAt){
        this.lastPublishAt = lastPublishAt;
    }
}
