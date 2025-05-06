package com.sparkyconsulting.aihub.model.dto;

import com.sparkyconsulting.aihub.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    
    @NotBlank(message = "Full name is required")
    private String fullName;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;
    
    private String password;
    
    private boolean active;
    
    private LocalDateTime creationDate;
    
    private LocalDateTime lastLogin;
    
    private Long companyId;
    
    private String companyName;
    
    private User.UserRole role;
}
