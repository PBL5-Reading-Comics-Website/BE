package com.example.readingcomicwebsite.auth;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiDataResponse {

    private String status;
    private Object data;
    private Object error;
    private Object meta;

    // Create successful generic response instance
    public static ApiDataResponse success(Object data, Object meta) {
        return ApiDataResponse.builder().status("success").data(data).meta(meta).build();
    }

    public static ApiDataResponse successWithoutMeta(Object data) {
        return ApiDataResponse.builder().status("success").data(data).build();
    }

    public static ApiDataResponse successWithoutMetaAndData() {
        return ApiDataResponse.builder().status("success").build();
    }

    // Create failed generic response instance
    public static ApiDataResponse error(Object error) {
        return ApiDataResponse.builder().status("fail").error(error).build();
    }
}
