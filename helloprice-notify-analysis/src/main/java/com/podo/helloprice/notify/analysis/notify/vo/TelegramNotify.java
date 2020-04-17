package com.podo.helloprice.notify.analysis.notify.vo;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class TelegramNotify {
    private String telegramId;
    private String contents;
}
