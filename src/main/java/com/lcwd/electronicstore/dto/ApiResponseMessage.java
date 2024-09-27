package com.lcwd.electronicstore.dto;

import org.springframework.http.HttpStatus;

import com.lcwd.electronicstore.entities.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiResponseMessage {
private String message;
private boolean success;
private HttpStatus status;
}
