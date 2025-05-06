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
public class ModelRestrictionDTO {
    private Long id;
    
    @NotBlank(message = "Model name is required")
    private String modelName;
    
    @NotBlank(message = "Model provider is required")
    private String modelProvider;
    
    private boolean enabled;
    
    @Min(value = 1, message = "Max requests must be at least 1")
    private Integer maxRequestsPerWindow;
    
    @Min(value = 1, message = "Max tokens must be at least 1")
    private Integer maxTokensPerWindow;
    
    @NotNull(message = "Window duration is required")
    @Min(value = 1, message = "Window duration must be at least 1 minute")
    private Integer windowDurationMinutes;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    private Long companyId;
}
