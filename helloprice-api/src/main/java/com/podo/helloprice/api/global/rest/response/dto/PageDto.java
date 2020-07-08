package com.podo.helloprice.api.global.rest.response.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;


@Getter
public class PageDto<T> {
    private Integer currentPage;
    private Integer pageSize;
    private Integer totalPages;
    private Long totalElements;
    private List<T> contents;

    @Builder
    public PageDto(Integer currentPage, Integer pageSize, Integer totalPages, Long totalElements, List<T> contents) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.contents = contents;
    }
}

