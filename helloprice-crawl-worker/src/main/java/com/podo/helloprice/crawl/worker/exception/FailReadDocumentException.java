package com.podo.helloprice.crawl.worker.exception;

public class FailReadDocumentException extends RuntimeException {
    public FailReadDocumentException(Exception e) {
        super(e);
    }
}
