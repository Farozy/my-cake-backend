package org.farozy.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class ErrorResponseBuilder {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger logger = LoggerFactory.getLogger(ErrorResponseBuilder.class);

    public static Map<String, Object> validationErrorResponse(HttpStatus status, String message, Object errors) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", status.value());
        body.put("message", message);
        body.put(errors instanceof Map ? "errors" : "error", errors);

        logger.error("Responding with unauthorized error, Message " + message);

        return body;
    }

    public static void handleError(HttpServletResponse response, HttpStatus status, String errorType, String message) throws IOException {

        Map<String, Object> errorResponse = validationErrorResponse(
                status,
                errorType,
                message
        );

        logger.error("Responding with unauthorized error, Message " + message);

        response.setStatus(status.value());
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }


}
