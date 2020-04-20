package com.podo.helloprice.telegram.app.menu;

import com.podo.helloprice.telegram.app.menu.email.delete.EmailDeleteKeyboard;
import com.podo.helloprice.telegram.app.menu.home.HomeKeyboard;
import com.podo.helloprice.telegram.app.menu.product.delete.ProductDeleteKeyboard;
import com.podo.helloprice.telegram.app.menu.product.searchselect.ProductSearchSelectKeyboard;
import com.podo.helloprice.telegram.app.menu.product.typeselect.ProductTypeSelectKeyboard;
import lombok.experimental.UtilityClass;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;

import java.util.List;

@UtilityClass
public class Keyboard {

    public static ReplyKeyboardRemove TYPING_KEYBOARD = new ReplyKeyboardRemove();

//    public static ReplyKeyboard getHomeKeyboard(List<String> productCommands) {
//        return new HomeKeyboard(productCommands);
//    }
//
//    public static ReplyKeyboard getDefaultKeyboard() {
//        return DEFAULT_KEYBOARD;
//    }
//
//    public static ReplyKeyboard getProductDeleteKeyboard(List<String> productCommands) {
//        return new ProductDeleteKeyboard(productCommands);
//    }
//
//    public static ReplyKeyboard getProductSearchResultKeyboard(List<String> productCommands) {
//        return new ProductSearchSelectKeyboard(productCommands);
//    }
//
//    public static ReplyKeyboard getEmailDeleteKeyboard() {
//        return new EmailDeleteKeyboard();
//    }
//
//    public static ProductTypeSelectKeyboard getProductTypeSelectKeyboard(List<String> commands) {
//        return new ProductTypeSelectKeyboard(commands);
//    }
}
