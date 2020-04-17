package com.podo.helloprice.notify.analysis.notify.vo;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class GmailNotify {
    private String email;
    private String title;
    private String contents;
}
