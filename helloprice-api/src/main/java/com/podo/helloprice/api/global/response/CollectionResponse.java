package com.podo.helloprice.api.global.response;

import lombok.Getter;

import java.util.Collection;

@Getter
public class CollectionResponse {

    private Integer size;
    private Collection<?> contents;

    private CollectionResponse(Collection<?> result) {
        this.size = result.size();
        this.contents = result; }

    public static CollectionResponse ok(Collection<?> result) {
        return new CollectionResponse(result);
    }

}
