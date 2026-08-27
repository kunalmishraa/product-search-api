package com.example.productsearch.exception;

import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler({ProductNotFoundException.class, InventoryNotFoundException.class})
	public ResponseEntity<ErrorResponse> handleNotFound(RuntimeException exception, HttpServletRequest request) {
		return buildResponse(HttpStatus.NOT_FOUND, exception.getMessage(), request.getRequestURI());
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResponse> handleBadRequest(IllegalArgumentException exception, HttpServletRequest request) {
		return buildResponse(HttpStatus.BAD_REQUEST, exception.getMessage(), request.getRequestURI());
	}

	private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String message, String path) {
		ErrorResponse response = new ErrorResponse();
		response.setTimestamp(Instant.now());
		response.setStatus(status.value());
		response.setError(status.getReasonPhrase());
		response.setMessage(message);
		response.setPath(path);
		return ResponseEntity.status(status).body(response);
	}
}
