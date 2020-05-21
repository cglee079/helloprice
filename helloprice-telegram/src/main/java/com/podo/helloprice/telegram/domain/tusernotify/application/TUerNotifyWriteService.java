package com.podo.helloprice.telegram.domain.tusernotify.application;

import com.podo.helloprice.telegram.domain.product.model.Product;
import com.podo.helloprice.telegram.domain.productsale.ProductSale;
import com.podo.helloprice.telegram.domain.productsale.ProductSaleRepository;
import com.podo.helloprice.telegram.domain.productsale.application.helper.ProductSaleReadHelper;
import com.podo.helloprice.telegram.domain.tuser.application.helper.TUserReadServiceHelper;
import com.podo.helloprice.telegram.domain.tuser.model.TUser;
import com.podo.helloprice.telegram.domain.tuser.repository.TUserRepository;
import com.podo.helloprice.telegram.domain.tusernotify.TUserNotify;
import com.podo.helloprice.telegram.domain.tusernotify.repository.TUserNotifyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class TUerNotifyWriteService {

    private final TUserRepository userRepository;
    private final ProductSaleRepository productSaleRepository;
    private final TUserNotifyRepository userProductNotifyRepository;

    public void insertNewNotify(Long userId, Long productSaleId) {
        final TUser tUser = TUserReadServiceHelper.findUserById(userRepository, userId);
        final ProductSale productSale = ProductSaleReadHelper.findProductSaleById(productSaleRepository, productSaleId);

        final TUserNotify tUserNotify = new TUserNotify(tUser, productSale);
        userProductNotifyRepository.save(tUserNotify);

        tUser.addUserProductNotify(tUserNotify);
        productSale.addUserProductNotify(tUserNotify);
    }

    public void deleteNotifyByUserIdAndProductId(Long userId, Long productSaleId) {
        final Optional<TUserNotify> tUserNotify = userProductNotifyRepository.findByTUserIdAndProductSaleId(userId, productSaleId);
        tUserNotify.ifPresent(this::deleteExistedNotify);
    }

    private void deleteExistedNotify(TUserNotify tUserNotify) {
        final TUser tUser = tUserNotify.getTUser();
        final ProductSale productSale = tUserNotify.getProductSale();
        final Product product = productSale.getProduct();

        log.debug("APP :: {}님의 {}({}) 상품 알림을 삭제합니다.", tUser.getTelegramId(), product.getProductName(), product.getProductCode());

        tUser.removeUserProductNotify(tUserNotify);
        productSale.removeUserProductNotify(tUserNotify);

        userProductNotifyRepository.delete(tUserNotify);
    }


}
