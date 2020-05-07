package com.podo.helloprice.telegram.domain.userproduct.application;

import com.podo.helloprice.core.enums.SaleType;
import com.podo.helloprice.telegram.domain.product.model.Product;
import com.podo.helloprice.telegram.domain.productsale.ProductSale;
import com.podo.helloprice.telegram.domain.productsale.ProductSaleRepository;
import com.podo.helloprice.telegram.domain.productsale.application.helper.ProductSaleReadHelper;
import com.podo.helloprice.telegram.domain.user.application.helper.UserReadServiceHelper;
import com.podo.helloprice.telegram.domain.user.model.User;
import com.podo.helloprice.telegram.domain.user.repository.UserRepository;
import com.podo.helloprice.telegram.domain.userproduct.UserProductSaleNotify;
import com.podo.helloprice.telegram.domain.userproduct.repository.UserProductNotifyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class UserProductSaleNotifyWriteService {

    private final UserRepository userRepository;
    private final ProductSaleRepository productSaleRepository;
    private final UserProductNotifyRepository userProductNotifyRepository;

    public void insertNewNotify(Long userId, Long productSaleId) {
        final User user = UserReadServiceHelper.findUserById(userRepository, userId);
        final ProductSale productSale = ProductSaleReadHelper.findProductSaleById(productSaleRepository, productSaleId);

        final UserProductSaleNotify userProductSaleNotify = new UserProductSaleNotify(user, productSale);
        userProductNotifyRepository.save(userProductSaleNotify);

        user.addUserProductNotify(userProductSaleNotify);
        productSale.addUserProductNotify(userProductSaleNotify);
    }

    public void deleteNotifyByUserIdAndProductId(Long userId, Long productSaleId) {
        final UserProductSaleNotify userProductSaleNotify = userProductNotifyRepository.findByUserIdAndProductSaleId(userId, productSaleId);
        deleteExistedNotify(userProductSaleNotify);
    }

    private void deleteExistedNotify(UserProductSaleNotify userProductSaleNotify) {
        final User user = userProductSaleNotify.getUser();
        final ProductSale productSale = userProductSaleNotify.getProductSale();
        final Product product = productSale.getProduct();

        log.debug("APP :: {}님의 {}({}) 상품 알림을 삭제합니다.", user.getTelegramId(), product.getProductName(), product.getProductCode());

        user.removeUserProductNotify(userProductSaleNotify);
        productSale.removeUserProductNotify(userProductSaleNotify);

        userProductNotifyRepository.delete(userProductSaleNotify);
    }


}
