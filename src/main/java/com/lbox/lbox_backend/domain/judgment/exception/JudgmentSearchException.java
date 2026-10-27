package com.lbox.lbox_backend.domain.judgment.exception;

//클라이언트가 전달한 검색 조건이 잘못됐을 때 사용한다.

public class JudgmentSearchException extends RuntimeException {

    private final String code;

    public JudgmentSearchException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}