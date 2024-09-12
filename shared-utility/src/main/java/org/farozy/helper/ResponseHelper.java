package org.farozy.helper;

import org.farozy.payload.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseHelper {

    public static <T> ResponseEntity<ApiResponse<T>> buildResponseData(HttpStatus status, String message, T data) {
        ApiResponse<T> response = ApiResponse.withData(status.value(), message, data);
        return new ResponseEntity<>(response, status);
    }

    public static <T> ResponseEntity<ApiResponse<T>> buildResponseToken(
            HttpStatus status, String message, T data, T tokenInfo
    ) {
        ApiResponse<T> response = ApiResponse.withToken(status.value(), message, data, tokenInfo);

        return new ResponseEntity<>(response, status);
    }

}
