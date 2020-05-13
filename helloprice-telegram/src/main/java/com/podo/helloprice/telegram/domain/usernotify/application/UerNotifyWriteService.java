package com.podo.helloprice.telegram.domain.usernotify.application;

import com.podo.helloprice.telegram.domain.product.model.Product;
import com.podo.helloprice.telegram.domain.productsale.ProductSale;
import com.podo.helloprice.telegram.domain.productsale.ProductSaleRepository;
import com.podo.helloprice.telegram.domain.productsale.application.helper.ProductSaleReadHelper;
import com.podo.helloprice.telegram.domain.user.application.helper.UserReadServiceHelper;
import com.podo.helloprice.telegram.domain.user.model.User;
import com.podo.helloprice.telegram.domain.user.repository.UserRepository;
import com.podo.helloprice.telegram.domain.usernotify.UserNotify;
import com.podo.helloprice.telegram.domain.usernotify.repository.UserProductNotifyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class UerNotifyWriteService {

    private final UserRepository userRepository;
    private final ProductSaleRepository productSaleRepository;
    private final UserProductNotifyRepository userProductNotifyRepository;

    public void insertNewNotify(Long userId, Long productSaleId) {
        final User user = UserReadServiceHelper.findUserById(userRepository, userId);
        final ProductSale productSale = ProductSaleReadHelper.findProductSaleById(productSaleRepository, productSaleId);

        final UserNotify userNotify = new UserNotify(user, productSale);
        userProductNotifyRepository.save(userNotify);

        user.addUserProductNotify(userNotify);
        productSale.addUserProductNotify(userNotify);
    }

    public void deleteNotifyByUserIdAndProductId(Long userId, Long productSaleId) {
        final UserNotify userNotify = userProductNotifyRepository.findByUserIdAndProductSaleId(userId, productSaleId);
        deleteExistedNotify(userNotify);
    }

    private void deleteExistedNotify(UserNotify userNotify) {
        final User user = userNotify.getUser();
        final ProductSale productSale = userNotify.getProductSale();
        final Product product = productSale.getProduct();

        log.debug("APP :: {}님의 {}({}) 상품 알림을 삭제합니다.", user.getTelegramId(), product.getProductName(), product.getProductCode());

        user.removeUserProductNotify(userNotify);
        productSale.removeUserProductNotify(userNotify);

        userProductNotifyRepository.delete(userNotify);
    }


}
