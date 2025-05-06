package com.sparkyconsulting.aihub.model.dto;

import com.sparkyconsulting.aihub.model.entity.AIRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AIRequestDTO {
    private Long id;
    
    private Long userId;
    
    private String userName;
    
    @NotBlank(message = "Model name is required")
    private String modelName;
    
    @NotBlank(message = "Model provider is required")
    private String modelProvider;
    
    @NotNull(message = "Request type is required")
    private AIRequest.RequestType requestType;
    
    @NotBlank(message = "Prompt is required")
    private String prompt;
    
    private String response;
    
    private String errorMessage;
    
    private Integer inputTokens;
    
    private Integer outputTokens;
    
    private Integer totalTokens;
    
    private LocalDateTime requestTimestamp;
    
    private LocalDateTime responseTimestamp;
    
    private boolean successful;
    
    private Long processingTimeMs;
    
    private String filenameReference;
}

// DTO para realizar una solicitud al sistema
@Data
@NoArgsConstructor
@AllArgsConstructor
class AIRequestInputDTO {
    @NotBlank(message = "Model name is required")
    private String modelName;
    
    @NotBlank(message = "Model provider is required")
    private String modelProvider;
    
    @NotNull(message = "Request type is required")
    private AIRequest.RequestType requestType;
    
    @NotBlank(message = "Prompt is required")
    private String prompt;
    
    private String filenameReference;
}
