package com.example.hackathon.Repository;

import com.sparkyconsulting.aihub.model.entity.AIRequest;
import com.sparkyconsulting.aihub.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AIRequestRepository extends JpaRepository<AIRequest, Long> {
    List<AIRequest> findByUser(User user);
    Page<AIRequest> findByUserId(Long userId, Pageable pageable);
    
    @Query("SELECT r FROM AIRequest r WHERE r.user.id = :userId AND r.requestTimestamp >= :startTime")
    List<AIRequest> findByUserIdAndRequestTimestampAfter(
            @Param("userId") Long userId, 
            @Param("startTime") LocalDateTime startTime);
    
    @Query("SELECT r FROM AIRequest r WHERE r.user.id = :userId AND r.modelName = :modelName " +
           "AND r.modelProvider = :modelProvider AND r.requestTimestamp >= :startTime")
    List<AIRequest> findByUserIdAndModelAndRequestTimestampAfter(
            @Param("userId") Long userId,
            @Param("modelName") String modelName,
            @Param("modelProvider") String modelProvider,
            @Param("startTime") LocalDateTime startTime);
    
    @Query("SELECT COUNT(r) FROM AIRequest r WHERE r.user.id = :userId AND r.modelName = :modelName " +
           "AND r.modelProvider = :modelProvider AND r.requestTimestamp >= :startTime")
    int countRequestsByUserIdAndModelAndRequestTimestampAfter(
            @Param("userId") Long userId,
            @Param("modelName") String modelName,
            @Param("modelProvider") String modelProvider,
            @Param("startTime") LocalDateTime startTime);
    
    @Query("SELECT SUM(r.totalTokens) FROM AIRequest r WHERE r.user.id = :userId AND r.modelName = :modelName " +
           "AND r.modelProvider = :modelProvider AND r.requestTimestamp >= :startTime")
    Integer sumTokensByUserIdAndModelAndRequestTimestampAfter(
            @Param("userId") Long userId,
            @Param("modelName") String modelName,
            @Param("modelProvider") String modelProvider,
            @Param("startTime") LocalDateTime startTime);
    
    @Query("SELECT r FROM AIRequest r WHERE r.user.company.id = :companyId")
    Page<AIRequest> findByCompanyId(@Param("companyId") Long companyId, Pageable pageable);
    
    @Query("SELECT COUNT(r) FROM AIRequest r WHERE r.user.company.id = :companyId")
    long countByCompanyId(@Param("companyId") Long companyId);
    
    @Query("SELECT SUM(r.totalTokens) FROM AIRequest r WHERE r.user.company.id = :companyId")
    Integer sumTokensByCompanyId(@Param("companyId") Long companyId);
}
