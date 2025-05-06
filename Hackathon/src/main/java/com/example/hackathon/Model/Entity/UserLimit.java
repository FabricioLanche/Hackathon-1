package com.sparkyconsulting.aihub.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_limits")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLimit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String modelName;

    @Column(nullable = false)
    private String modelProvider;

    @Column(name = "max_requests_per_window")
    private Integer maxRequestsPerWindow;

    @Column(name = "max_tokens_per_window")
    private Integer maxTokensPerWindow;

    @Column(name = "window_duration_minutes")
    private Integer windowDurationMinutes;

    @Column(name = "current_requests_count")
    private Integer currentRequestsCount;

    @Column(name = "current_tokens_count")
    private Integer currentTokensCount;

    @Column(name = "window_start_time")
    private LocalDateTime windowStartTime;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        currentRequestsCount = 0;
        currentTokensCount = 0;
        windowStartTime = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
