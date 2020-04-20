package com.podo.helloprice.telegram.domain.userproduct.application;

import com.podo.helloprice.core.model.PriceType;
import com.podo.helloprice.telegram.domain.product.application.helper.ProductReadHelper;
import com.podo.helloprice.telegram.domain.product.model.Product;
import com.podo.helloprice.telegram.domain.product.repository.ProductRepository;
import com.podo.helloprice.telegram.domain.user.application.helper.UserReadServiceHelper;
import com.podo.helloprice.telegram.domain.user.model.User;
import com.podo.helloprice.telegram.domain.user.repository.UserRepository;
import com.podo.helloprice.telegram.domain.userproduct.UserProductNotify;
import com.podo.helloprice.telegram.domain.userproduct.repository.UserProductNotifyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class UserProductNotifyWriteService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final UserProductNotifyRepository userProductNotifyRepository;

    public void insertNewNotify(Long userId, Long productId, PriceType priceType) {
        final User existedUser = UserReadServiceHelper.findUserById(userRepository, userId);
        final Product existedProduct = ProductReadHelper.findProductById(productRepository, productId);

        UserProductNotify userProductNotify = new UserProductNotify(existedUser, existedProduct, priceType);
        userProductNotifyRepository.save(userProductNotify);

        existedUser.addUserProductNotify(userProductNotify);
        existedProduct.addUserProductNotify(userProductNotify);
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


}
