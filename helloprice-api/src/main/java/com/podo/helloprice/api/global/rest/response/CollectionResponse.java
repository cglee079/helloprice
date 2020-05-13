package com.podo.helloprice.api.global.rest.response;

import com.podo.helloprice.api.global.rest.ApiResponse;
import lombok.Getter;

import java.util.Collection;

@Getter
public class CollectionResponse implements ApiResponse {

    public int size;
    public Collection<?> contents;

    public CollectionResponse(Collection<?> result) {
        this.size = result.size();
        this.contents = result;
    }

    public static CollectionResponse ok(Collection<?> result) {
        return new CollectionResponse(result);
    }
}
