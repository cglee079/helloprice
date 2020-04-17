package com.podo.helloprice.notify.analysis.notify.generator;

import com.podo.helloprice.core.model.ProductUpdateStatus;
import com.podo.helloprice.notify.analysis.notify.vo.TelegramNotify;

import java.util.List;

public interface TelegramNotifyGenerator {

    ProductUpdateStatus getProductUpdateStatus();

    List<TelegramNotify> generate(Long productId);

}
