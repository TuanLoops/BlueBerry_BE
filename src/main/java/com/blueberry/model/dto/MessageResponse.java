package com.blueberry.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse {
    private String message;
}
