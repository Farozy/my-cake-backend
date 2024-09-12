package org.farozy.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class ApiResponse<T> {

    private int status;
    private String message;
    @Builder.Default
    private T data = null;
    private T token;

    public static <T> ApiResponse<T> withData(int status, String message, T data) {
        return ApiResponse.<T>builder()
                .status(status)
                .message(message)
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> withToken(int status, String message, T data, T token) {
        return ApiResponse.<T>builder()
                .status(status)
                .message(message)
                .data(data)
                .token(token)
                .build();
    }

}
