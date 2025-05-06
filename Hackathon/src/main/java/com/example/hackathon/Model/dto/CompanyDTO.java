package com.sparkyconsulting.aihub.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDTO {
    private Long id;
    
    @NotBlank(message = "Company name is required")
    private String name;
    
    @NotBlank(message = "RUC is required")
    @Pattern(regexp = "^[0-9]{11}$", message = "RUC must be 11 digits")
    private String ruc;
    
    private LocalDateTime registrationDate;
    
    private boolean active;
    
    @Email(message = "Email should be valid")
    private String contactEmail;
    
    private String contactPhone;
    
    private UserDTO adminUser;
}
