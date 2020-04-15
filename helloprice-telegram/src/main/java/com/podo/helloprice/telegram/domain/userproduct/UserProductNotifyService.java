package com.podo.helloprice.telegram.domain.userproduct;

import com.podo.helloprice.telegram.domain.product.dto.ProductDetailDto;
import com.podo.helloprice.telegram.domain.product.model.PriceType;
import com.podo.helloprice.telegram.domain.product.model.Product;
import com.podo.helloprice.telegram.domain.product.repository.ProductRepository;
import com.podo.helloprice.telegram.domain.user.dto.UserDetailDto;
import com.podo.helloprice.telegram.domain.user.model.User;
import com.podo.helloprice.telegram.domain.user.model.UserStatus;
import com.podo.helloprice.telegram.domain.user.repository.UserRepository;
import com.podo.helloprice.telegram.domain.userproduct.repository.UserProductNotifyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.podo.helloprice.telegram.domain.product.application.helper.ProductReadHelper.findProductById;
import static com.podo.helloprice.telegram.domain.user.application.helper.UserReadServiceHelper.findUserById;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class UserProductNotifyService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final UserProductNotifyRepository userProductNotifyRepository;

    public boolean isExistedNotify(Long userId, Long productId, PriceType priceType) {
        final UserProductNotify existedNotify = userProductNotifyRepository.findByUserIdAndProductIdAndPriceType(userId, productId, priceType);
        return Objects.nonNull(existedNotify);
    }

    public void addNewNotify(Long userId, Long productId, PriceType priceType) {
        final User existedUser = findUserById(userRepository, userId);
        final Product existedProduct = findProductById(productRepository, productId);

        UserProductNotify userProductNotify = new UserProductNotify(existedUser, existedProduct, priceType);
        userProductNotifyRepository.save(userProductNotify);

        existedUser.addUserProductNotify(userProductNotify);
        existedProduct.addUserProductNotify(userProductNotify);
    }

    public void deleteNotifiesByProductId(Long productId) {
        final List<UserProductNotify> userProductNotifies = userProductNotifyRepository.findByProductId(productId);

        for (UserProductNotify userProductNotify : userProductNotifies) {
            this.deleteExistedNotify(userProductNotify);
        }
    }

    public void deleteNotifyByUserIdAndProductId(Long userId, Long productId, PriceType priceType) {
        final UserProductNotify userProductNotify = userProductNotifyRepository.findByUserIdAndProductIdAndPriceType(userId, productId, priceType);
        deleteExistedNotify(userProductNotify);
    }

    private void deleteExistedNotify(UserProductNotify userProductNotify) {
        final User user = userProductNotify.getUser();
        final Product product = userProductNotify.getProduct();

        log.debug("APP :: {}님의 {}({}) 상품 알림을 삭제합니다.", user.getTelegramId(), product.getProductName(), product.getProductCode());

        user.removeUserProductNotify(userProductNotify);
        product.removeUserProductNotify(userProductNotify);

        userProductNotifyRepository.delete(userProductNotify);
    }


    public List<UserDetailDto> findNotifyUsersByProductIdAndUserStatus(Long productId, UserStatus userStatus) {
        final List<UserProductNotify> existedUserProductNotifies = userProductNotifyRepository.findByProductIdAndUserStatus(productId, userStatus);

        return existedUserProductNotifies.stream()
                .map(notify -> new UserDetailDto(notify.getUser()))
                .collect(Collectors.toList());
    }

    public List<ProductDetailDto> findNotifyProductsByUserTelegramId(String telegramId) {
        final List<UserProductNotify> existedUserProductNotifies = userProductNotifyRepository.findByUserTelegramId(telegramId);

        return existedUserProductNotifies.stream()
                .map(notify -> new ProductDetailDto(notify.getProduct(), notify.getPriceType()))
                .collect(Collectors.toList());
    }

    public void updateNotifiedAtByProductId(Long productId, LocalDateTime notifiedAt) {
        for (UserProductNotify userProductNotify : userProductNotifyRepository.findByProductId(productId)) {
            userProductNotify.updateNotifiedAt(notifiedAt);
        }
    }
}
