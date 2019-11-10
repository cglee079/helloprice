package com.podo.sadream.pooler.exception;

public class FailGetDocumentException extends RuntimeException {
    public FailGetDocumentException(String url) {
        super("HTML 문서를 가져올 수 없습니다. url : " + url);
    }
}
