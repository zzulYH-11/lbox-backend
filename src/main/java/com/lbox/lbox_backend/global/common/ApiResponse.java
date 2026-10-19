package com.lbox.lbox_backend.global.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"isSuccess","code","message","result","error"})
public class ApiResponse<T> {

    @JsonProperty("isSuccess")
    private Boolean isSuccess;

    @JsonProperty("code")
    private String code;

    @JsonProperty("message")
    private String message;

    @JsonProperty("result")
    private final T result;

    @JsonProperty("error")
    private Object error;

    //result가 있는 경우 성공 응답
    public static <T> ApiResponse<T> onSuccess(String message, T result)
    {
        return new ApiResponse<>(true, "200", message, result, null);
    }

    //result 없는 경우 성공 응답
    public static <T> ApiResponse<T> onSuccess(String message)
    {
        return new ApiResponse<>(true, "200", message, null, null);
    }

    //실패 응답
    public static <T> ApiResponse<T> onFailure(String errorCode, String errorMessage, Object error)
    {
        return new ApiResponse<>(false, errorCode, errorMessage, null, error);
    }

}

