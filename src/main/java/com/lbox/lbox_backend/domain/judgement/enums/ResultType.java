package com.lbox.lbox_backend.domain.judgement.enums;

public enum ResultType {
    PLAINTIFF_WIN("원고승"),
    PLAINTIFF_LOSE("원고패"),
    PARTIAL_WIN("원고일부승"),
    DISMISSAL("기각"),
    REJECTION("각하");

    private final String description;

    ResultType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}