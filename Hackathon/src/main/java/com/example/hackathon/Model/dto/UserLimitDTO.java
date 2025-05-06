package com.sparkyconsulting.aihub.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLimitDTO {
    private Long id;
    
    @NotNull(message = "User ID is required")
    private Long userId;
    
    private String userName;
    
    @NotBlank(message = "Model name is required")
    private String modelName;
    
    @NotBlank(message = "Model provider is required")
    private String modelProvider;
    
    @Min(value = 1, message = "Max requests must be at least 1")
    private Integer maxRequestsPerWindow;
    
    @Min(value = 1, message = "Max tokens must be at least 1")
    private Integer maxTokensPerWindow;
    
    @NotNull(message = "Window duration is required")
    @Min(value = 1, message = "Window duration must be at least 1 minute")
    private Integer windowDurationMinutes;
    
    private Integer currentRequestsCount;
    
    private Integer currentTokensCount;
    
    private LocalDateTime windowStartTime;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
}
