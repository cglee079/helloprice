//package com.podo.helloprice.telegram.job.notifier;
//
//import com.podo.helloprice.code.model.PriceUpdateStatus;
//import com.podo.helloprice.code.model.ProductAliveStatus;
//import com.podo.helloprice.telegram.domain.notifylog.application.NotifyLogService;
//import com.podo.helloprice.telegram.domain.notifylog.dto.NotifyLogDto;
//import com.podo.helloprice.telegram.domain.product.application.ProductReadService;
//import com.podo.helloprice.telegram.domain.product.application.ProductWriteService;
//import com.podo.helloprice.telegram.domain.product.dto.ProductDetailDto;
//import com.podo.helloprice.telegram.domain.user.dto.UserDetailDto;
//import com.podo.helloprice.telegram.domain.user.model.UserStatus;
//import com.podo.helloprice.telegram.domain.userproduct.UserProductNotifyService;
//import com.podo.helloprice.telegram.job.Worker;
//import com.podo.helloprice.telegram.job.notifier.message.NotifyContents;
//import com.podo.helloprice.telegram.job.notifier.message.NotifyTitle;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import java.math.BigDecimal;
//import java.text.DecimalFormat;
//import java.time.LocalDateTime;
//import java.util.List;
//
//import static com.podo.helloprice.core.util.CalculateUtil.getChangePercent;
//import static java.math.BigDecimal.ONE;
//import static java.math.BigDecimal.ZERO;
//import static java.util.stream.Collectors.toList;
//
//@Slf4j
//@RequiredArgsConstructor
//@Component
//public class ProductAliveStatusNotifyWorker implements Worker {
//
//    @Value("${product.max_dead_count}")
//    private Integer maxDeadCount;
//
//    @Value("${product.max_unknown_count}")
//    private Integer maxUnknownCount;
//
//    private int deadCount = 0;
//    private int unknownCount = 0;
//
//    private final GlobalNotifier globalNotifier;
//    private final ProductReadService productReadService;
//    private final ProductWriteService productWriteService;
//    private final NotifyLogService notifyLogService;
//    private final UserProductNotifyService userProductNotifyService;
//
//    @Override
//    public void doIt(LocalDateTime now) {
//        log.debug("NOTIFY :: 상품 업데이트 알림 WORKER, 상품 상태체크를 시작합니다");
//    }
//
//
//    private void handleByProductSaleStatus(LocalDateTime notifyAt, ProductDetailDto product) {
//        switch (product.getSaleStatus()) {
//            case DISCONTINUE:
//                handleDiscontinueProduct(product);
//                break;
//            case UNKNOWN:
//                handleUnknownProduct(product);
//                break;
//            case EMPTY_AMOUNT:
//                handleEmptyAmount(product, notifyAt);
//                break;
//            case NOT_SUPPORT:
//                handleNotSupport(product);
//                break;
//            case SALE:
//                handleSale(product, notifyAt);
//                break;
//        }
//    }
//
//    private void handleSale(ProductDetailDto product, LocalDateTime notifyAt) {
//
//        if (product.getBeforePrice() == 0) {
//            handleResale(product, notifyAt);
//            return;
//        }
//
//        log.debug("NOTIFY :: {}({}) 상품의 최저가가 갱신되었습니다.", product.getProductName(), product.getProductCode());
//        final BigDecimal changePercent = getChangePercent(product.getPrice(), product.getBeforePrice());
//
//        if ((changePercent.abs().compareTo(ONE) > 0) && (changePercent.compareTo(ZERO) < 0)) {
//            logging(product);
//
//            if (changePercent.compareTo(BigDecimal.valueOf(-30)) < 0) { //이스터에그
//                globalNotifier.notifyAdmin(NotifyTitle.notifyProductSale(product), product.getImageUrl(), NotifyContents.notifyProductSale(product));
//            }
//
//            notify(product, NotifyTitle.notifyProductSale(product), NotifyContents.notifyProductSale(product), notifyAt);
//            return;
//        }
//
//        log.debug("NOTIFY :: {}({}) 상품의 가격변화율('{}%')이 적합하지 않아 알림을 전송하지 않습니다", product.getProductName(), product.getProductCode(), new DecimalFormat("#.##").format(changePercent));
//    }
//
//    private void handleResale(ProductDetailDto product, LocalDateTime notifyAt) {
//        logging(product);
//        log.debug("NOTIFY :: {}({}) 상품은 이전가가 0원이고, 다시 판매를 시작했습니다", product.getProductName(), product.getProductCode());
//        notify(product, NotifyTitle.notifyProductReSale(product), NotifyContents.notifyProductResale(product), notifyAt);
//    }
//
//
//    private void handleEmptyAmount(ProductDetailDto product, LocalDateTime notifyAt) {
//        logging(product);
//        log.debug("NOTIFY :: {}({}) 상품은 재고없음 상태로 변경되었습니다.", product.getProductName(), product.getProductCode());
//        notify(product, NotifyTitle.notifyProductEmptyAccount(product), NotifyContents.notifyProductEmptyAccount(product), notifyAt);
//    }
//
//    private void handleNotSupport(ProductDetailDto product) {
//        logging(product);
//        log.debug("NOTIFY :: {}({}) 상품은 가격격비교중지 상태로 변경되었습니다.", product.getProductName(), product.getProductCode());
//        notifyAndRemoveNotify(product, NotifyTitle.notifyProductNotSupport(product), NotifyContents.notifyProductNotSupport(product));
//    }
//
//    private void handleUnknownProduct(ProductDetailDto product) {
//        logging(product);
//        log.debug("NOTIFY :: {}({}) 상품은 알 수 없는 상태로 변경되었습니다.", product.getProductName(), product.getProductCode());
//        increaseUnknownCount();
//        notifyAndRemoveNotify(product, NotifyTitle.notifyProductUnknown(product), NotifyContents.notifyProductUnknown(product));
//    }
//
//    private void handleDiscontinueProduct(ProductDetailDto product) {
//        logging(product);
//        log.debug("NOTIFY :: {}({}) 상품은 단종 상태로 변경되었습니다.", product.getProductName(), product.getProductCode());
//
//        notifyAndRemoveNotify(product, NotifyTitle.notifyProductDiscontinued(product), NotifyContents.notifyProductDiscontinued(product));
//    }
//
//    private void increaseUnknownCount() {
//        this.unknownCount++;
//
//        if (unknownCount >= maxUnknownCount) {
//            log.debug("NOTIFY :: {} 이상 상품 상태를 확인 할 수 없습니다", unknownCount);
//            globalNotifier.notifyAdmin(NotifyTitle.notifyTooManyUnknown(unknownCount), NotifyContents.notifyTooManyUnknown(unknownCount));
//
//            unknownCount = 0;
//        }
//    }
//
//    private void handleProductAliveStatusDead() {
//        for (ProductDetailDto product : products) {
//            logging(product);
//
//            increaseDeadCount();
//
//            log.debug("{}({}) 상품은 페이지를 확인 할 수 없는 상태로 변경되었습니다.", product.getProductName(), product.getProductCode());
//
//            notifyAndRemoveNotify(product, NotifyTitle.notifyProductDead(product), NotifyContents.notifyProductDead(product));
//        }
//    }
//
//    private void increaseDeadCount() {
//        this.deadCount++;
//
//        if (deadCount >= maxDeadCount) {
//            log.debug("NOTIFY ::: {} 이상 상품페이지를 확인 할 수 없습니다", deadCount);
//            globalNotifier.notifyAdmin(NotifyTitle.notifyTooManyDead(unknownCount), NotifyContents.notifyTooManyDead(unknownCount));
//            deadCount = 0;
//        }
//    }
//
//    private void notify(ProductDetailDto product, String title, String contents, LocalDateTime notifiedAt) {
//        globalNotifier.notifyUsers(getNotifyUsersByProductId(product.getId()), title, product.getImageUrl(), contents);
//        userProductNotifyService.updateNotifiedAtByProductId(product.getId(), notifiedAt);
//    }
//
//    private void notifyAndRemoveNotify(ProductDetailDto product, String title, String contents) {
//        globalNotifier.notifyUsers(getNotifyUsersByProductId(product.getId()), title, product.getImageUrl(), contents);
//        userProductNotifyService.deleteNotifiesByProductId(product.getId());
//    }
//
//    private List<NotifyUserVo> getNotifyUsersByProductId(Long productId) {
//        final List<UserDetailDto> users = userProductNotifyService.findNotifyUsersByProductIdAndUserStatus(productId, UserStatus.ALIVE);
//        return users.stream()
//                .map(user -> new NotifyUserVo(user.getUsername(), user.getEmail(), user.getTelegramId()))
//                .collect(toList());
//    }
//
//    private void logging(ProductDetailDto product) {
//        notifyLogService.insertNewNotifyLog(NotifyLogDto.createByProduct(product));
//    }
//
//}
