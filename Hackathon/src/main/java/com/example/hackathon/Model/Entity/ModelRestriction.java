package com.sparkyconsulting.aihub.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "model_restrictions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModelRestriction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String modelName;

    @Column(nullable = false)
    private String modelProvider;

    @Column(nullable = false)
    private boolean enabled;

    @Column(name = "max_requests_per_window")
    private Integer maxRequestsPerWindow;

    @Column(name = "max_tokens_per_window")
    private Integer maxTokensPerWindow;

    @Column(name = "window_duration_minutes")
    private Integer windowDurationMinutes;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
